package com.ruoyi.rec.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.rec.domain.*;
import com.ruoyi.rec.mapper.MeetingGeoMapper;
import com.ruoyi.rec.mapper.MeetingMapper;
import com.ruoyi.rec.mapper.MeetingRateMapper;
import com.ruoyi.rec.service.RecService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecServiceImpl implements RecService {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private MeetingGeoMapper meetingGeoMapper;
    @Resource
    private MeetingRateMapper meetingRateMapper;


    /**
     * 基于内容推荐，通过meetingId去mongodb里面查询4条信息，如果查不到就取static llm的四条结果
     * 基于内容是直接扫sql库，每一条会议都会有匹配数据，不可能为空
     *
     * 这个是在会议详细页面最底下，基于会议相似度进行的基于内容推荐
     * @param meetingId
     * @return
     */
    @Override
    public List<MeetingResponse> selectContentRecMeetingList(Long meetingId) {
        // 匹配条件：根据 meetingId 查找文档
        MatchOperation matchOperation = Aggregation.match(Criteria.where("meetingId").is(meetingId));
        // 投影操作：只保留前 4 条 recs 数据
        ProjectionOperation projectionOperation = Aggregation.project()
                .and("meetingId").as("meetingId")
                .and("recs").slice(4).as("recs");
        // 构建聚合查询
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, projectionOperation);
        List<ContentMeetingRec> result = mongoTemplate.aggregate(aggregation, ContentMeetingRec.class, ContentMeetingRec.class).getMappedResults();
        List<MeetingResponse> collect = result.get(0).getRecs().stream().map(recommendation -> {
            Integer recMeetingId = recommendation.getMeetingId();
            Meeting meeting = meetingMapper.selectById(recMeetingId);

            MeetingResponse meetingGeo = new MeetingResponse();
            BeanUtils.copyProperties(meeting, meetingGeo);
            Long locationId = Long.parseLong(meeting.getLocation());
            MeetingGeo meetingGeo1 = meetingGeoMapper.selectById(locationId);
            meetingGeo.setLocation(meetingGeo1);
            return meetingGeo;
        }).collect(Collectors.toList());
        return collect;
    }


    /**
     * 基于LLM和views的统计推荐，只返回四条最热门最有价值的推荐数据
     * 这个是用来应付冷启动的，如果用户没有任何打分记录，那么实时查询就为空，那么就走最最热门统计推荐
     * @param
     * @return
     */
    @Override
    public List<MeetingResponse> selectStaticLLMRecMeetingList() {
        QueryWrapper<MeetingRate> meetingRateQueryWrapper = new QueryWrapper<>();
        meetingRateQueryWrapper.last("order by rate desc limit 4");
        List<MeetingRate> meetingRates = meetingRateMapper.selectList(meetingRateQueryWrapper);
        List<MeetingResponse> collect = meetingRates.stream().map(rating -> {
            Long meetingId = rating.getMeetingId();
            Meeting meeting = meetingMapper.selectById(meetingId);

            MeetingResponse meetingGeo = new MeetingResponse();
            BeanUtils.copyProperties(meeting, meetingGeo);
            Long locationId = Long.parseLong(meeting.getLocation());
            MeetingGeo meetingGeo1 = meetingGeoMapper.selectById(locationId);
            meetingGeo.setLocation(meetingGeo1);
            return meetingGeo;
        }).collect(Collectors.toList());
        return collect;
    }


    /**
     * 这是用在首页的，根据用户兴趣和打分，做实时推荐
     * 根据用户浏览数据实时推荐
     * @param userId
     * @return
     */
    @Override
    public List<MeetingResponse> selectStreamRecMeetingList(Long userId) {
        // 匹配条件：根据 meetingId 查找文档
        MatchOperation matchOperation = Aggregation.match(Criteria.where("userId").is(userId));
        // 投影操作：只保留前 4 条 recs 数据
        ProjectionOperation projectionOperation = Aggregation.project()
                .and("userId").as("userId")
                .and("recs").slice(4).as("recs");
        // 构建聚合查询
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, projectionOperation);
        List<StreamMeetingRec> result = mongoTemplate.aggregate(aggregation, StreamMeetingRec.class, StreamMeetingRec.class).getMappedResults();
        // 如果没有数据就走llm统计推荐
        if (result.isEmpty()) {
            return this.selectStaticLLMRecMeetingList();
        }

        // 有数据就封装数据返回
        List<MeetingResponse> collect = result.get(0).getRecs().stream().map(recommendation -> {
            Integer recMeetingId = recommendation.getMeetingId();
            Meeting meeting = meetingMapper.selectById(recMeetingId);
            MeetingResponse meetingGeo = new MeetingResponse();
            BeanUtils.copyProperties(meeting, meetingGeo);
            Long locationId = Long.parseLong(meeting.getLocation());
            MeetingGeo meetingGeo1 = meetingGeoMapper.selectById(locationId);
            meetingGeo.setLocation(meetingGeo1);
            return meetingGeo;
        }).collect(Collectors.toList());
        return collect;
    }
}
