package com.ruoyi.live.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.live.domain.LiveRoomRecord;
import com.ruoyi.live.mapper.LiveRoomRecordMapper;
import com.ruoyi.live.service.LiveRoomRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LiveRoomRecordServiceImpl extends ServiceImpl<LiveRoomRecordMapper, LiveRoomRecord> implements LiveRoomRecordService {

    @Resource
    private LiveRoomRecordMapper liveRoomRecordMapper;

}
