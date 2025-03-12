package com.ruoyi.rag.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.EmbeddingMilvus;
import com.ruoyi.rag.domain.EmbeddingRouteMappingMilvus;
import com.ruoyi.rag.domain.StepSplitParamsEntity;
import com.ruoyi.rag.domain.StepSplitParamsFilterEntity;
import com.ruoyi.rag.domain.query.Meeting;
import com.ruoyi.rag.domain.query.MeetingAgenda;
import com.ruoyi.rag.domain.query.MeetingGeo;
import com.ruoyi.rag.domain.query.News;
import com.ruoyi.rag.mapper.RouteMappingMapper;
import com.ruoyi.rag.mapper.query.MeetingAgendaMapper;
import com.ruoyi.rag.mapper.query.MeetingGeoMapper;
import com.ruoyi.rag.mapper.query.MeetingMapper;
import com.ruoyi.rag.mapper.query.NewsMapper;
import com.ruoyi.rag.model.CustomPrompt;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
import com.ruoyi.rag.tcp.server.WebSocketServerHandler;
import com.ruoyi.rag.utils.IdGenerator;
import com.ruoyi.rag.utils.MilvusOperateUtils;
import com.ruoyi.rag.utils.RouteMappingOperateUtils;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * v2版本 用来替代v1版本的action操作，执行数据库查询操作
 */
@Component
public class ToolQueryHandler implements ToolSimpleHandler {
    private static final Logger logger = LoggerFactory.getLogger(ToolQueryHandler.class);
    @Resource
    private DomesticEmbeddingModel domesticEmbeddingModel;
    @Resource
    private MilvusOperateUtils milvus;
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private NewsMapper newsMapper;
    @Resource
    private MeetingGeoMapper meetingGeoMapper;
    @Resource
    private MeetingAgendaMapper meetingAgendaMapper;

    private WebSocketServerHandler nettyServerHandler = new WebSocketServerHandler();


    @Override
    public String handler(String content) {
        return "";
    }

    /**
     * v2 版本中取代action作为查询功能
     * query中需要处理依赖关系
     * @param params
     * @param step
     * @param output
     * @param uid
     * @return
     */
    @Override
    public boolean handler(StepSplitParamsEntity params, int step, Map<Integer, Map<String, Object>> output, String uid) {
        // 1. 先解析参数
        String keywords = params.getKeywords();
        String db = params.getDb();
        Integer dependency = params.getDependency();


        if (dependency != -1) {
            // 2 如果dependency不为-1就表示当前结果依赖之前的某一条数据，取出之后再次基础之上进行处理
            // 被依赖的往期结果
            Map<String, Object> dependencyOutput = output.get(dependency - 1);
            Map<String, Object> dependencyQueryResult = (Map<String, Object>) dependencyOutput.get("queryResult");
            String dependencyDB = (String)dependencyQueryResult.get("type");
            // 这三个都有meeting信息，虽然不太可能解析成这种样子，大概率是meeting后面跟着meeting_agenda或者meeting_geo
            if (
                    dependencyDB.equals("meeting") ||
                    dependencyDB.equals("meeting_agenda") ||
                    dependencyDB.equals("meeting_geo")
            ) {

                // 所依赖的meetingId
                long dependencyMeetingId = Long.parseLong(String.valueOf(dependencyQueryResult.get("id")));
                switch (db) {
                    case "meeting_agenda":
                        List<MeetingAgenda> meetingAgenda = meetingAgendaMapper.selectList(new QueryWrapper<MeetingAgenda>().eq("meeting_id", dependencyMeetingId));
                        // 解析出来的议程信息
                        String meetingAgendaQuerySuccessPrompt = "\n" + String.format(
                                CustomPrompt.QUERY_MEETING_AGENDA_SUCCESS_PROMPT,
                                meetingAgenda
                        ) + "\n";
                        output.get(step).put("status", true);
                        output.get(step).put("prompt", meetingAgendaQuerySuccessPrompt);
                        output.get(step).put("queryResult", meetingAgenda);
                        output.get(step).put("routePath", "");
                        output.get(step).put("intent", "query");
                        break;
                    case "meeting_geo":
                        Long geoId = Long.parseLong( meetingMapper.selectById(dependencyMeetingId).getLocation());
                        MeetingGeo meetingGeo = meetingGeoMapper.selectById(geoId);
                        String meetingGeoQuerySuccessPrompt = "\n" + String.format(
                                CustomPrompt.QUERY_GEO_SUCCESS_PROMPT,
                                meetingGeo.getFormattedAddress(),
                                meetingGeo.getCountry() + "-" + meetingGeo.getProvince() + "-" + meetingGeo.getCity() + "-" + meetingGeo.getDistrict(),
                                meetingGeo.getLocation()
                        ) + "\n";
                        output.get(step).put("status", true);
                        output.get(step).put("prompt", meetingGeoQuerySuccessPrompt);
                        output.get(step).put("queryResult", meetingGeo);
                        output.get(step).put("routePath", "");
                        output.get(step).put("intent", "query");
                        break;
                    default: break;
                }
            }
        }
        else {
            // 3 没有任何依赖，直接根据值对此进行解析
            // 3.1 数据库和milvus都查一次，如果数据库没有查询就走milvus
            Map<String, Object> resBySQL = this.generateQueryWrapperByFiltersForMeeting(keywords, db, params.getFilters());
            if (resBySQL == null) {
                // TODO 会议没查出来，肯定有问题，直接掐断
                output.get(step).put("status", false);
                output.get(step).put("prompt", CustomPrompt.QUERY_MEETING_FAILURE_PROMPT);
                output.get(step).put("queryResult", "");
                output.get(step).put("routePath", "");
                output.get(step).put("intent", "query");
                return false;
            }
            /**
             * 逻辑问题
             * meeting_agenda里面都是单条数据，用户说查询xxx会议议程，应该是拿到meetingId之后去查所有的agenda！
             * meeting_geo和会议同样是绑定状态，用户说查询xxx会议地点，也是通过meetingId去查询meetingGeo
             */
            // 这里在按照数据库，封装对应的prompt
            String outputSuccessFormatPrompt = "";
            if (db.equals("meeting")) {
                // 如果只查询meeting，那么就只返回meeting信息
                outputSuccessFormatPrompt = "\n"+String.format(
                        CustomPrompt.QUERY_MEETING_SUCCESS_PROMPT,
                        resBySQL.get("title"),
                        resBySQL.get("beginTime"),
                        resBySQL.get("address"),
                        resBySQL.get("views"),
                        resBySQL.get("content")
                )+"\n";
            }
            else if (db.equals("meeting_agenda")) {
                // 解析到db：meeting_agenda 那么肯定是要查议程
                // 此时步骤固定，查到meetingId之后去查agenda
                String meetingQuerySuccessPrompt = String.format(
                        CustomPrompt.QUERY_MEETING_SUCCESS_PROMPT,
                        resBySQL.get("title"),
                        resBySQL.get("beginTime"),
                        resBySQL.get("address"),
                        resBySQL.get("views"),
                        resBySQL.get("content")
                );
                String meetingAgendaQuerySuccessPrompt = String.format(
                        CustomPrompt.QUERY_MEETING_AGENDA_SUCCESS_PROMPT,
                        resBySQL.get("agenda")
                );
                outputSuccessFormatPrompt = "\n" + meetingQuerySuccessPrompt + "\n" + meetingAgendaQuerySuccessPrompt + "\n";
            }
            else if (db.equals("meeting_geo")) {
                // 解析到db：meeting_geo 那么肯定是要查地图
                // 此时步骤固定，查到 meetingId 之后去查 geo
                String meetingQuerySuccessPrompt = String.format(
                        CustomPrompt.QUERY_MEETING_SUCCESS_PROMPT,
                        resBySQL.get("title"),
                        resBySQL.get("beginTime"),
                        resBySQL.get("address"),
                        resBySQL.get("views"),
                        resBySQL.get("content")
                );
                String meetingGeoQuerySuccessPrompt = String.format(
                        CustomPrompt.QUERY_GEO_SUCCESS_PROMPT,
                        resBySQL.get("address"),
                        resBySQL.get("moreLocation"),
                        resBySQL.get("location")
                );
                outputSuccessFormatPrompt = "\n" + meetingQuerySuccessPrompt + "\n" + meetingGeoQuerySuccessPrompt + "\n";
            }

            output.get(step).put("status", true);
            output.get(step).put("prompt", outputSuccessFormatPrompt);
            output.get(step).put("queryResult", resBySQL);
            output.get(step).put("routePath", "");
            output.get(step).put("intent", "query");
            logger.info("Query Tool 独立查询完成");
        }
        return true;
    }


    private Map<String, Object> generateQueryWrapperByFiltersForMeeting(
            String keywords,
            String db,
            List<StepSplitParamsFilterEntity> filters
    ) {
        Map<String, Object> map = new HashMap<>();

        // 1. 先去milvus里面查，因为milvus一定能查到东西
        Response<Embedding> embed = domesticEmbeddingModel.embed(TextSegment.textSegment(keywords));
        List<Float> keywordsEmbedding = embed.content().vectorAsList();
        List<?> meetingMilvus = milvus.searchByFeature(EmbeddingMilvus.COLLECTION_NAME, keywordsEmbedding);
        Long composeId = (Long) meetingMilvus.get(0);
        long originalId = IdGenerator.getOriginalId((Long) composeId);
        boolean isMeeting = IdGenerator.isMeeting((Long) composeId);

        Meeting meeting = null;
        // milvus查出来是meeting，那么直接查
        if (isMeeting) {
            meeting = meetingMapper.selectById(originalId);
        }
        // milvus查出来是议程，那么就还换思路，根据filters去数据库查
        else {
            QueryWrapper<Meeting> meetingQueryWrapper= new QueryWrapper<>();
            String filter = filters.get(0).getFilter();
            if (filter.equals("title"))
                meetingQueryWrapper.like(filters.get(0).getFilter(), filters.get(0).getValue());

            meetingQueryWrapper.last("limit 1");
            List<Meeting> meetings = meetingMapper.selectList(meetingQueryWrapper);
            meeting = meetings.get(0);
            // 为空，直接返回，那么一定没查到，直接掐断即可
            if (meetings.isEmpty())
                return null;
        }


        // 会议只需要返回这些内容
        map.put("id", meeting.getId());
        map.put("type", db);
        map.put("title", meeting.getTitle());
        map.put("beginTime", meeting.getBeginTime());
        map.put("views", meeting.getViews());
        map.put("content", meeting.getRemark());

        switch (db) {
            case "meeting":
                // 只查询meeting和formatted_location
                long geoId = Long.parseLong(meeting.getLocation());
                MeetingGeo meetingGeo = meetingGeoMapper.selectById(geoId);
                // 补上地址
                map.put("address", meetingGeo.getFormattedAddress());
                return map;
            case "meeting_agenda":
                // 补上所有的议程信息
                QueryWrapper<MeetingAgenda> meetingAgendaQueryWrapper = new QueryWrapper<>();
                meetingAgendaQueryWrapper.eq("meeting_id", meeting.getId());
                List<MeetingAgenda> meetingAgenda = meetingAgendaMapper.selectList(meetingAgendaQueryWrapper);
                map.put("agenda", meetingAgenda);
                return map;
            case "meeting_geo":
                // 补上更加详细的地图信息
                MeetingGeo meetingGeoMore = meetingGeoMapper.selectById(Long.parseLong(meeting.getLocation()));
                map.put("moreLocation", meetingGeoMore.getCountry() + "-" + meetingGeoMore.getProvince() + "-" + meetingGeoMore.getCity() + "-" + meetingGeoMore.getDistrict());
                map.put("location", meetingGeoMore.getLocation());
                return map;
            default:
                break;
        }
        return null;
    }
}
