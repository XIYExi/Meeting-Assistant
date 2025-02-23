package com.ruoyi.meeting.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.meeting.component.GeoMapComponent;
import com.ruoyi.meeting.domain.MeetingGeo;
import com.ruoyi.meeting.service.IMeetingGeoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/geo")
public class GeoMapController {

    @Resource
    private GeoMapComponent geoMapComponent;
    @Resource
    private IMeetingGeoService meetingGeoService;

    /**
     * 根据地名去查询经纬度信息
     */
    @GetMapping("/getGeoCode")
    public AjaxResult getGeoCode(@RequestParam("address")String address, @RequestParam("city")String city) {
        Object o = geoMapComponent.geoCodeQuery(address, city);
        return AjaxResult.success(o);
    }


    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("id") Long id) {
        MeetingGeo meetingGeo = meetingGeoService.selectMeetingGeoById(id);
        return AjaxResult.success(meetingGeo);
    }
}
