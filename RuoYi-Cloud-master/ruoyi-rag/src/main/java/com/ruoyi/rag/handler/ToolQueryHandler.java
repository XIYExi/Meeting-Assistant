package com.ruoyi.rag.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.MeetingRecResponse;
import com.ruoyi.rag.domain.StepSplitEntity;
import com.ruoyi.rag.domain.StepSplitParamsFilterEntity;
import com.ruoyi.rag.domain.query.Meeting;
import com.ruoyi.rag.domain.query.MeetingAgenda;
import com.ruoyi.rag.domain.query.MeetingGeo;
import com.ruoyi.rag.domain.query.News;
import com.ruoyi.rag.mapper.query.MeetingAgendaMapper;
import com.ruoyi.rag.mapper.query.MeetingGeoMapper;
import com.ruoyi.rag.mapper.query.MeetingMapper;
import com.ruoyi.rag.mapper.query.NewsMapper;
import com.ruoyi.rag.model.CustomPrompt;
import com.ruoyi.rag.tcp.server.WebSocketServerHandler;
import com.ruoyi.rec.api.RemoteRecService;
import com.ruoyi.rec.api.domain.MeetingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * v2版本 用来替代v1版本的action操作，执行数据库查询操作
 */
@Component
public class ToolQueryHandler implements ToolSimpleHandler {
    private static final Logger logger = LoggerFactory.getLogger(ToolQueryHandler.class);

    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private NewsMapper newsMapper;
    @Resource
    private MeetingGeoMapper meetingGeoMapper;
    @Resource
    private MeetingAgendaMapper meetingAgendaMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private RemoteRecService recService;


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
    public boolean handler(StepSplitEntity params, int step, Map<Integer, Map<String, Object>> output, String uid) {
        String keywords = params.getKeywords();
        String db = params.getDb();
        Integer dependency = params.getDependency();

        String query = params.getQuery();

        switch (query) {
            case "meeting":

                break;
            case "file":
                break;
            case "news":
                break;
            case "rec":
                // 获得用来推荐的最终列表
                List<MeetingRecResponse> queryRec = this.queryRec();
                break;
            case "rank":
                // 获取用来推荐的最终列表
                List<MeetingRecResponse> queryRank = this.queryRank();
                break;
        }


        return true;
    }


    private List<MeetingRecResponse> queryRec() {
        List<MeetingResponse> meetingResponses = recService.recForAgent(1L);
        List<MeetingRecResponse> collect = meetingResponses.stream().map(meetingResponse -> {
            MeetingRecResponse meetingRecResponse = new MeetingRecResponse();
            BeanUtils.copyProperties(meetingResponse, meetingRecResponse);
            meetingRecResponse.setLocation(meetingResponse.getLocation().getFormattedAddress());
            meetingRecResponse.setRoute("/pages/schedule/detail/index?id" + meetingResponse.getId());
            meetingRecResponse.setMeetingType(meetingResponse.getMeetingType() == 1L ? "线下会议" : "线上会议");
            return meetingRecResponse;
        }).collect(Collectors.toList());
        return collect;
    }

    private List<MeetingRecResponse> queryRank() {
        // 总榜
        Set<ZSetOperations.TypedTuple<Object>> ranks =  redisTemplate.opsForZSet().reverseRangeWithScores("meeting:view:rank", 0L, -1L).stream().limit(4L).collect(Collectors.toSet());
        List<MeetingRecResponse> collect = ranks.stream().map(elem -> {
            Meeting meeting = meetingMapper.selectById((Long) elem.getValue());
            MeetingGeo meetingGeo = meetingGeoMapper.selectById(Long.parseLong(meeting.getLocation()));

            MeetingRecResponse meetingRecResponse = new MeetingRecResponse();
            BeanUtils.copyProperties(meeting, meetingRecResponse);
            meetingRecResponse.setLocation(meetingGeo.getFormattedAddress());
            meetingRecResponse.setMeetingType(meeting.getMeetingType() == 1L ? "线下会议": "线上会议");
            return meetingRecResponse;
        }).collect(Collectors.toList());
        return collect;
    }

    private List<News> queryNes(StepSplitEntity params
    ) {
        return null;
    }


    private boolean oldHanlder(StepSplitEntity params, int step, Map<Integer, Map<String, Object>> output, String uid) {
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

                        // 为当前封装output - queryResult
                        dependencyQueryResult.put("agenda", meetingAgenda);

                        output.get(step).put("status", true);
                        output.get(step).put("prompt", meetingAgendaQuerySuccessPrompt);
                        output.get(step).put("queryResult", dependencyQueryResult);
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

                        dependencyQueryResult.put("moreLocation", meetingGeo.getCountry() + "-" + meetingGeo.getProvince() + "-" + meetingGeo.getCity() + "-" + meetingGeo.getDistrict());
                        dependencyQueryResult.put("location", meetingGeo.getLocation());

                        output.get(step).put("status", true);
                        output.get(step).put("prompt", meetingGeoQuerySuccessPrompt);
                        output.get(step).put("queryResult", dependencyQueryResult);
                        output.get(step).put("routePath", "");
                        output.get(step).put("intent", "query");
                        break;
                    case "meeting":
                        // 这个最简单，不管之前是哪个，只要是meeting、meeting_agenda或者meeting_geo里面的一个，那么肯定存储了完整的meeting信息
                        String meetingQuerySuccessPrompt = "\n" + String.format(
                                CustomPrompt.QUERY_MEETING_SUCCESS_PROMPT,
                                dependencyQueryResult.get("title"),
                                dependencyQueryResult.get("beginTime"),
                                dependencyQueryResult.get("address"),
                                dependencyQueryResult.get("views"),
                                dependencyQueryResult.get("content"),
                                dependencyQueryResult.get("url")
                        ) +"\n";
                        output.get(step).put("status", true);
                        output.get(step).put("prompt", meetingQuerySuccessPrompt);
                        output.get(step).put("queryResult", dependencyQueryResult);
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
                        resBySQL.get("content"),
                        resBySQL.get("url")
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
                        resBySQL.get("content"),
                        resBySQL.get("url")
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
                        resBySQL.get("content"),
                        resBySQL.get("url")
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
        // 查和会议相关的内容，第一步一定是先把会议查出来！
        Meeting meeting = null;
        switch (db) {
            case "meeting":
                QueryWrapper<Meeting> meetingQueryWrapper = new QueryWrapper<>();
                for (StepSplitParamsFilterEntity filter : filters) {
                    if (filter.getOperator().equals("eq"))
                        meetingQueryWrapper.eq(filter.getFilter(), filter.getValue());
                    else if (filter.getOperator().equals("like"))
                        meetingQueryWrapper.like(filter.getFilter(), filter.getValue());

                    if ("desc".equals(filter.getOrder()))
                        meetingQueryWrapper.orderByDesc(filter.getFilter());
                    else if ("asc".equals(filter.getOrder()))
                        meetingQueryWrapper.orderByAsc(filter.getFilter());
                }
                meetingQueryWrapper.last("limit 1");
                List<Meeting> meetings = meetingMapper.selectList(meetingQueryWrapper);
                if (meetings.isEmpty())
                    return null;
                meeting = meetings.get(0);
                // 只查询meeting和formatted_location
                long geoId = Long.parseLong(meeting.getLocation());
                MeetingGeo meetingGeo = meetingGeoMapper.selectById(geoId);
                // 补上地址
                map.put("address", meetingGeo.getFormattedAddress());
                break;
            case "meeting_agenda":
                QueryWrapper<MeetingAgenda> meetingAgendaQueryWrapper = new QueryWrapper<>();
                for (StepSplitParamsFilterEntity filter : filters) {
                    if (filter.getOperator().equals("eq"))
                        meetingAgendaQueryWrapper.eq(filter.getFilter(), filter.getValue());
                    else if (filter.getOperator().equals("like"))
                        meetingAgendaQueryWrapper.like(filter.getFilter().equals("title") ? "content" : filter.getFilter(), filter.getValue());

                    if ("desc".equals(filter.getOrder()))
                        meetingAgendaQueryWrapper.orderByDesc(filter.getFilter());
                    else if ("asc".equals(filter.getOrder()))
                        meetingAgendaQueryWrapper.orderByAsc(filter.getFilter());
                }
                meetingAgendaQueryWrapper.last("limit 1");
                List<MeetingAgenda> meetingAgendas = meetingAgendaMapper.selectList(meetingAgendaQueryWrapper);
                if (meetingAgendas.isEmpty())
                    return null;
                MeetingAgenda meetingAgenda = meetingAgendas.get(0);
                // 正常情况下，如果问xxx议程的xxx，其实应该只查一条数据就行了，这里为了防止发生莫名其妙的bug，索性都补上，大模型会自己取需要的数据的
                Long meetingId = meetingAgenda.getMeetingId();
                meeting = meetingMapper.selectById(meetingId);
                // 补上所有的议程信息
                QueryWrapper<MeetingAgenda> meetingAgendaQueryWrapperAgain = new QueryWrapper<>();
                meetingAgendaQueryWrapperAgain.eq("meeting_id", meetingId);
                List<MeetingAgenda> meetingAgendaFinal = meetingAgendaMapper.selectList(meetingAgendaQueryWrapperAgain);
                map.put("agenda", meetingAgendaFinal);
                break;
            case "meeting_geo":
                QueryWrapper<MeetingGeo> meetingGeoQueryWrapper=new QueryWrapper<>();
                for (StepSplitParamsFilterEntity filter : filters) {
                    if (filter.getOperator().equals("eq"))
                        meetingGeoQueryWrapper.eq(filter.getFilter(), filter.getValue());
                    else if (filter.getOperator().equals("like"))
                        meetingGeoQueryWrapper.like(filter.getFilter(), filter.getValue());

                     if ("desc".equals(filter.getOrder()))
                        meetingGeoQueryWrapper.orderByDesc(filter.getFilter());
                    else if ("asc".equals(filter.getOrder()))
                        meetingGeoQueryWrapper.orderByAsc(filter.getFilter());
                }
                meetingGeoQueryWrapper.last("limit 1");
                List<MeetingGeo> meetingGeos = meetingGeoMapper.selectList(meetingGeoQueryWrapper);
                if (meetingGeos.isEmpty())
                    return null;
                MeetingGeo meetingGeoMore = meetingGeos.get(0);
                Long locationId = meetingGeos.get(0).getId();
                QueryWrapper<Meeting> locationQueryWrapper = new QueryWrapper<>();
                locationQueryWrapper.eq("location_id", locationId);
                locationQueryWrapper.last("limit 1");
                List<Meeting> meetingsByGeo = meetingMapper.selectList(locationQueryWrapper);
                meeting = meetingsByGeo.get(0);

                // 补上更加详细的地图信息
                map.put("moreLocation", meetingGeoMore.getCountry() + "-" + meetingGeoMore.getProvince() + "-" + meetingGeoMore.getCity() + "-" + meetingGeoMore.getDistrict());
                map.put("location", meetingGeoMore.getLocation());
                break;
            default:
                break;
        }
        // 只要能到这里，说明meeting肯定查出来了，封装剩余会议需要返回的内容
        map.put("id", meeting.getId());
        map.put("type", db);
        map.put("url", meeting.getUrl());
        map.put("title", meeting.getTitle());
        map.put("beginTime", meeting.getBeginTime());
        map.put("views", meeting.getViews());
        map.put("content", meeting.getRemark());
        return map;
    }
}
