package com.ruoyi.im.service;

import com.ruoyi.im.constant.ImCoreServerConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ImOnlineServiceImpl implements ImOnlineService{

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isOnline(long userId, int appId) {
        return redisTemplate.hasKey(ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId);
    }
}
