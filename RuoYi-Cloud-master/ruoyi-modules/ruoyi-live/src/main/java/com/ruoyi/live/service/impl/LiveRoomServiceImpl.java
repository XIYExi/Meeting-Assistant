package com.ruoyi.live.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.entity.im.ImOfflineDTO;
import com.ruoyi.common.entity.im.ImOnlineDTO;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.live.config.LivingProviderCacheKeyBuilder;
import com.ruoyi.live.domain.LiveRoom;
import com.ruoyi.live.domain.LiveRoomRecord;
import com.ruoyi.live.enums.CommonStatusEum;
import com.ruoyi.live.mapper.LiveRoomMapper;
import com.ruoyi.live.mapper.LiveRoomRecordMapper;
import com.ruoyi.live.service.LiveRoomService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements LiveRoomService {

    @Resource
    private LiveRoomMapper liveRoomMapper;
    @Resource
    private LiveRoomRecordMapper liveRoomRecordMapper;
    @Resource
    private LivingProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 开播功能
     * @param type
     * @return
     */
    @Override
    public Long startingLiving(Integer type) {
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setAnchorId(SecurityUtils.getUserId());
        liveRoom.setRoomName("会议-" + SecurityUtils.getUserId() + "的直播间");
        liveRoom.setType(type);
        liveRoom.setStatus(CommonStatusEum.VALID_STATUS.getCode());
        liveRoom.setStartTime(new Date());
        liveRoomMapper.insert(liveRoom);
        String cacheKey = cacheKeyBuilder.buildLivingRoomObj(liveRoom.getId());
        //防止之前有空值缓存，这里做移除操作
        redisTemplate.delete(cacheKey);
        return liveRoom.getId();
    }

    @Override
    public boolean closeLiveing(Long roomIdm, Long sendId) {
        Long userId = SecurityUtils.getUserId();
        if (!Objects.equals(userId, sendId)) {
            return false;
        }
        LiveRoomRecord liveRoomRecord = new LiveRoomRecord();
        liveRoomRecord.setEndTime(new Date());
        liveRoomRecord.setStatus(CommonStatusEum.INVALID_STATUS.getCode());
        liveRoomRecordMapper.insert(liveRoomRecord);
        liveRoomMapper.deleteById(roomIdm);
        //移除掉直播间cache
        String cacheKey = cacheKeyBuilder.buildLivingRoomObj(roomIdm);
        redisTemplate.delete(cacheKey);
        return true;
    }

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

    @Override
    public List<Long> queryByRoomId(Integer roomId, Integer appId) {
        String cacheKey = cacheKeyBuilder.buildLivingRoomUserSet(roomId.intValue(), appId);
        Cursor<Object> cursor = redisTemplate.opsForSet().scan(cacheKey, ScanOptions.scanOptions().match("*").count(100).build());
        List<Long> userIdList = new ArrayList<>();
        while (cursor.hasNext()) {
            Long userId = (Long) cursor.next();
            userIdList.add(userId);
        }
        return userIdList;
    }
}
