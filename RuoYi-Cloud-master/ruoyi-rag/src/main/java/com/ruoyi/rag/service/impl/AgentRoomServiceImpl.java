package com.ruoyi.rag.service.impl;

import com.ruoyi.common.entity.im.ImOfflineDTO;
import com.ruoyi.common.entity.im.ImOnlineDTO;
import com.ruoyi.rag.config.LivingProviderCacheKeyBuilder;
import com.ruoyi.rag.service.AgentRoomService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class AgentRoomServiceImpl implements AgentRoomService {

    @Resource
    private LivingProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void userOnlineHandler(ImOnlineDTO imOnlineDTO) {
        Integer roomId = imOnlineDTO.getRoomId();
        Integer appId = imOnlineDTO.getAppId();
        Long userId = imOnlineDTO.getUserId();
        String cacheKey = cacheKeyBuilder.buildLivingRoomUserSet(roomId, appId);
        redisTemplate.opsForSet().add(cacheKey, userId);
        redisTemplate.expire(cacheKey, 10, TimeUnit.HOURS);
    }

    @Override
    public void userOfflineHandler(ImOfflineDTO imOfflineDTO) {
Long userId = imOfflineDTO.getUserId();
        Integer roomId = imOfflineDTO.getRoomId();
        Integer appId = imOfflineDTO.getAppId();
        String cacheKey = cacheKeyBuilder.buildLivingRoomUserSet(roomId, appId);
        redisTemplate.opsForSet().remove(cacheKey, userId);
    }
}
