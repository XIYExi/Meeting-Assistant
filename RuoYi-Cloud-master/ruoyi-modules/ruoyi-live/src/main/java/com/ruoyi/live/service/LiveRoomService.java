package com.ruoyi.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.live.domain.LiveRoom;

public interface LiveRoomService extends IService<LiveRoom> {
    Long startingLiving(Integer type);

    boolean closeLiveing(Long roomId, Long sendId);
}
