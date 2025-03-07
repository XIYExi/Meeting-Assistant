package com.ruoyi.rec.schedule;

import com.ruoyi.rag.api.RemoteRagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("lLMRecComponent")
public class LLMRecComponent {
    private static final Logger logger = LoggerFactory.getLogger(LLMRecComponent.class);


    @Resource
    private RemoteRagService remoteRagService;

    /**
     * 本一小时一次，同步会议权重
     */
    @Scheduled(cron = "0 0 20 * * ?")
    public void synchronizeMeetingWeights() {

    }
}
