package com.ruoyi.rag.assistant.handler.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.domain.MeetingClip;
import com.ruoyi.rag.assistant.entity.Filter;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.mapper.MeetingClipMapper;
import com.ruoyi.rag.assistant.mapper.MeetingMapper;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.assistant.utils.RequestContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 获取推荐会议列表
 */
@Component
public class QueryFileProcessor implements QueryProcessor {
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private MeetingClipMapper meetingClipMapper;

    @Override
    public void processor(StepDefinition step, QueryContext context) {
        String db = step.getDb();
        int dependency = step.getDependency();
        List<Filter> filters = step.getFilters();
        Map<String, String> dataBindings = step.getDataBindings();
        int totalSteps = RequestContextHolder.getRequestList().size();

        List<String> outputFields = new ArrayList<>();
        // 当前有输出，并且不是最后一项
        if (step.getOutputFields() != null && !step.getOutputFields().isEmpty() && step.getStep() < totalSteps) {
            List<String> outputFieldsArray = step.getOutputFields();
            for (int i = 0; i < outputFieldsArray.size(); i++) {
                outputFields.add(outputFieldsArray.get(i));
            }
        }


        if ("meeting_clip".equals(db)) {
            QueryWrapper<MeetingClip> queryWrapper = new QueryWrapper<>();
            handleDependencyResult(step, context, queryWrapper);
            for (Filter filter : filters) {
                String field = filter.getField();
                String operator = filter.getOperator();
                String value = String.valueOf(filter.getValue());
                // 如果包含step表示依赖了前置项，但是前置依赖已经在上面处理过了，这里直接跳过
                if (value != null && value.contains("step")) {
                    continue;
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
                }

            }

            List<MeetingClip> meetingClips = meetingClipMapper.selectList(queryWrapper);
            if (meetingClips.isEmpty()) {
                // TODO 为空校验
                return;
            }
            MeetingClip meetingClip = meetingClips.get(0);
            if (step.getStep() != totalSteps) {
                Map<String, Object> resultItem = new HashMap<>();
                for (String field : outputFields) {
                    switch (field) {
                        case "file_id":
                            resultItem.put("file_id", meetingClip.getId());
                            break;
                        case "meeting_id":
                            resultItem.put("meeting_id", meetingClip.getMeetingId());
                            break;
                        // 添加其他需要的字段
                        default:
                            break;
                    }
                }
                context.storeStepResult(step.getStep(), resultItem);
            } else {
                context.storeStepResult(step.getStep(), meetingClip);
            }

        }

    }

    public static <T> void handleDependencyResult(StepDefinition step, QueryContext context, QueryWrapper<T> queryWrapper) {
        int dependency = step.getDependency();
        Map<String, String> dataBindings = step.getDataBindings();
        if (dependency > 0) {
            // 获得前置依赖的output结果
            Map dependencyResult = context.getDependencyResult(dependency, Map.class);
            if (dependencyResult != null) {
                for (Map.Entry<String, String> entry : dataBindings.entrySet()) {
                    String bindValue = entry.getValue();
                    if (bindValue.startsWith("step")) {
                        String[] parts = bindValue.split("\\.");
                        if (parts.length == 2) {
                            String dependencyStep = parts[0];
                            String dependencyField = parts[1];
                            Object value = dependencyResult.get(dependencyField);
                            if (value != null) {
                                List<StepDefinition> requestList = RequestContextHolder.getRequestList();
                                StepDefinition dStep = requestList.get(Integer.valueOf(dependencyStep.split("step")[1]) - 1);
                                String dependencyDb = dStep.getDb();
                                // meeting_clip依赖的只能是meeting，所以传递的一定是meeting_id
                                if ("meeting".equals(dependencyDb)) {
                                    queryWrapper.eq("meeting_id", value);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static final Map<String, String> fieldMapping = Map.of(
            "fileName", "file_name",
            "fileType", "file_type",
            "meeting_id", "meeting_id"
    );

    private static String convertFieldName(String logicalName) {
        // 字段映射逻辑
        return fieldMapping.getOrDefault(logicalName, "");
    }
}
