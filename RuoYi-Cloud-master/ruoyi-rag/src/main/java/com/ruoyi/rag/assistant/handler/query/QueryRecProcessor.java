package com.ruoyi.rag.assistant.handler.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.domain.MeetingAgenda;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.mapper.MeetingAgendaMapper;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rec.api.RemoteRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 获取推荐会议列表
 */
@Component
public class QueryRecProcessor implements QueryProcessor {
    @Resource
    private RemoteRecService remoteRecService;
    @Autowired
    private MeetingAgendaMapper meetingAgendaMapper;

    @Override
    public void processor(StepDefinition step, QueryContext context) {
        // 这里是早期设计失误了，rec用来推荐的数据格式和这里的不一样了
        // 之前推荐逻辑是把 meeting 和 meeting_geo合起来
        // 这里 rec 的对话逻辑是把 meeting 和 meeting_agenda 和起来
        List<com.ruoyi.rec.api.domain.MeetingResponse> meetingResponses = remoteRecService.recForAgent(1L);
        List<com.ruoyi.rag.assistant.entity.MeetingResponse> result = meetingResponses.stream().map(meeting -> {
            com.ruoyi.rag.assistant.entity.MeetingResponse meetingResponse = new com.ruoyi.rag.assistant.entity.MeetingResponse();
            BeanUtils.copyProperties(meeting, meetingResponse);
            List<MeetingAgenda> agenda = meetingAgendaMapper.selectList(new QueryWrapper<MeetingAgenda>().eq("meeting_id", meeting.getId()));
            meetingResponse.setAgenda(agenda);
            return meetingResponse;
        }).collect(Collectors.toList());
        // System.err.println(result);
        context.storeStepResult(step.getStep(), result);
    }
}
