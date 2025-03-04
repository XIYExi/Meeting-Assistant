package com.ruoyi.rag;

import com.ruoyi.rag.domain.EmbeddingMilvus;
import com.ruoyi.rag.domain.MessageEmbedding;
import com.ruoyi.rag.handler.MilvusPoolFactory;
import com.ruoyi.rag.utils.IdGenerator;
import io.milvus.Response.SearchResultsWrapper;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DataType;
import io.milvus.grpc.MutationResult;
import io.milvus.grpc.SearchResults;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.*;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.index.CreateIndexParam;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Milvus向量库操作类
 */
@Data
@Component
public class MilvusOptTest {
    private static final Logger log = LoggerFactory.getLogger(com.ruoyi.rag.utils.MilvusOperateUtils.class);
    private GenericObjectPool<MilvusServiceClient> milvusServiceClientGenericObjectPool;  // 管理链接对象的池子
    // https://milvus.io/cn/docs/v2.0.x/load_collection.md
    private final int MAX_POOL_SIZE = 5;

    private MilvusOptTest() {
        // 私有构造方法创建一个池
        // 对象池工厂
        MilvusPoolFactory milvusPoolFactory = new MilvusPoolFactory();
        // 对象池配置
        GenericObjectPoolConfig<MessageEmbedding> objectPoolConfig = new GenericObjectPoolConfig<>();
        objectPoolConfig.setMaxTotal(8);
        AbandonedConfig abandonedConfig = new AbandonedConfig();

        abandonedConfig.setRemoveAbandonedOnMaintenance(true); //在Maintenance的时候检查是否有泄漏

        abandonedConfig.setRemoveAbandonedOnBorrow(true); //borrow 的时候检查泄漏

        abandonedConfig.setRemoveAbandonedTimeout(MAX_POOL_SIZE); //如果一个对象borrow之后10秒还没有返还给pool，认为是泄漏的对象

        // 对象池
        milvusServiceClientGenericObjectPool = new GenericObjectPool(milvusPoolFactory, objectPoolConfig);
        milvusServiceClientGenericObjectPool.setAbandonedConfig(abandonedConfig);
        milvusServiceClientGenericObjectPool.setTimeBetweenEvictionRunsMillis(5000); //5秒运行一次维护任务
        log.info("milvus 对象池创建成功 维护了" + MAX_POOL_SIZE + "个对象");
    }

    // 创建一个Collection 类似于创建关系型数据库中的一张表
    private void createCollection(String collection) {
        MilvusServiceClient milvusServiceClient = null;
        try {
            // 通过对象池管理对象
            milvusServiceClient = milvusServiceClientGenericObjectPool.borrowObject();
            FieldType fieldType1 = FieldType.newBuilder()
                    .withName(EmbeddingMilvus.Field.composite_id)
                    .withDescription("组合id")
                    .withPrimaryKey(true)
                    .withAutoID(false)
                    .withDataType(DataType.Int64)
                    .build();
            FieldType fieldType2 = FieldType.newBuilder()
                    .withName(EmbeddingMilvus.Field.original_id)
                    .withDescription("原始id")
                    .withDataType(DataType.Int64)
                    .build();
            FieldType fieldType3 = FieldType.newBuilder()
                    .withName(EmbeddingMilvus.Field.type)
                    .withDescription("存储数据库")
                    .withDataType(DataType.Int64)
                    .build();
            FieldType fieldType4 = FieldType.newBuilder()
                    .withName(EmbeddingMilvus.Field.feature)
                    .withDescription("特征向量")
                    .withDataType(DataType.FloatVector)
                    .withDimension(EmbeddingMilvus.FEATURE_DIM)
                    .build();
            CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
                    .withCollectionName(collection)
                    .withDescription("会议特征向量库")
                    .withShardsNum(2)
                    .addFieldType(fieldType1)
                    .addFieldType(fieldType2)
                    .addFieldType(fieldType3)
                    .addFieldType(fieldType4)
                    .build();
            R<RpcStatus> result = milvusServiceClient.createCollection(createCollectionReq);
            log.info("创建结果" + result.getStatus() + "0 为成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 回收对象到对象池
            if (milvusServiceClient != null) {
                milvusServiceClientGenericObjectPool.returnObject(milvusServiceClient);
            }
        }
    }

    private void createIndex(String collection) {
         MilvusServiceClient milvusServiceClient = null;
        try {
            // 通过对象池管理对象
            milvusServiceClient = milvusServiceClientGenericObjectPool.borrowObject();
            R<RpcStatus> rpcStatusR = milvusServiceClient.createIndex(
                    CreateIndexParam.newBuilder()
                            .withCollectionName(collection)
                            .withFieldName(EmbeddingMilvus.Field.feature)  // 向量字段名
                            .withIndexType(IndexType.HNSW)  // 选择索引类型
                            .withMetricType(MetricType.L2)   // 根据场景选择距离计算方式（L2、IP等）
                            .withExtraParam("{\"M\":16,\"efConstruction\":200}")  // HNSW参数
                            .withSyncMode(true)  // 同步等待索引创建完成
                            .build()
            );
            log.info("创建索引 " + rpcStatusR);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 回收对象到对象池
            if (milvusServiceClient != null) {
                milvusServiceClientGenericObjectPool.returnObject(milvusServiceClient);
            }
        }
    }

    public void loadingLocation(String collection) {
        MilvusServiceClient milvusServiceClient = null;
        try {
            // 通过对象池管理对象
            milvusServiceClient = milvusServiceClientGenericObjectPool.borrowObject();
            R<RpcStatus> rpcStatusR = milvusServiceClient.loadCollection(
                    LoadCollectionParam.newBuilder()
                            .withCollectionName(collection)
                            .build());
            log.info("加载结果" + rpcStatusR);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 回收对象到对象池
            if (milvusServiceClient != null) {
                milvusServiceClientGenericObjectPool.returnObject(milvusServiceClient);
            }
        }


    }

    public void freedLoaction(String collection) {
        MilvusServiceClient milvusServiceClient = null;
        try {
            // 通过对象池管理对象
            milvusServiceClient = milvusServiceClientGenericObjectPool.borrowObject();
            R<RpcStatus> rpcStatusR = milvusServiceClient.releaseCollection(
                    ReleaseCollectionParam.newBuilder()
                            .withCollectionName(collection)
                            .build());
            log.info("加载结果" + rpcStatusR);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 回收对象到对象池
            if (milvusServiceClient != null) {
                milvusServiceClientGenericObjectPool.returnObject(milvusServiceClient);
            }
        }
    }


    // 删除一个Collection
    private void delCollection(String collection) {
        MilvusServiceClient milvusServiceClient = null;
        try {
            // 通过对象池管理对象
            milvusServiceClient = milvusServiceClientGenericObjectPool.borrowObject();
            R<RpcStatus> book = milvusServiceClient.dropCollection(
                    DropCollectionParam.newBuilder()
                            .withCollectionName(collection)
                            .build());
            log.info("删除" + book.getStatus() + " 0 为成功");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 回收对象到对象池
            if (milvusServiceClient != null) {
                milvusServiceClientGenericObjectPool.returnObject(milvusServiceClient);
            }
        }
    }


    // 插入数据 和对应的字段相同

    public long insert(long originalId, long dbType, List<Float> embedding) {
        MilvusServiceClient milvusServiceClient = null;
        try {
            // 通过对象池管理对象
            milvusServiceClient = milvusServiceClientGenericObjectPool.borrowObject();
            List<InsertParam.Field> fields = new ArrayList<>();
            // TODO 判断这么插入对不对
            long compose_id = 0L;
            if (Objects.equals(dbType, 1L)) {
                compose_id = IdGenerator.generateMeetingId(originalId);
            } else {
                compose_id = IdGenerator.generateAgendaId(originalId);
            }

            fields.add(new InsertParam.Field(EmbeddingMilvus.Field.composite_id, DataType.Int64, Collections.singletonList(compose_id)));
            fields.add(new InsertParam.Field(EmbeddingMilvus.Field.original_id, DataType.Int64, Collections.singletonList(originalId)));
            fields.add(new InsertParam.Field(EmbeddingMilvus.Field.type, DataType.Int64, Collections.singletonList(dbType)));
            fields.add(new InsertParam.Field(EmbeddingMilvus.Field.feature, DataType.FloatVector, Collections.singletonList(embedding)));
            InsertParam insertParam = InsertParam.newBuilder()
                    .withCollectionName("meeting_home")
                    .withPartitionName("_default")
                    .withFields(fields)
                    .build();
            R<MutationResult> insertResult = milvusServiceClient.insert(insertParam);
            if (insertResult.getStatus() == 0) {
                return insertResult.getData().getIDs().getIntId().getData(0);
            } else {
                log.error("特征值上传失败 加入失败队列稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;

        } finally {
            // 回收对象到对象池
            if (milvusServiceClient != null) {
                milvusServiceClientGenericObjectPool.returnObject(milvusServiceClient);
            }
        }
        return 0;
    }


    // 根据向量搜索数据
    public List<?> searchByFeature(String collection, List<Float> search_vectors) {
        MilvusServiceClient milvusServiceClient = null;
        try {
            // 通过对象池管理对象
            milvusServiceClient = milvusServiceClientGenericObjectPool.borrowObject();
            List<String> search_output_fields = Arrays.asList(EmbeddingMilvus.Field.composite_id);
            SearchParam searchParam = SearchParam.newBuilder()
                    .withCollectionName(collection)
                    .withPartitionNames(Collections.singletonList("_default"))
                    .withMetricType(MetricType.L2)
                    .withOutFields(search_output_fields)
                    .withTopK(EmbeddingMilvus.SEARCH_K)
                    .withVectors(Collections.singletonList(search_vectors))
                    .withVectorFieldName(EmbeddingMilvus.Field.feature)
                    .withParams(EmbeddingMilvus.SEARCH_PARAM)
                    .build();
            R<SearchResults> respSearch = milvusServiceClient.search(searchParam);
            if (respSearch.getStatus() == 0) {
                SearchResultsWrapper wrapperSearch = new SearchResultsWrapper(respSearch.getData().getResults());
                List<?> fieldData = wrapperSearch.getFieldData(EmbeddingMilvus.Field.composite_id, 0);
                return fieldData;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();

        } finally {
            // 回收对象到对象池
            if (milvusServiceClient != null) {
                milvusServiceClientGenericObjectPool.returnObject(milvusServiceClient);
            }
        }
        return new ArrayList<>();
    }


    public static void main(String[] args) {
        MilvusOptTest milvusOperateUtils = new MilvusOptTest();
        //milvusOperateUtils.createCollection("meeting_home");
        //milvusOperateUtils.createIndex("meeting_home");
        // milvusOperateUtils.delCollection("meeting_home");
        double[] emb = {-0.028126633,
    0.023895886,
    -0.06921972,
    -2.5080275E-4,
    -0.06945477,
    -0.012868523,
    0.008877713,
    0.011066537,
    0.0125061665,
    0.04740004,
    0.033650108,
    0.04207243,
    -0.004987287,
    -0.021016628,
    0.037273664,
    -0.045833092,
    0.11548372,
    -0.023543324,
    0.0070169684,
    -0.03729325,
    -0.011223231,
    0.008299903,
    -0.0360397,
    0.052061692,
    0.07752452,
    0.0067574433,
    -0.05331525,
    -0.029184321,
    0.054451283,
    -0.011947943,
    -0.017530179,
    0.017569352,
    0.036646888,
    -0.012466993,
    -0.040505487,
    -0.035510853,
    -0.027441096,
    -0.02240729,
    -0.09252801,
    -0.029928619,
    -3.522562E-4,
    0.04089722,
    0.015424599,
    -0.044422846,
    0.033180024,
    -0.031221347,
    0.014014349,
    -0.044971276,
    -0.005552856,
    -0.01828427,
    0.0017346552,
    0.017393071,
    0.08218618,
    -0.022368116,
    0.023367044,
    -0.02362167,
    0.015620466,
    0.0145334,
    -0.07822965,
    5.3435215E-4,
    -0.027774071,
    0.0155715,
    0.0078445105,
    -0.040015817,
    0.04602896,
    0.067926995,
    0.029634817,
    -0.015365838,
    -0.020174395,
    -0.03535416,
    0.03290581,
    0.028107047,
    0.02131043,
    0.015316871,
    -0.06346121,
    -0.028910106,
    0.05257095,
    0.005920108,
    0.028890518,
    0.021388777,
    0.060797404,
    -0.0010534022,
    -0.017961089,
    -0.044187803,
    -0.0103614135,
    0.0030261595,
    0.0063852943,
    0.06710435,
    0.0069190343,
    -0.068553776,
    -0.0072715967,
    -0.011066537,
    0.036529366,
    -0.03570672,
    -0.0348449,
    0.027068947,
    -0.0348449,
    -0.030359527,
    0.021212496,
    0.037920028,
    0.030966718,
    0.02863589,
    0.009093168,
    -0.04273838,
    0.02967399,
    0.031887297,
    0.03131928,
    0.014298358,
    -0.036333498,
    0.031162586,
    0.022093901,
    0.016629187,
    0.021956794,
    -0.016501872,
    -0.018636832,
    -0.053902853,
    0.03431606,
    -0.0021692372,
    0.007766163,
    -0.025110267,
    0.02326911,
    0.016472492,
    0.036509782,
    -0.01511121,
    0.0290668,
    -0.037038624,
    -0.021878447,
    0.02796994,
    0.051630784,
    0.02460101,
    0.0053765746,
    0.007976721,
    -0.00986195,
    -0.024816466,
    -0.03521705,
    -0.012280919,
    0.019077536,
    -0.04457954,
    0.040740527,
    -0.08555511,
    0.014983896,
    -0.019488858,
    -0.030790437,
    -0.0015057346,
    -0.039173584,
    -0.08469329,
    0.011242819,
    -0.009049098,
    0.015062243,
    0.02863589,
    0.01306439,
    -0.05629244,
    -0.002707874,
    -0.005998455,
    0.046851605,
    0.0020309056,
    -0.017109063,
    -0.004651863,
    0.04939789,
    0.028831758,
    -0.0010956362,
    -0.011947943,
    -0.06424468,
    -0.0029478122,
    0.014788028,
    -0.019958941,
    0.03711697,
    0.012653068,
    -0.03786127,
    0.032651182,
    -0.010978397,
    -0.055665664,
    0.013926209,
    -0.0324749,
    0.012036084,
    -0.0050338055,
    0.06502815,
    0.03286664,
    -0.038488045,
    -0.05848616,
    -0.024914399,
    -4.2325835E-4,
    4.1851465E-5,
    -0.005298227,
    -0.02894928,
    -0.028557543,
    -0.033728454,
    -0.008921783,
    -0.020977454,
    0.066830136,
    0.010772736,
    -0.052061692,
    0.020311503,
    -0.049045328,
    0.029811097,
    0.04175904,
    0.00391491,
    -0.03699945,
    0.030986305,
    -0.024287622,
    -0.07388138,
    0.0355892,
    0.06502815,
    0.010478933,
    -0.027754484,
    -0.014288565,
    -0.0029649506,
    0.03445317,
    0.008730813,
    0.032416143,
    -0.0149643095,
    0.032631595,
    0.018215716,
    -0.010498521,
    -0.07513493,
    0.043756895,
    -0.0039663254,
    0.046420697,
    -0.050024666,
    -0.01193815,
    -0.020683652,
    0.057115085,
    -0.014925135,
    0.010557281,
    0.06385294,
    -0.011663935,
    -0.004169538,
    0.019909974,
    0.033180024,
    0.011693315,
    -0.0060082483,
    -0.0066399225,
    -0.006248187,
    -0.030085312,
    0.088297255,
    0.010782529,
    -0.06906303,
    0.019126503,
    0.0053912643,
    0.0032146822,
    0.014288565,
    -0.04034879,
    -0.023034068,
    -0.0700032,
    -0.01565964,
    7.1430585E-4,
    -0.0020578373,
    0.009034408,
    0.027088534,
    -0.02240729,
    0.020840347,
    0.010371206,
    -0.039957058,
    -0.023739193,
    0.014386498,
    0.010625835,
    0.047909293,
    -0.013710755,
    0.007996308,
    0.005396161,
    -5.2914943E-4,
    -0.015277698,
    0.040466312,
    0.017863154,
    0.047909293,
    -0.016619394,
    0.016257038,
    -0.05735013,
    -0.028831758,
    0.0049995286,
    -0.034570687,
    0.0071442826,
    0.016619394,
    0.0017322069,
    -0.0314368,
    -0.016942576,
    -0.01773584,
    -0.059583023,
    -0.02246605,
    0.08532006,
    -0.0019317474,
    -0.041249786,
    -0.011791249,
    0.0029992275,
    0.0013857655,
    0.006453848,
    0.01041038,
    -0.017461626,
    -0.04273838,
    -0.0045269975,
    -0.040231273,
    0.0023700017,
    0.07595758,
    -0.0186858,
    0.046812434,
    0.03729325,
    0.0017738289,
    -0.18615288,
    -0.031828538,
    -0.018029641,
    0.011624761,
    -0.010018644,
    -0.049241196,
    0.027245227,
    0.006630129,
    -0.06291278,
    0.003001676,
    -0.057506822,
    -0.052610125,
    -0.024718532,
    0.037645813,
    0.0073009767,
    0.003087368,
    -0.013338606,
    -0.002482626,
    0.0047277617,
    -0.039917883,
    -0.004747349,
    -0.040505487,
    0.0594655,
    0.032572836,
    0.03194606,
    0.0050068735,
    0.0656941,
    0.021232083,
    -0.026775144,
    -0.017755426,
    0.013250465,
    0.03149556,
    -0.013309225,
    0.028420435,
    0.029438948,
    -0.012466993,
    0.030986305,
    0.009014821,
    0.011742282,
    0.0043752,
    -0.004083846,
    0.0074527743,
    -0.019449685,
    -0.024326796,
    0.013612821,
    -0.038311765,
    -0.0057487236,
    -0.040936396,
    0.003919807,
    -0.026971012,
    -0.016746707,
    -0.013896829,
    0.04865359,
    -0.04579392,
    -0.011135091,
    -0.03120176,
    -0.024385557,
    0.022661919,
    -0.017579146,
    -0.0018044332,
    0.0015228731,
    -0.00937228,
    -0.006453848,
    -0.041484825,
    0.025325723,
    -0.00999416,
    0.06056236,
    2.4850742E-4,
    0.018871874,
    -0.015248317,
    0.028675064,
    -0.020526959,
    -0.01672712,
    -0.024209276,
    0.041563172,
    5.39861E-4,
    -0.006174736,
    -0.050533924,
    -0.02381754,
    -0.09777727,
    0.013798895,
    -0.029830685,
    -0.010067611,
    0.050024666,
    -0.027872005,
    -0.01606117,
    0.0014555434,
    0.006424468,
    -0.0011354218,
    0.17847484,
    0.021643406,
    -0.00941635,
    -0.016452905,
    -0.018450757,
    0.0073891175,
    -0.03460986,
    0.01242782,
    0.029634817,
    -0.028479196,
    -0.0011439911,
    0.020389851,
    -0.0021410813,
    -0.0077465763,
    -0.014797822,
    0.007883684,
    -0.041915733,
    -0.022994895,
    0.064832285,
    0.009191102,
    -0.015121004,
    0.018303858,
    -0.0125061665,
    0.011722695,
    -0.03748912,
    -0.009573044,
    -0.018852288,
    0.03192647,
    -0.041876562,
    0.023504151,
    -0.00836356,
    0.022975307,
    0.023915473,
    -0.0011115505,
    -0.0024299864,
    0.026716385,
    -0.014033937,
    -0.0069631045,
    -0.012946869,
    -0.0018264684,
    3.941842E-4,
    -0.041367304,
    0.022231009,
    3.1201149E-4,
    0.02826374,
    -0.0069141374,
    -0.023484563,
    -0.0013845414,
    -0.03729325,
    -0.035452094,
    -0.006145356,
    -0.016443113,
    -0.029713163,
    0.010449554,
    -0.0022524812,
    0.013808688,
    -0.0042380923,
    -0.06044484,
    0.01920485,
    -0.005469612,
    0.06032732,
    0.021114562,
    -0.04787012,
    0.0033591348,
    0.021800099,
    0.051004007,
    -0.017197203,
    -0.027245227,
    0.0061404593,
    0.020135222,
    0.013838069,
    0.01136034,
    0.0070071747,
    0.011918563,
    -0.009161722,
    0.040427137,
    0.002734806,
    0.03931069,
    -0.008133415,
    0.03272953,
    -0.0043556127,
    -6.3657074E-4,
    -0.014063316,
    0.044657886,
    -0.033630524,
    0.005851554,
    -0.014885962,
    0.059974756,
    0.054255415,
    -0.013113357,
    0.027676137,
    -0.016217863,
    -0.014846789,
    0.026422583,
    0.021506298,
    0.004480479,
    0.026540102,
    0.009842363,
    -0.04939789,
    -0.018764148,
    -0.050651446,
    -0.023425803,
    -2.938019E-4,
    0.0014836995,
    0.025717458,
    0.005631203,
    0.016413732,
    -0.03053581,
    0.006409778,
    0.0186858,
    -0.029027626,
    0.011184058,
    -0.013035011,
    0.0010852307,
    -2.6671702E-4,
    0.018734766,
    -0.033552174,
    0.021212496,
    0.030633742,
    0.0105768675,
    -0.033924323,
    0.016031789,
    -0.016678153,
    0.026030846,
    -0.016560633,
    0.006096389,
    -0.066830136,
    -0.029595643,
    -0.0040593627,
    0.036548954,
    0.012809762,
    -0.01730493,
    0.011810835,
    0.010381,
    0.008295006,
    0.019057948,
    0.004113226,
    0.027245227,
    0.004573516,
    0.02955647,
    0.03486449,
    0.049828798,
    -0.021721752,
    0.009538768,
    -7.211153E-5,
    -0.007491948,
    -0.0054500247,
    0.04266003,
    -0.02082076,
    0.028067874,
    -0.018597659,
    0.0011850009,
    0.017863154,
    -0.003843908,
    -0.0136422,
    0.028224567,
    -0.009014821,
    -0.008015894,
    0.0031412318,
    -0.040427137,
    0.028772997,
    -0.065850794,
    0.055665664,
    0.0732546,
    0.04743921,
    0.026246302,
    -0.019625966,
    0.020429024,
    0.01669774,
    -0.016159104,
    0.02460101,
    -0.010694388,
    -0.06334369,
    0.08743544,
    0.037449945,
    -0.04340433,
    0.012584514,
    -0.022975307,
    0.010077405,
    0.0133581925,
    0.05970054,
    -0.0048575243,
    0.0099354,
    -0.023425803,
    0.004490272,
    -0.016070964,
    0.07031658,
    -0.026305063,
    0.019430097,
    -0.039526146,
    -0.023641258,
    0.08806222,
    -0.0035794862,
    -0.020742413,
    0.010390793,
    0.009244966,
    -0.043874413,
    0.071100056,
    0.0040030507,
    -0.026207129,
    -0.01704051,
    -0.03304292,
    0.010567075,
    -0.049632933,
    0.0012547788,
    -0.033101678,
    -0.020027494,
    -0.006032732,
    -0.0037435256,
    -0.0060278354,
    -0.0042968527,
    -0.022603158,
    -0.043325983,
    0.020859934,
    -0.0076241586,
    0.028498782,
    -0.034061432,
    0.036646888,
    0.027499856,
    -0.042189952,
    0.029282255,
    4.713072E-4,
    0.026873078,
    -0.08908073,
    5.3289845E-5,
    0.019126503,
    -0.015150384,
    -0.025169028,
    0.021506298,
    0.041367304,
    0.0089658545,
    -0.061541703,
    0.01984142,
    -0.017089477,
    0.049280368,
    0.036548954,
    -0.0015583741,
    0.01978266,
    -0.008574118,
    -0.011115504,
    -0.023778366,
    0.02681432,
    -0.0045906543,
    0.0010442209,
    0.015855508,
    0.054686323,
    0.008769986,
    -0.011585588,
    -0.031534735,
    0.009866847,
    -0.037920028,
    0.03157391,
    0.0034570687,
    -0.027088534,
    -0.002896397,
    -0.029105972,
    -0.013544267,
    -0.05421624,
    -0.060131453,
    0.012907696,
    -0.0024446766,
    0.056135748,
    -0.03498201,
    0.0071442826,
    -0.021153735,
    -0.022250596,
    0.015963236,
    0.016913194,
    -0.061032444,
    -0.028204981,
    0.04994632,
    -0.005337401,
    -0.0017701563,
    0.001880332,
    0.008270523,
    0.03872309,
    -0.026598863,
    0.009524078,
    0.027695725,
    0.043247636,
    0.017471418,
    0.040740527,
    -0.03607887,
    0.034041844,
    -0.039839536,
    0.036862344,
    -0.03968284,
    -0.0016783433,
    0.01920485,
    -0.01687402,
    -0.0115953805,
    -0.049867973,
    -0.029282255,
    -0.0037680091,
    -0.041367304,
    -0.013416952,
    9.983144E-4,
    0.010322239,
    0.0033052713,
    0.0241701,
    -0.015365838,
    0.0048085577,
    -0.045872267,
    0.0114191,
    0.015238524,
    0.03327796,
    0.0022439118,
    -0.024561837,
    0.002070079,
    0.0033493415,
    -0.018558485,
    0.013808688,
    0.029380187,
    0.01087067,
    -0.046969127,
    -0.021545472,
    0.020076461,
    -0.037959203,
    -0.017549766,
    0.0045612743,
    0.02338663,
    0.010811909,
    -0.0070904186,
    -0.0024495733,
    0.016805468,
    0.0014983896,
    -0.061345834,
    -0.0010980845,
    0.014122077,
    0.04011375,
    0.0028253947,
    -0.035745896,
    0.04140648,
    -0.0053129173,
    0.038194243,
    0.007722093,
    -0.003430137,
    -0.0352954,
    0.0102536855,
    0.0049481136,
    0.009147032,
    0.027558617,
    0.0272844,
    -0.02015481,
    -0.013573647,
    -0.018666213,
    0.010811909,
    0.021173323,
    -0.052061692,
    0.0112721985,
    0.0035892797,
    0.016070964,
    -0.024444317,
    -0.022994895,
    0.0044853752,
    0.028361674,
    0.013887036,
    -0.03333672,
    -0.01733431,
    0.018245097,
    0.0038683915,
    0.051317394,
    0.019958941,
    -0.008569222,
    -0.025051506,
    0.008814056,
    -0.0053569875,
    0.033983085,
    -0.014797822,
    0.05029888,
    -0.009073582,
    0.036921103,
    -0.0021484264,
    -0.012623687,
    -0.056057397,
    0.05966137,
    0.023974234,
    -0.02894928,
    0.04763508,
    -0.025737045,
    -0.006669303,
    -0.049280368,
    -0.039859124,
    -0.0342573,
    0.0062383935,
    -0.01462154,
    -0.008236246,
    -0.050103012,
    0.009068685,
    0.0074674645,
    0.0013576095,
    -0.056919217,
    0.007722093,
    0.020977454,
    -0.11305496,
    0.02583498,
    0.011468067,
    -0.003388515,
    -0.0027323575,
    -0.011115504,
    -0.018960014,
    -0.033101678,
    0.022818612,
    -0.029105972,
    -0.007976721,
    -0.04548053,
    0.035275813,
    -1.247893E-4,
    0.013544267,
    -0.013994763,
    0.0056214095,
    0.0027201157,
    -0.007408704,
    0.03431606,
    -0.00521988,
    0.0102536855,
    -0.01908733,
    0.029145146,
    -0.0026221818,
    -0.037449945,
    -0.0010405483,
    -0.060092278,
    -0.048810285,
    -0.022485638,
    0.028498782,
    -0.023171175,
    0.015728194,
    0.026207129,
    0.007864097,
    -0.043443505,
    -0.014337532,
    -0.020468198,
    0.035922177,
    -0.0117520755,
    -0.022877373,
    0.02185886,
    -0.057545993,
    -7.234106E-5,
    0.018215716,
    0.037234493,
    0.010518108,
    -0.02174134,
    -0.047478385,
    0.04665574,
    -0.026050434,
    -0.0043115425,
    -0.03680358,
    -0.013279845,
    -0.033238787,
    0.0062530832,
    -0.04810516,
    0.031828538,
    0.001080334,
    0.0029869857,
    -0.029223494,
    0.028440023,
    -0.010635628,
    0.063030295,
    0.010684595,
    0.016168896,
    -0.032200687,
    0.014513813,
    -0.018362617,
    0.041837387,
    -0.018098196,
    0.005885831,
    0.010449554,
    -0.013857655,
    0.015326665,
    0.01447464,
    0.0059592817,
    0.008735709,
    -0.0046273796,
    -0.027499856,
    0.03688193,
    -0.014846789,
    -0.0794832,
    -0.02381754,
    -0.009142135,
    -0.007648642,
    -0.006571369,
    -0.0121829845,
    0.034590274,
    -0.011154678,
    -0.009431041,
    -0.014425673,
    -0.0049554585,
    0.06455807,
    -0.04426615,
    -0.03942821,
    -0.0029478122,
    -0.025071094,
    -0.024111342,
    0.014053524,
    -0.013240672,
    0.0111644715,
    0.0324749,
    -0.0088091595,
    0.013377779,
    -0.049006153,
    0.049593758,
    -0.025266962,
    0.023523737,
    -0.013534473,
    0.011144885,
    0.022250596,
    -0.04798764,
    0.028440023,
    -0.041171435,
    0.0043164394,
    0.0073842206,
    -0.010013747,
    -0.012653068,
    -0.024346383,
    -0.0034595171,
    -0.018254891,
    -0.0071736625,
    -0.03682317,
    0.0031730602,
    -0.006992485,
    0.015316871,
    0.029517295,
    -0.0055675455,
    -0.015189557,
    0.021995967,
    -0.022093901,
    -0.010263479,
    -0.024444317,
    -5.1935605E-4,
    -0.042033255,
    0.037743747,
    0.028342089,
    -0.049828798,
    -0.027441096,
    -0.0055332687,
    0.017608525,
    0.026265888,
    0.021349603,
    0.021956794,
    -0.06432302,
    0.02540407,
    -0.019146089,
    0.021545472,
    -0.05245343,
    0.005464715,
    0.037508707,
    -0.029928619,
    -0.0175008,
    0.0061698393,
    0.021232083,
    -0.058407813,
    0.008652465,
    -0.041288957,
    0.065224014,
    0.0021361846,
    0.0019807143,
    -0.023288695,
    -0.006336327,
    0.023112414,
    -0.030594569,
    0.009269449,
    -0.0014836995,
    -0.015640054,
    -0.011468067,
    0.020096049,
    0.060092278,
    -0.020350678,
    0.034335647,
    -0.009622011,
    0.03216151,
    0.007540915,
    -0.03186771,
    0.05288434,
    0.046499044,
    0.0071246955,
    0.005841761,
    0.022231009,
    0.026931839,
    -0.023915473,
    0.002911087,
    0.060836576,
    0.0055038887,
    -0.050886486,
    0.005136636,
    0.030202834,
    0.020742413,
    0.020526959,
    -0.01340716,
    -0.004519652,
    0.020546544,
    0.0010564626,
    -0.031534735,
    -0.026638037,
    0.006326534,
    -0.062481865,
    -0.02326911,
    -0.011566001,
    0.0028253947,
    8.7650894E-4,
    0.020644478,
    -0.01297625,
    0.011536621,
    0.0047938675,
    0.02546283,
    0.04634235,
    0.018764148,
    0.014425673,
    -0.0019623518,
    0.03498201,
    -0.0021986174,
    -0.007075729,
    -0.018352823,
    -0.0024091755,
    -0.054294586,
    0.009866847,
    0.009910917,
    0.014797822,
    -0.0040103956,
    -0.018352823,
    0.0031681636,
    0.006649716,
    0.020664066,
    0.008784676,
    0.010048024,
    -0.0136422,
    0.032337792,
    0.0065224017,
    0.032827463,
    0.05202252,
    -0.09762057,
    0.015826128,
    0.025913326,
    9.265471E-5,
    -0.013877242,
    0.019890387,
    0.024640184,
    0.03760664,
    0.00940166};

        log.info("加载内存" );
        milvusOperateUtils.loadingLocation("meeting_home");

        Double[] object = ArrayUtils.toObject(emb);
        List<Double> list = Arrays.asList(object);
        List<Float> floatList = list.stream().map(d -> d.floatValue()).collect(Collectors.toList());

        milvusOperateUtils.insert(1L, 1L, floatList);


        log.info("加载完毕");
        List<?> meetingHome = milvusOperateUtils.searchByFeature("meeting_home", floatList);
        for (Object composeId : meetingHome) {
            System.err.println(IdGenerator.getOriginalId((Long) composeId));
        }
    }
}
