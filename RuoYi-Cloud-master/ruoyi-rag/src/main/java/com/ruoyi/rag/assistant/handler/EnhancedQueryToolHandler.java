package com.ruoyi.rag.assistant.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.rag.assistant.constant.QueryToolDispatchEnum;
import com.ruoyi.rag.assistant.declare.EnhancedToolHandler;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.entity.Filter;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.handler.query.*;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.assistant.utils.ToolJudgmentFunc;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 查询网络安全大会信息
 * [
 *     {
 *         "step": 1,
 *         "intent": "query",
 *         "subtype": "meeting",
 *         "db": "meeting",
 *         "dependency": -1,
 *         "data_bindings": {},
 *         "filters": [
 *             {
 *                 "field": "title",
 *                 "operator": "LIKE",
 *                 "value": "%网络安全大会%"
 *             }
 *         ],
 *         "auth_condition": {
 *             "verify_mode": "password"
 *         },
 *         "output_fields": [
 *             "meeting_id"
 *         ]
 *     }
 * ]
 */
@Component
public class EnhancedQueryToolHandler implements EnhancedToolHandler, InitializingBean {

    private static final Map<String, QueryProcessor> queryToolMap = new ConcurrentHashMap<>();

    @Resource
    ApplicationContext applicationContext;

    @Override
    public boolean handler(StepDefinition step, QueryContext context) {
        QueryProcessor queryProcessor = queryToolMap.get(step.getSubtype());
        queryProcessor.processor(step, context);
        return false;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        queryToolMap.put(QueryToolDispatchEnum.QUERY_MEETING.getMessage(), applicationContext.getBean(QueryMeetingProcessor.class));
        queryToolMap.put(QueryToolDispatchEnum.QUERY_REC.getMessage(), applicationContext.getBean(QueryRecProcessor.class));
        queryToolMap.put(QueryToolDispatchEnum.QUERY_RANk.getMessage(), applicationContext.getBean(QueryRankProcessor.class));
        queryToolMap.put(QueryToolDispatchEnum.QUERY_NEWS.getMessage(), applicationContext.getBean(QueryNewsProcessor.class));
        queryToolMap.put(QueryToolDispatchEnum.QUERY_FILE.getMessage(), applicationContext.getBean(QueryFileProcessor.class));
    }
}
