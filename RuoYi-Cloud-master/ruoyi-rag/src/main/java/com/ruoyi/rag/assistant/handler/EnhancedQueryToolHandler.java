package com.ruoyi.rag.assistant.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.rag.assistant.constant.QueryToolDispatchEnum;
import com.ruoyi.rag.assistant.declare.EnhancedToolHandler;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.entity.Filter;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.handler.query.QueryMeetingProcessor;
import com.ruoyi.rag.assistant.handler.query.QueryRecProcessor;
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

    /**
     * 这一步是dependency为-1的时候，这个时候表示没有依赖，所以需要对filters中进行过滤
     * 比方说如果有依赖关系，filters中会存在stepX.id = xxx这种情况
     * 但是dependency不需要处理这种关系
     * @param step
     * @param context
     * @return
     */
    private boolean processQueryStep(StepDefinition step, QueryContext context) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        boolean skipQueryForMeetingWithTitle = false;
        // 通过filter封装查询条件
        for (Filter filter : step.getFilters()) {
            if (context.containsBinding(filter.getValue().toString())) {
                // 如果filter的value真的为stepX.XX这种格式，表示出现了错误的依赖关系
                // 直接跳过不处理
                continue;
            }
            // 正常处理其他逻辑
            if (ToolJudgmentFunc.isVectorSearchRequired(step, filter)) {
                // 如果查的是meeting，并且根据title查询，那么直接返回结果
                // title优先级最高，只要对于meeting数据出现了title那么其他所有字段可以忽略，通过title一定可以锁定数据

                skipQueryForMeetingWithTitle = true;
                // context.storeStepResult(step.getStep(), meetingResponse);
                break;
            }

        }

        if (skipQueryForMeetingWithTitle) {
            // 表示当前步骤通过title走Milvus查询到数据了，直接返回,不需要进行下列处理和查询
            return true;
        }



        return true;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        queryToolMap.put(QueryToolDispatchEnum.QUERY_MEETING.getMessage(), applicationContext.getBean(QueryMeetingProcessor.class));
        queryToolMap.put(QueryToolDispatchEnum.QUERY_REC.getMessage(), applicationContext.getBean(QueryRecProcessor.class));
    }
}
