package com.ruoyi.live.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.live.config.LivingProviderCacheKeyBuilder;
import com.ruoyi.live.service.LiveRoomService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/living")
public class LiveController {

    @Resource
    private LiveRoomService liveRoomService;

    @PostMapping("/startLiving")
    public AjaxResult startLiving(@RequestParam("type") Integer type) {
        Long roomId = liveRoomService.startingLiving(type);
        return AjaxResult.success(roomId);
    }

    @PostMapping("/closeLiving")
    public AjaxResult closeLiving(@RequestParam("roomId") Long roomId, @RequestParam("sendId")Long sendId) {
        boolean b = liveRoomService.closeLiveing(roomId, sendId);
        return b ? AjaxResult.success() : AjaxResult.error();
    }


    /**
     * 支持通过roomId查询出userId
     */
    @PostMapping("/queryByRoomId")
    public AjaxResult queryByRoomId(@RequestParam("roomId") Long roomId, @RequestParam("appId") Integer appId) {
        return AjaxResult.success(liveRoomService.queryByRoomId(roomId.intValue(), appId));
    }
}
