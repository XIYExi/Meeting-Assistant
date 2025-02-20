package com.ruoyi.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.entity.im.ImOfflineDTO;
import com.ruoyi.common.entity.im.ImOnlineDTO;
import com.ruoyi.live.domain.LiveRoom;

import java.util.List;

public interface LiveRoomService extends IService<LiveRoom> {
    Long startingLiving(Integer type);

    boolean closeLiveing(Long roomId, Long sendId);

    void userOnlineHandler(ImOnlineDTO imOnlineDTO);

    void userOfflineHandler(ImOfflineDTO imOfflineDTO);

    List<Long> queryByRoomId(Integer roomId, Integer appId);
}
