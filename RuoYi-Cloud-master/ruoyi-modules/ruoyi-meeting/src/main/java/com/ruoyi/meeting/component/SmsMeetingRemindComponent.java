package com.ruoyi.meeting.component;

import com.ruoyi.meeting.domain.Meeting;
import com.ruoyi.meeting.domain.MeetingSchedule;
import com.ruoyi.meeting.mapper.MeetingMapper;
import com.ruoyi.meeting.mapper.MeetingScheduleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("smsMeetingRemindComponent")
public class SmsMeetingRemindComponent {

    @Value("${aliyun.market.appcode}")
    private String appCode;
    @Value("${aliyun.market.smsSignId}")
    private String smsSignId;
    @Value("${aliyun.market.templateId}")
    private String templateId;

    @Autowired
    private MeetingMapper meetingMapper;
    @Autowired
    private MeetingScheduleMapper meetingScheduleMapper;
    @Autowired
    private RestTemplate restTemplate;

    private static final String postForSmsUrl = "https://gyytz.market.alicloudapi.com/sms/smsSend";
    private static String smsSlot = "**title**:%s,**begintime**:%s";

    @Scheduled(cron = "0 0 20 * * ?") // 每天晚上8点执行
    // @Scheduled(cron = "0/30 * * * * ?") // 每10秒执行一次，进行测试
    public void sendSmsBeforeMeeting() {
        List<Meeting> meetings = meetingMapper.selectTomorrowMeetingsList();
        if (meetings.isEmpty()) {
            return;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        meetings.forEach(meeting -> {
           // 遍历所有明天的会议，给所有报名的用户发sms
            MeetingSchedule meetingScheduleQuery = new MeetingSchedule();
            meetingScheduleQuery.setMeetingId(meeting.getId());
            List<MeetingSchedule> usersSignUpMeeting = meetingScheduleMapper.selectMeetingScheduleList(meetingScheduleQuery);
            if (!usersSignUpMeeting.isEmpty()) {
                List<String> phones = usersSignUpMeeting.stream().map(elem -> elem.getPhone()).collect(Collectors.toList());
                // 发送sms 或 app推送

                // 构造请求头
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "APPCODE " + appCode);
                if (!phones.isEmpty()) {
                    phones.forEach(phone -> {
                        // 构造请求参数
                        Map<String, String> querys = new HashMap<>();
                        querys.put("mobile", phone); // 替换为实际的手机号
                        querys.put("templateId", templateId); // 替换为实际的模板ID
                        querys.put("smsSignId", smsSignId); // 替换为实际的模板变量值
                        querys.put("param", String.format(smsSlot, meeting.getTitle(), df.format(meeting.getBeginTime())));
                        // 构造HttpEntity
                        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(headers);

                        restTemplate.exchange(postForSmsUrl + "?" + toQueryString(querys), HttpMethod.POST, requestEntity, String.class);
                    });
                }
                String logger = String.format("【 %s 】 -- 会议邮件发送成功", meeting.getTitle());
                log.info(logger);
            }
        });
        String finalMsg = String.format("%s 会议提醒短信发送完成", df.format(new Date()));
        log.info(finalMsg);
    }



    // 将Map转换为查询字符串
    private static String toQueryString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

}
