package com.ruoyi.rag.assistant.handler.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.domain.Meeting;
import com.ruoyi.rag.assistant.domain.MeetingAgenda;
import com.ruoyi.rag.assistant.entity.MeetingResponse;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.mapper.MeetingAgendaMapper;
import com.ruoyi.rag.assistant.mapper.MeetingClipMapper;
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
public class QueryFileProcessor implements QueryProcessor {
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private MeetingClipMapper meetingClipMapper;

    @Override
    public void processor(StepDefinition step, QueryContext context) {

    }
}
