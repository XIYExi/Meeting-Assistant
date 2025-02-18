package com.ruoyi.live.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.live.service.LiveRoomService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
