package com.ruoyi.rec.controller;


import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.meeting.api.RemoteScheduleService;
import com.ruoyi.rag.api.RemoteRagService;
import com.ruoyi.rec.domain.ContentMeetingRec;
import com.ruoyi.rec.domain.MeetingResponse;
import com.ruoyi.rec.schedule.LLMRecComponent;
import com.ruoyi.rec.service.RecService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private RemoteScheduleService remoteScheduleService;
    @Resource
    private RemoteRagService remoteRagService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private LLMRecComponent llmRecComponent;
    @Resource
    private RecService recService;


    /**
     * 基于内容推荐
     * @param meetingId
     * @return
     */
    @GetMapping("/content_rec_list")
    public AjaxResult contentRecList(@RequestParam("meetingId") Long meetingId) {
        List<MeetingResponse> contentMeetingRecs = recService.selectContentRecMeetingList(meetingId);
        return AjaxResult.success(contentMeetingRecs);
    }

    /**
     * 基于LLM统计推荐
     * @return
     */
    @GetMapping("/llm_view_static_rec_list")
    public AjaxResult llmViewStaticRecList() {
        List<MeetingResponse> meetingResponses = recService.selectStaticLLMRecMeetingList();
        return AjaxResult.success(meetingResponses);
    }


    /**
     * 基于内容相似度推荐
     * @param userId
     * @return
     */
    @GetMapping("/stream_real_time_rec_list")
    public AjaxResult streamRealTimeRecList(@RequestParam("userId") Long userId) {
        List<MeetingResponse> meetingResponses = recService.selectStreamRecMeetingList(userId);
        return AjaxResult.success(meetingResponses);
    }


    @GetMapping("/getRecMeeting")
    public AjaxResult getRecMeeting() {
        List<Map<String, Object>> vv = remoteScheduleService.getListForRec();
        redisTemplate.opsForValue().set("dev-rec-meeting", vv, 2, TimeUnit.DAYS);

        Object devList = redisTemplate.opsForValue().get("dev-rec-meeting");
        List<Map> listForRec = JSONArray.parseArray(JSONArray.toJSONString(devList), Map.class);
        List<Map<String, Object>> collect = listForRec.stream().map(element -> {
            Map<String, Object> map = new HashMap<>();

            String question = "现在你需要为当前会议打分，得分请从新颖程度、创意性、议题深度等多方面考量，给出一个你认为最合适的分数，得分控制在0~100分之间，只需要输出得分数字即可，不需要任何标注，也不需要输出判分理由，判分尺度可以大一点，分数尽量不要一样。";
            String prompt = "当前会议信息【"+ element.get("title") +"】，当前会议会议主题【" + element.get("type") + "】，当前会议开展类型【"+ element.get("meetingType") +"】";
            AjaxResult chat = remoteRagService.chat(question, prompt);
            Object o = chat.get("msg");
            System.err.println(o);
            map.put("id", element.get("id"));
            map.put("score", o);
            return map;
        }).toList();

        return AjaxResult.success(collect);
    }


    @GetMapping("/calRate")
    public AjaxResult calRate() {
        llmRecComponent.synchronizeMeetingWeights();
        return AjaxResult.success();
    }

}
