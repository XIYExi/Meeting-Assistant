package com.ruoyi.rag.handler;

import com.ruoyi.rag.config.ToolDispatchEnum;
import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.StepSplitParamsEntity;
import com.ruoyi.rag.model.CustomPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 总结模块，首先需要注意，一般情况下，summary模块不会单独出现
 * summary算是action的子分支，summary一定会依赖某个前置输出
 */
@Component
public class ToolSummaryHandler implements ToolSimpleHandler {



    @Override
    public String handler(String content) {
        return "";
    }

    @Override
    public boolean handler(StepSplitParamsEntity params, int step, Map<Integer, Map<String, Object>> output, String uid) {
        // 1. 先解析参数
        Integer dependency = params.getDependency();

        // 2. 直接获取前置输出，summary一定有前置输出，没有就直接返回结束
        if (dependency != -1) {
            Map<String, Object> dependencyOutput = output.get(dependency - 1);
            String dependencyIntent = (String) dependencyOutput.get("intent");

            switch (dependencyIntent) {
                case "chat":
                    // chat 直接把问题再丢给模型一次
                    output.get(step).put("status", true);
                    output.get(step).put("prompt", "");
                    output.get(step).put("queryResult", "");
                    output.get(step).put("routePath", "");
                    output.get(step).put("intent", "summary");
                    break;
                case "query":
                    // query 都遵信以下结构
                    Map<String, Object> dependencyQueryResult = (Map<String, Object>) dependencyOutput.get("queryResult");
                    String dependencyDB = (String)dependencyQueryResult.get("type");
                    StringBuffer querySb = new StringBuffer();
                    switch (dependencyDB) {
                        case "meeting":
                            querySb.append(dependencyQueryResult.get("content"));
                            break;
                        case "meeting_agenda":
                            querySb.append(dependencyQueryResult.get("agenda"));
                            break;
                        case "meeting_geo":
                            querySb.append(dependencyQueryResult.get("moreLocation")).append(" ").append(dependencyQueryResult.get("address"));
                            break;
                        default: break;
                    }

                    String queryFormatPrompt = "\n" + String.format(CustomPrompt.SUMMARY_SUCCESS_PROMPT, querySb.toString()) + "\n";

                    output.get(step).put("status", true);
                    output.get(step).put("prompt", queryFormatPrompt);
                    output.get(step).put("queryResult", "");
                    output.get(step).put("routePath", "");
                    output.get(step).put("intent", "summary");
                    break;
                default: break;
            }
            return true;
        }
        else {
            output.get(step).put("status", false);
            output.get(step).put("prompt", CustomPrompt.SUMMARY_FAILURE_PROMPT);
            output.get(step).put("queryResult", "");
            output.get(step).put("routePath", "");
            output.get(step).put("intent", "summary");
            return false;
        }
    }
}
