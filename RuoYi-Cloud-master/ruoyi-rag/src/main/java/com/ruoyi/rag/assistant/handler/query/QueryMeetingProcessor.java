package com.ruoyi.rag.assistant.handler.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.rag.assistant.component.VectorSearchComponent;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.domain.Meeting;
import com.ruoyi.rag.assistant.domain.MeetingAgenda;
import com.ruoyi.rag.assistant.domain.MeetingClip;
import com.ruoyi.rag.assistant.domain.MeetingGeo;
import com.ruoyi.rag.assistant.entity.Filter;
import com.ruoyi.rag.assistant.entity.MeetingResponse;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.assistant.utils.RequestContextHolder;
import com.ruoyi.rag.mapper.query.MeetingAgendaMapper;
import com.ruoyi.rag.mapper.query.MeetingMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class QueryMeetingProcessor implements QueryProcessor {

    @Resource
    private VectorSearchComponent vectorSearchComponent;
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private MeetingAgendaMapper meetingAgendaMapper;


    @Override
    public void processor(StepDefinition step, QueryContext context) {
        QueryWrapper<Meeting> wrapper = new QueryWrapper<>();
        // 如果有依赖，就优先走依赖
        if (step.getDependency() != -1) {
            // 通过前置依赖，获取meeting_id的值
            handleDependency(step, context, wrapper);
        }

        // 优先查询 title，如果有 title 那么其他就没有必要查询
        // 走 title 去 Milvus 肯定可以查到 Meeting
        Optional<Filter> titleFilter = step.getFilters().stream().filter(f -> "title".equals(f.getField())).findFirst();
        if (titleFilter.isPresent()) {
            handleTitleFilter(step, titleFilter.get(), context);
            return;
        }

        // 处理其他过滤条件
        step.getFilters().stream()
            .filter(f -> !"title".equals(f.getField()))
            .forEach(filter -> applyFilter(filter, wrapper, context));

        List<Meeting> results = meetingMapper.selectList(wrapper);

        List<MeetingResponse> queryMeetings = results.stream().map(elem -> {
            MeetingResponse result = new MeetingResponse();
            BeanUtils.copyProperties(elem, result);
            List<MeetingAgenda> agenda = meetingAgendaMapper.selectList(new QueryWrapper<MeetingAgenda>().eq("meeting_id", elem.getId()));
            result.setAgenda(agenda);
            return result;
        }).collect(Collectors.toList());
        context.storeStepResult(step.getStep(), queryMeetings);
    }


    private void handleDependency(StepDefinition step, QueryContext context, QueryWrapper<Meeting> wrapper) {
        // 拿到上一步依赖的数据
        // 但是此时上一步以来的到底是什么类型的数据，需要通过LocalThread中保存的steps拿到上一步数据到底是什么
        Object dependencyData = context.getDependencyResult(step.getDependency(), Object.class);

        // 被依赖的步骤
        StepDefinition dependentOnStep = RequestContextHolder.getRequestList().get(step.getDependency() - 1);
        // 获取被依赖的哪一步到底是哪个数据库的 等于判断到底是什么数据类型
        String dependentOnStepDb = dependentOnStep.getDb();
        if ("meeting".equals(dependentOnStepDb)) {
            MeetingResponse castDependentOnData = (MeetingResponse) dependencyData;
            wrapper.in("id", castDependentOnData.getId());
        }
        else if ("meeting_geo".equals(dependentOnStepDb)) {
            MeetingGeo castDependentOnData = (MeetingGeo) dependencyData;
            wrapper.in("location", castDependentOnData.getId());
        }
        else if ("meeting_clip".equals(dependentOnStepDb)) {
            MeetingClip castDependentOnData = (MeetingClip) dependencyData;
            wrapper.in("id", castDependentOnData.getMeetingId());
        }
    }


    private void handleTitleFilter(StepDefinition step, Filter filter, QueryContext context) {
        MeetingResponse meetingResponse = vectorSearchComponent.vectorSearch(String.valueOf(filter.getField()));
        context.storeStepResult(step.getStep(), meetingResponse);
    }


    private void applyFilter(Filter filter, QueryWrapper<Meeting> wrapper, QueryContext context) {
        // 此时value有可能是前置项，也有可能是string值
        Object value = resolveValue(filter.getValue(), context);
        if (value instanceof List) {
            MeetingResponse meetingResponse = (MeetingResponse)((List<?>) value).get(0);
            value = meetingResponse.getId();
        }
        // 如果字段转换不存在为空，那么表示 field 字段违法，直接掠过当前轮次
        String convertFieldName = convertFieldName(filter.getField());
        if (!"".equals(convertFieldName))
            return;
        switch (filter.getOperator().toUpperCase()) {
            case "LIKE":
                wrapper.like(convertFieldName, value);
                break;
            case "BETWEEN":
                Object[] values = (Object[]) value;
                wrapper.between(convertFieldName, values[0], values[1]);
                break;
            default:
                wrapper.eq(convertFieldName, value);
        }
    }

    private Object resolveValue(Object rawValue, QueryContext context) {
        if (rawValue instanceof String) {
            String strValue = (String) rawValue;
            if (strValue.startsWith("step")) {
                // 如果step开头，那么就需要查前置依赖
                return context.getDependencyResult(
                    Integer.parseInt(strValue.split("\\.")[0].substring(4)),
                    Object.class
                );
            }
        }
        return rawValue;
    }


    private String convertFieldName(String logicalName) {
        // 字段映射逻辑
        Map<String, String> fieldMapping = Map.of(
            "beginTime", "begin_time",
            "geoId", "location",
            "type","type",
            "meetingType", "meeting_type",
            "views", "views",
            "meetingId", "id"
        );
        return fieldMapping.getOrDefault(logicalName, "");
    }
}
