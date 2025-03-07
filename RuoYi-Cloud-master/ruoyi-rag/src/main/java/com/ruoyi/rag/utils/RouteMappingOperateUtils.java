package com.ruoyi.rag.utils;

import com.ruoyi.rag.domain.EmbeddingRouteMappingMilvus;
import com.ruoyi.rag.domain.RouteMappingEmbedding;
import com.ruoyi.rag.handler.MilvusPoolFactory;
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
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RouteMappingOperateUtils {

    private static final Logger log = LoggerFactory.getLogger(RouteMappingOperateUtils.class);
    private GenericObjectPool<MilvusServiceClient> milvusServiceClientGenericObjectPool;  // 管理链接对象的池子
    // https://milvus.io/cn/docs/v2.0.x/load_collection.md
    private final int MAX_POOL_SIZE = 5;

    private RouteMappingOperateUtils() {
        // 私有构造方法创建一个池
        // 对象池工厂
        MilvusPoolFactory milvusPoolFactory = new MilvusPoolFactory();
        // 对象池配置
        GenericObjectPoolConfig<RouteMappingEmbedding> objectPoolConfig = new GenericObjectPoolConfig<>();
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

     /**
     * 现在创建的是route表
     * @param collection
     */
    private void createCollection(String collection) {
        MilvusServiceClient milvusServiceClient = null;
        try {
            // 通过对象池管理对象
            milvusServiceClient = milvusServiceClientGenericObjectPool.borrowObject();
            FieldType fieldType1 = FieldType.newBuilder()
                    .withName(EmbeddingRouteMappingMilvus.Field.id)
                    .withDescription("id")
                    .withPrimaryKey(true)
                    .withAutoID(false)
                    .withDataType(DataType.Int64)
                    .build();
            FieldType fieldType2 = FieldType.newBuilder()
                    .withName(EmbeddingRouteMappingMilvus.Field.originalId)
                    .withDescription("原始id")
                    .withDataType(DataType.Int64)
                    .build();
            FieldType fieldType3 = FieldType.newBuilder()
                    .withName(EmbeddingRouteMappingMilvus.Field.feature)
                    .withDescription("特征向量")
                    .withDataType(DataType.FloatVector)
                    .withDimension(EmbeddingRouteMappingMilvus.FEATURE_DIM)
                    .build();
            CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
                    .withCollectionName(collection)
                    .withDescription("路径跳转特征向量库")
                    .withShardsNum(2)
                    .addFieldType(fieldType1)
                    .addFieldType(fieldType2)
                    .addFieldType(fieldType3)
                    .build();
            R<RpcStatus> result = milvusServiceClient.createCollection(createCollectionReq);
            log.info("[route向量库]创建结果" + result.getStatus() + "0 为成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 回收对象到对象池
            if (milvusServiceClient != null) {
                milvusServiceClientGenericObjectPool.returnObject(milvusServiceClient);
            }
        }
    }

    /**
     * 创建 route mapping的索引
     * @param collection
     */
    private void createIndex(String collection) {
         MilvusServiceClient milvusServiceClient = null;
        try {
            // 通过对象池管理对象
            milvusServiceClient = milvusServiceClientGenericObjectPool.borrowObject();
            R<RpcStatus> rpcStatusR = milvusServiceClient.createIndex(
                    CreateIndexParam.newBuilder()
                            .withCollectionName(collection)
                            .withFieldName(EmbeddingRouteMappingMilvus.Field.feature)  // 向量字段名
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

     public long insert(long id,long originalId, List<Float> embedding) {
        MilvusServiceClient milvusServiceClient = null;
        try {
            // 通过对象池管理对象
            milvusServiceClient = milvusServiceClientGenericObjectPool.borrowObject();
            List<InsertParam.Field> fields = new ArrayList<>();
            fields.add(new InsertParam.Field(EmbeddingRouteMappingMilvus.Field.id, DataType.Int64, Collections.singletonList(id)));
            fields.add(new InsertParam.Field(EmbeddingRouteMappingMilvus.Field.originalId, DataType.Int64, Collections.singletonList(originalId)));
            fields.add(new InsertParam.Field(EmbeddingRouteMappingMilvus.Field.feature, DataType.FloatVector, Collections.singletonList(embedding)));
            InsertParam insertParam = InsertParam.newBuilder()
                    .withCollectionName(EmbeddingRouteMappingMilvus.COLLECTION_NAME)
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
            // 需要返回的字段
            // 修复composeId解析之后就不需要获取其他字段了
            List<String> search_output_fields = Arrays.asList(
                    EmbeddingRouteMappingMilvus.Field.id,
                    EmbeddingRouteMappingMilvus.Field.originalId
            );
            SearchParam searchParam = SearchParam.newBuilder()
                    .withCollectionName(collection)
                    .withPartitionNames(Collections.singletonList("_default"))
                    .withMetricType(MetricType.L2)
                    .withOutFields(search_output_fields)
                    .withTopK(EmbeddingRouteMappingMilvus.SEARCH_K)
                    .withVectors(Collections.singletonList(search_vectors))
                    .withVectorFieldName(EmbeddingRouteMappingMilvus.Field.feature)
                    .withParams(EmbeddingRouteMappingMilvus.SEARCH_PARAM)
                    .build();
            R<SearchResults> respSearch = milvusServiceClient.search(searchParam);
            if (respSearch.getStatus() == 0) {
                SearchResultsWrapper wrapperSearch = new SearchResultsWrapper(respSearch.getData().getResults());
                /**
                 * composeId解析原始id没有问题，但是isMeeting有计算问题
                 */
                List<?> fieldData = wrapperSearch.getFieldData(EmbeddingRouteMappingMilvus.Field.originalId, 0);
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
        RouteMappingOperateUtils milvusOperateUtils = new RouteMappingOperateUtils();
        milvusOperateUtils.createCollection(EmbeddingRouteMappingMilvus.COLLECTION_NAME);
        milvusOperateUtils.createIndex(EmbeddingRouteMappingMilvus.COLLECTION_NAME);
        //milvusOperateUtils.delCollection(EmbeddingRouteMappingMilvus.COLLECTION_NAME);


//        log.info("加载内存" );
//        milvusOperateUtils.loadingLocation(EmbeddingRouteMappingMilvus.COLLECTION_NAME);
//
//        double[] emb = {1.111, 222, }; // 1024dims
//        Double[] object = ArrayUtils.toObject(emb);
//        List<Double> list = Arrays.asList(object);
//        List<Float> floatList = list.stream().map(d -> d.floatValue()).collect(Collectors.toList());
//
//        milvusOperateUtils.insert(1L, floatList);
//
//
//        log.info("加载完毕");
//        List<?> meetingHome = milvusOperateUtils.searchByFeature(EmbeddingRouteMappingMilvus.COLLECTION_NAME, floatList);
//        for (Object composeId : meetingHome) {
//            System.err.println(IdGenerator.getOriginalId((Long) composeId));
//        }
    }
}
