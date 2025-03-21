package com.ruoyi.rag.assistant.handler.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.domain.Meeting;
import com.ruoyi.rag.assistant.domain.MeetingAgenda;
import com.ruoyi.rag.assistant.entity.MeetingResponse;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.mapper.MeetingAgendaMapper;
import com.ruoyi.rag.assistant.mapper.MeetingMapper;
import com.ruoyi.rag.assistant.utils.QueryContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 获取推荐会议列表
 */
@Component
public class QueryRankProcessor implements QueryProcessor {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private MeetingAgendaMapper meetingAgendaMapper;

    @Override
    public void processor(StepDefinition step, QueryContext context) {
       Set<ZSetOperations.TypedTuple<Object>> ranks =  redisTemplate.opsForZSet().reverseRangeWithScores("meeting:view:rank", 0L, 4L);
        List<MeetingResponse> collect = ranks.stream().map(elem -> {
            Meeting meeting = meetingMapper.selectById((Long) elem.getValue());
            MeetingResponse meetingResponse = new MeetingResponse();
            BeanUtils.copyProperties(meeting, meetingResponse);
            List<MeetingAgenda> agenda = meetingAgendaMapper.selectList(new QueryWrapper<MeetingAgenda>().eq("meeting_id", meeting.getId()));
            meetingResponse.setAgenda(agenda);
            return meetingResponse;
        }).collect(Collectors.toList());
        // System.err.println(collect);
        context.storeStepResult(step.getStep(), collect);
    }
}
