package com.ruoyi.rec.controller;


import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.meeting.api.RemoteScheduleService;
import com.ruoyi.rag.api.RemoteRagService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private RemoteScheduleService remoteScheduleService;
    @Resource
    private RemoteRagService remoteRagService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/getRecMeeting")
    public AjaxResult getRecMeeting() {
        //List<Map<String, Object>> listForRec = remoteScheduleService.getListForRec();
        //redisTemplate.opsForValue().set("dev-rec-meeting", listForRec, 2, TimeUnit.DAYS);

        Object devList = redisTemplate.opsForValue().get("dev-rec-meeting");
        List<Map> listForRec = JSONArray.parseArray(JSONArray.toJSONString(devList), Map.class);
        List<Map<String, Object>> collect = listForRec.stream().map(element -> {
            Map<String, Object> map = new HashMap<>();

            String question = "现在你需要为当前会议打分，得分请从新颖程度、创意性、议题深度等多方面考量，给出一个你认为最合适的分数，得分控制在0~100分之间，只需要输入得分数字即可。";
            String prompt = "当前会议信息【"+ element.get("title") +"】，当前会议会议主题【" + element.get("type") + "】，当前会议开展类型【"+ element.get("meetingType") +"】";
            AjaxResult chat = remoteRagService.chat(question, prompt);
            Object o = chat.get("data");
            System.err.println(o);
            map.put("id", element.get("id"));
            map.put("score", o);
            return map;
        }).toList();

        return AjaxResult.success();
    }

}
