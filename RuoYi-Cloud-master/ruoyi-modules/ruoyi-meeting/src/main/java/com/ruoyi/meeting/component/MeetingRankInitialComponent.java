package com.ruoyi.meeting.component;


import com.ruoyi.meeting.constant.MeetingRedisKeyBuilder;
import com.ruoyi.meeting.domain.Meeting;
import com.ruoyi.meeting.service.IMeetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MeetingRankInitialComponent {
    private static final Logger logger = LoggerFactory.getLogger(MeetingRankInitialComponent.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private IMeetingService meetingService;

    /**
     * 启动的时候对排行榜进行初始化
     */
    @PostConstruct
    public void InitRank() {
        // 先删除原始的，清空，防止污染
        redisTemplate.delete(MeetingRedisKeyBuilder.MEETING_RANK_KEY);

        List<Meeting> meetings = meetingService.selectMeetingList(new Meeting());

        // 创建TypedDouble集合 用来批量添加
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
        meetings.forEach(meeting -> {
            Long id = meeting.getId();
            tuples.add(new DefaultTypedTuple<>(id, meeting.getViews().doubleValue()));
        });
        Long add = redisTemplate.opsForZSet().add(MeetingRedisKeyBuilder.MEETING_RANK_KEY, tuples);
        logger.info("[MeetingRankInitialComponent] 排行榜初始化成功! {}", add);
    }

}
