package com.ruoyi.rag.assistant.handler.query;

import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rec.api.RemoteRecService;
import com.ruoyi.rec.api.domain.MeetingResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * 获取推荐会议列表
 */
@Component
public class QueryRecProcessor implements QueryProcessor {
    @Resource
    private RemoteRecService remoteRecService;

    @Override
    public void processor(StepDefinition step, QueryContext context) {
        List<MeetingResponse> meetingResponses = remoteRecService.recForAgent(1L);
        
    }
}
