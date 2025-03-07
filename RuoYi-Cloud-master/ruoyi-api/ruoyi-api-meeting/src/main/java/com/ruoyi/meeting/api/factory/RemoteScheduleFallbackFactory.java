package com.ruoyi.meeting.api.factory;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.meeting.api.RemoteScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RemoteScheduleFallbackFactory implements FallbackFactory<RemoteScheduleService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteScheduleService.class);

    @Override
    public RemoteScheduleService create(Throwable throwable) {
        log.error("会议服务调用失败:{}", throwable.getMessage());
        return new RemoteScheduleService()
        {
            @Override
            public AjaxResult updateMeetingStatus(Long meetingId, int status) {
               return AjaxResult.error("会议状态修改失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult getMessageByAgenda(Long id, Long dbType) {
                return AjaxResult.error("获取Meeting/Agenda数据失败:" + throwable.getMessage());
            }

            @Override
            public List<Map<String, Object>> getListForRec() {
                return new ArrayList<>();
            }
        };
    }
}
