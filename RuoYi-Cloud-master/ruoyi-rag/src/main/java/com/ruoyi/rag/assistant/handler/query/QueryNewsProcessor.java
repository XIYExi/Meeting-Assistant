package com.ruoyi.rag.assistant.handler.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.domain.News;
import com.ruoyi.rag.assistant.entity.Filter;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.mapper.NewsMapper;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.assistant.utils.RequestContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QueryNewsProcessor implements QueryProcessor {

    @Resource
    private NewsMapper newsMapper;

    @Override
    public void processor(StepDefinition step, QueryContext context) {
        String db = step.getDb();
        List<Filter> filters = step.getFilters();
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

        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        // 处理依赖和数据绑定
        handleDependencyResult(step, context, queryWrapper);

        for (Filter filter : filters) {
            String field = filter.getField();
            String operator = filter.getOperator();
            String value = String.valueOf(filter.getValue());
            if (value != null && value.contains("step")) {
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

        News news = newsMapper.selectList(queryWrapper).get(0);
        // 不是最后一步，那么就按output_fields输出
        if (step.getStep() != totalSteps) {
            // 不然按照output_fields输出
            if (!outputFields.isEmpty()) {
                Map<String, Object> resultItem = new HashMap<>();
                for (String field : outputFields) {
                    switch (field) {
                        case "news_id":
                            resultItem.put("news_id", news.getId());
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
            context.storeStepResult(step.getStep(), news);
        }
    }

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
                            String dependencyField = parts[1];
                            Object value = dependencyResult.get(dependencyField);
                            if (value != null) {
                                queryWrapper.eq(entry.getKey(), value);
                            }
                        }
                    }
                }
            }
        }
    }

    private static final Map<String, String> fieldMapping = Map.of(
            "beginTime", "create_time",
            "author", "author",
            "title", "title"
        );
    private static String convertFieldName(String logicalName) {
        // 字段映射逻辑
        return fieldMapping.getOrDefault(logicalName, "");
    }


}
