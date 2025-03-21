package com.ruoyi.rag.assistant.handler.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.rag.assistant.component.VectorSearchComponent;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.domain.Meeting;
import com.ruoyi.rag.assistant.domain.MeetingAgenda;
import com.ruoyi.rag.assistant.domain.MeetingGeo;
import com.ruoyi.rag.assistant.entity.Filter;
import com.ruoyi.rag.assistant.entity.MeetingResponse;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.assistant.utils.RequestContextHolder;
import com.ruoyi.rag.assistant.mapper.MeetingAgendaMapper;
import com.ruoyi.rag.assistant.mapper.MeetingMapper;
import com.ruoyi.rag.assistant.mapper.MeetingGeoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class QueryMeetingProcessor implements QueryProcessor {

    @Resource
    private VectorSearchComponent vectorSearchComponent;
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private MeetingAgendaMapper meetingAgendaMapper;
    @Autowired
    private MeetingGeoMapper meetingGeoMapper;


    @Override
    public void processor(StepDefinition step, QueryContext context) {
        String db = step.getDb();
        int dependency = step.getDependency();
        List<Filter> filters = step.getFilters();
        Map<String, String> dataBindings = step.getDataBindings();
        int totalSteps = RequestContextHolder.getRequestList().size();

        boolean needSearch = true;

        List<String> outputFields = new ArrayList<>();
        // 当前有输出，并且不是最后一项
        if (step.getOutputFields()!= null && !step.getOutputFields().isEmpty() && step.getStep() < totalSteps) {
            List<String> outputFieldsArray = step.getOutputFields();
            for (int i = 0; i < outputFieldsArray.size(); i++) {
                outputFields.add(outputFieldsArray.get(i));
            }
        }

        // file拆分出去了，这里只会查询meeting和meeting_geo两个数据库
        // 同时meeting和meeting_agenda合二为一，查询的时候返回合并的结果MeetingResponse
        if ("meeting".equals(db)) {
            QueryWrapper<Meeting> queryWrapper = new QueryWrapper<>();
            // 处理依赖和数据绑定
            handleDependencyResult(step, context, queryWrapper);

            for (Filter filter : filters) {
                String field = filter.getField();
                String operator = filter.getOperator();
                String value = String.valueOf(filter.getValue());

                // 如果包含step表示依赖了前置项，但是前置依赖已经在上面处理过了，这里直接跳过
                if (value != null && value.contains("step")) {
                    continue;
                }

                // 处理title, 只要查询title，就不在乎是什么匹配了，直接送到Milvus拿结果
                if ("title".equals(field)) {
                    MeetingResponse meetingResponse = vectorSearchComponent.vectorSearch(value);
                    if (step.getStep() != totalSteps)
                        context.storeStepResult(step.getStep(), Map.of("meeting_id", meetingResponse.getId()));
                    else
                        context.storeStepResult(step.getStep(), meetingResponse);
                    needSearch = false;
                    break;
                }

                switch (operator) {
                    case "=":
                        if ("NULL".equals(value)) {
                            queryWrapper.isNull(convertFieldName(field));
                        } else {
                            queryWrapper.eq(convertFieldName(field), value);
                        }
                        break;
                    case "LIKE":
                        queryWrapper.like(convertFieldName(field), value);
                        break;
                    case ">":
                        queryWrapper.gt(convertFieldName(field), value);
                        break;
                    case "<":
                        queryWrapper.lt(convertFieldName(field), value);
                        break;
                    case ">=":
                        queryWrapper.ge(convertFieldName(field), value);
                        break;
                    case "<=":
                        queryWrapper.le(convertFieldName(field), value);
                        break;
                }
            }

            // 如果直接查了title那么肯定输出完整的meeting，所以不需要在进行判断了
            if (needSearch) {
                Meeting meeting = meetingMapper.selectList(queryWrapper).get(0);
                // 不是最后一步，那么就按output_fields输出
                if (step.getStep() != totalSteps){
                    // 不然按照output_fields输出
                    if (!outputFields.isEmpty()){
                        Map<String, Object> resultItem = new HashMap<>();
                        for (String field : outputFields) {
                            switch (field) {
                                case "meeting_id":
                                    resultItem.put("meeting_id", meeting.getId());
                                    break;
                                case "title":
                                    resultItem.put("title", meeting.getTitle());
                                    break;
                                case "beginTime":
                                    resultItem.put("beginTime", meeting.getBeginTime());
                                    break;
                                case "geo_id":
                                    resultItem.put("geo_id", meeting.getLocation());
                                    break;
                                // 添加其他需要的字段
                                default:
                                    break;
                            }
                        }
                        context.storeStepResult(step.getStep(), resultItem);
                    }
                }
                // 最后一步，直接输出完整的结果
                else {
                    // 最后一项，直接查询出完整数据然后送进去
                    List<MeetingAgenda> agenda = meetingAgendaMapper.selectList(new QueryWrapper<MeetingAgenda>().eq("meeting_id", meeting.getId()));
                    MeetingResponse meetingResponse = new MeetingResponse();
                    BeanUtils.copyProperties(meeting, meetingResponse);
                    meetingResponse.setAgenda(agenda);
                    context.storeStepResult(step.getStep(), meetingResponse);
                }
            }
        }
        else if ("meeting_geo".equals(db)) {
            QueryWrapper<MeetingGeo> queryWrapper = new QueryWrapper<>();
            handleDependencyResult(step, context, queryWrapper);

            for (Filter filter : filters) {
                String field = filter.getField();
                String operator = filter.getOperator();
                String value = String.valueOf(filter.getValue());
                if (value != null && value.contains("step")) {
                    continue;
                }

                if ("address".equals(field)) {
                    queryWrapper.like(convertFieldName(field), value);
                    continue;
                }

                switch (operator) {
                    case "=":
                        queryWrapper.eq(convertFieldName(field), value);
                        break;
                    case "LIKE":
                        queryWrapper.like(convertFieldName(field), value);
                        break;
                    default:
                        break;
                }
            }
            List<MeetingGeo> meetingGeos = meetingGeoMapper.selectList(queryWrapper);

            System.err.println(meetingGeos.size());
            MeetingGeo meetingGeo = meetingGeos.get(0);
            if (step.getStep() != totalSteps) {
                Map<String, Object> resultItem = new HashMap<>();
                for (String field : outputFields) {
                    switch (field) {
                        case "geo_id":
                            resultItem.put("geo_id", meetingGeo.getId());
                            break;
                        case "address":
                            resultItem.put("address", meetingGeo.getFormattedAddress());
                            break;
                        // 添加其他需要的字段
                        default: break;
                    }
                }
                context.storeStepResult(step.getStep(), resultItem);
            }
            else {
                context.storeStepResult(step.getStep(), meetingGeo);
            }
        }
    }

    // 处理依赖关系
    public static <T> void handleDependencyResult(StepDefinition step, QueryContext context, QueryWrapper<T> queryWrapper) {
        int dependency = step.getDependency();
        Map<String, String> dataBindings = step.getDataBindings();
        if (dependency > 0) {
            Map dependencyResult = context.getDependencyResult(dependency, Map.class);
            if (dependencyResult != null) {
                for (Map.Entry<String, String> entry : dataBindings.entrySet()) {
                    String bindValue = entry.getValue();
                    if (bindValue.startsWith("step")) {
                        String[]parts = bindValue.split("\\.");
                        if (parts.length == 2) {
                            String dependencyStep = parts[0];
                            String dependencyField = parts[1];
                            Object value = dependencyResult.get(dependencyField);
                            if (value != null) {
                                if (step.getDb().equals("meeting")) {
                                    List<StepDefinition> requestList = RequestContextHolder.getRequestList();
                                    // 被依赖数据的原始数据，要获取它的db
                                    StepDefinition dStep = requestList.get(Integer.valueOf(dependencyStep.split("step")[1]) - 1);
                                    String dependencyDb = dStep.getDb();
                                    if ("meeting_geo".equals(dependencyDb)) {
                                        queryWrapper.eq("location", value);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private static final Map<String, String> fieldMapping = Map.of(
            "beginTime", "begin_time",
            "geoId", "location",
            "type","type",
            "meetingType", "meeting_type",
            "views", "views",
            "meeting_id", "id",
            "address", "formatted_address"
        );
    private static String convertFieldName(String logicalName) {
        // 字段映射逻辑
        return fieldMapping.getOrDefault(logicalName, "");
    }


}
