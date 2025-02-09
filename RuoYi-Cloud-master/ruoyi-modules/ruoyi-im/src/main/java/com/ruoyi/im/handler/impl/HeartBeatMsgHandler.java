package com.ruoyi.im.handler.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.im.common.ImContextUtils;
import com.ruoyi.im.common.ImCoreServerCacheKeyBuilder;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.ImConstants;
import com.ruoyi.im.constant.ImMsgCodeEnum;
import com.ruoyi.im.entity.ImMsgBody;
import com.ruoyi.im.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class HeartBeatMsgHandler implements SimpleHandler {
    private static final Logger logger = LoggerFactory.getLogger(HeartBeatMsgHandler.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ImCoreServerCacheKeyBuilder cacheKeyBuilder;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        logger.info("进入心跳检测处理函数... {}", imMsg);
        // 心跳包基本校验
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null || appId == null) {
            ctx.close();
            logger.error("attr error, imMsg is {}", imMsg);
            throw new IllegalArgumentException("attr is error");
        }
        // 心跳包记录记录 redis存储心跳数据
        // zset集合存储 key(userId) - score(心跳时间)
        String redisKey = cacheKeyBuilder.buildImLoginTokenKey(userId, appId);
        this.recordOnlineTime(userId, redisKey);
        this.removeExpireRecord(redisKey);
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);

        ImMsgBody imMsgBody = new ImMsgBody();
        imMsgBody.setUserId(userId);
        imMsgBody.setAppId(appId);
        imMsgBody.setData("true");
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), JSON.toJSONString(imMsgBody));
        logger.info("[HeartBeatImMsgHandler] imMsg is {}", imMsg);
        ctx.writeAndFlush(respMsg);
    }

    /**
     *  清理掉过期不在线的用户留下额心跳记录
     *
     * @param redisKey
     */
    private void removeExpireRecord(String redisKey) {
        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, System.currentTimeMillis() - ImConstants.DEFAULT_HEART_BEAT_GAP * 1000 * 2);
    }

    /**
     * 记录用户最近一次心跳时间到zSet
     *
     * @param userId
     * @param redisKey
     */
    private void recordOnlineTime(Long userId, String redisKey) {
        redisTemplate.opsForZSet().add(redisKey, userId, System.currentTimeMillis());
    }
}
