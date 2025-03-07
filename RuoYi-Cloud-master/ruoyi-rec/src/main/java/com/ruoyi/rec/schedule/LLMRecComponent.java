package com.ruoyi.rec.schedule;

import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.meeting.api.RemoteScheduleService;
import com.ruoyi.rag.api.RemoteRagService;
import com.ruoyi.rec.config.Prompt;
import com.ruoyi.rec.domain.MeetingRate;
import com.ruoyi.rec.mapper.MeetingRateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component("lLMRecComponent")
public class LLMRecComponent {
    private static final Logger logger = LoggerFactory.getLogger(LLMRecComponent.class);

    @Resource
    private RemoteRagService remoteRagService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private RemoteScheduleService remoteScheduleService;
    @Resource
    private MeetingRateMapper meetingRateMapper;


    private double weightViews = 0.6;
    private double weightScore = 0.4;
    /**
     * 本一小时一次，同步会议权重
     */
    //@Scheduled(cron = "0 0 20 * * ?")
    public void synchronizeMeetingWeights() {
        meetingRateMapper.clearMeetingRate();

        // List<Map<String, Object>> devList = remoteScheduleService.getListForRec();
        Object devList = redisTemplate.opsForValue().get("dev-rec-meeting");
        List<Map> listForRec = JSONArray.parseArray(JSONArray.toJSONString(devList), Map.class);

        List<Map<String, Object>> collect = listForRec.stream().map(element -> {
            Map<String, Object> map = new HashMap<>();
            String question = Prompt.ScorePrompt;
            String prompt = "当前会议信息【"+ element.get("title") +"】，当前会议会议主题【" + element.get("type") + "】，当前会议开展类型【"+ element.get("meetingType") +"】";
            AjaxResult chat = remoteRagService.chat(question, prompt);

            double score = Double.parseDouble(String.valueOf(chat.get("msg")));
            double views = Double.parseDouble(String.valueOf(element.get("views")));
            map.put("id", element.get("id"));
            map.put("views", views);
            map.put("score", score);
            return map;
        }).toList();

        List<Double> doubles = processList(collect, weightViews, weightScore);

        for (int i = 0; i < collect.size(); i++) {
            collect.get(i).put("rate", doubles.get(i));
            MeetingRate meetingRate = new MeetingRate();
            meetingRate.setMeetingId(Long.parseLong(String.valueOf(collect.get(i).get("id"))));
            //meetingRate.setViews(Integer.parseInt(String.valueOf(collect.get(i).get("views"))));
            //meetingRate.setScore(Integer.parseInt(String.valueOf(collect.get(i).get("score"))));
            meetingRate.setRate(new BigDecimal(String.valueOf(collect.get(i).get("rate"))));
            meetingRateMapper.insert(meetingRate);
        }

        redisTemplate.opsForValue().set("dev-test-rate", collect, 1, TimeUnit.DAYS);
    }


    private static List<Double> processList(List<Map<String, Object>> dataList, double weightViews, double weightScore) {
        // 1. 归一化 views
        double minViews = Double.MAX_VALUE;
        double maxViews = Double.MIN_VALUE;
        for (Map<String, Object> map : dataList) {
            double views = Double.parseDouble(String.valueOf(map.get("views")));
            if (views < minViews) minViews = views;
            if (views > maxViews) maxViews = views;
        }

        // 2. 归一化 score
        double minScore = 0; // score 范围已知为 [0, 100]
        double maxScore = 100;

        // 3. 计算加权和
        List<Double> weightedSums = new ArrayList<>();
        for (Map<String, Object> map : dataList) {
            double views = Double.parseDouble(String.valueOf(map.get("views")));
            double score = Double.parseDouble(String.valueOf(map.get("score")));

            // 归一化
            double normalizedViews = (views - minViews) / (maxViews - minViews);
            double normalizedScore = (score - minScore) / (maxScore - minScore);

            // 加权和
            double weightedSum = normalizedViews * weightViews + normalizedScore * weightScore;
            weightedSums.add(weightedSum);
        }

        // 4. 应用 Softmax
        return softmax(weightedSums);
    }

    // Softmax 函数
    private static List<Double> softmax(List<Double> values) {
        double max = Double.NEGATIVE_INFINITY;
        for (double value : values) {
            if (value > max) max = value;
        }

        double sumExp = 0;
        for (int i = 0; i < values.size(); i++) {
            values.set(i, Math.exp(values.get(i) - max));
            sumExp += values.get(i);
        }

        for (int i = 0; i < values.size(); i++) {
            values.set(i, values.get(i) / sumExp);
        }

        return values;
    }
}
