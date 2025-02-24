package com.ruoyi.meeting.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.meeting.component.GeoMapComponent;
import com.ruoyi.meeting.entity.GeoStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.MeetingGeoMapper;
import com.ruoyi.meeting.domain.MeetingGeo;
import com.ruoyi.meeting.service.IMeetingGeoService;

import javax.annotation.Resource;

/**
 * 会议地图Service业务层处理
 * 
 * @author xiye
 * @date 2025-02-23
 */
@Service
public class MeetingGeoServiceImpl implements IMeetingGeoService 
{
    private static final Logger logger = LoggerFactory.getLogger(MeetingGeoServiceImpl.class);
    @Autowired
    private MeetingGeoMapper meetingGeoMapper;
    @Resource
    private GeoMapComponent geoMapComponent;


    /**
     * 计算
     * @param origins
     * @param distributions
     * @return
     */
    @Override
    public List calPathPlanning(String origins, String distributions) {
        Object pathPlanning = geoMapComponent.getPathPlanning(origins, distributions);
        Map rawObject = JSON.parseObject(JSONObject.toJSONString(pathPlanning), Map.class);
        if(Integer.parseInt(String.valueOf(rawObject.get("code"))) != 200) {
            logger.error("[MeetingGeoServiceImpl:calPathPlanning] 远程调用api计算开车路径规划错误");
            return new ArrayList();
        }
        Map wrapperObject = JSON.parseObject(JSONObject.toJSONString(rawObject.get("data")), Map.class);
        Map strategyObject = JSON.parseObject(JSONObject.toJSONString(wrapperObject.get("strategyList")), Map.class);
        JSONArray paths = JSONArray.parseArray(JSONArray.toJSONString(strategyObject.get("paths")));
        Map distanceWrapper = JSON.parseObject(JSONObject.toJSONString(paths.get(0)), Map.class);

        JSONArray steps = JSONArray.parseArray(JSONArray.toJSONString(distanceWrapper.get("steps")));
        List<GeoStep> geoSteps = steps.toJavaList(GeoStep.class);
        List<Map> results = new ArrayList<>();
        List<Map> list = new ArrayList<>();

        Map<String, Object> element = new HashMap<>();
        element.put("color", "#000");
        element.put("dottedLine", "true");
        element.put("level", "aboveroads");
        element.put("width", 5);

        geoSteps.forEach(step -> {
            String[] split = step.getPolyline().split(";");
            for(int i=0;i<split.length;++i) {
                Map<String, Double> point = new HashMap<>();
                point.put("latitude", Double.parseDouble(split[i].split(",")[1]));
                point.put("longitude", Double.parseDouble(split[i].split(",")[0]));
                list.add(point);
            }
        });
        element.put("points", list);
        results.add(element);

        return results;
    }

    /**
     * 查询会议地图
     * 
     * @param id 会议地图主键
     * @return 会议地图
     */
    @Override
    public MeetingGeo selectMeetingGeoById(Long id)
    {
        return meetingGeoMapper.selectMeetingGeoById(id);
    }

    
    /**
     * 查询会议地图列表
     * 
     * @param meetingGeo 会议地图
     * @return 会议地图
     */
    @Override
    public List<MeetingGeo> selectMeetingGeoList(MeetingGeo meetingGeo)
    {
        return meetingGeoMapper.selectMeetingGeoList(meetingGeo);
    }

    /**
     * 新增会议地图
     * 
     * @param meetingGeo 会议地图
     * @return 结果
     */
    @Override
    public int insertMeetingGeo(MeetingGeo meetingGeo)
    {
        meetingGeo.setCreateTime(DateUtils.getNowDate());
        return meetingGeoMapper.insertMeetingGeo(meetingGeo);
    }

    /**
     * 修改会议地图
     * 
     * @param meetingGeo 会议地图
     * @return 结果
     */
    @Override
    public int updateMeetingGeo(MeetingGeo meetingGeo)
    {
        meetingGeo.setUpdateTime(DateUtils.getNowDate());
        return meetingGeoMapper.updateMeetingGeo(meetingGeo);
    }

    /**
     * 批量删除会议地图
     * 
     * @param ids 需要删除的会议地图主键
     * @return 结果
     */
    @Override
    public int deleteMeetingGeoByIds(Long[] ids)
    {
        return meetingGeoMapper.deleteMeetingGeoByIds(ids);
    }

    /**
     * 删除会议地图信息
     * 
     * @param id 会议地图主键
     * @return 结果
     */
    @Override
    public int deleteMeetingGeoById(Long id)
    {
        return meetingGeoMapper.deleteMeetingGeoById(id);
    }
}
