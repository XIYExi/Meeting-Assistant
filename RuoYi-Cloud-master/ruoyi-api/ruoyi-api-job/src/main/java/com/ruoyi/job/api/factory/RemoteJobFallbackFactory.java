package com.ruoyi.job.api.factory;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.job.api.RemoteSysJobService;
import com.ruoyi.job.api.domain.SysJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RemoteJobFallbackFactory  implements FallbackFactory<RemoteSysJobService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteSysJobService.class);

    @Override
    public RemoteSysJobService create(Throwable throwable) {
        log.error("定时任务服务调用失败:{}", throwable.getMessage());
        return new RemoteSysJobService() {
            @Override
            public AjaxResult add(SysJob job) throws Exception {
                return AjaxResult.error("定时任务插入失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult removeByMeetingId(Long meetingId) throws Exception {
                return AjaxResult.error("定时任务删除失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult editByMeetingId(Long meetingId, String time, String type) throws Exception {
                return AjaxResult.error("定时任务更新失败:" + throwable.getMessage());
            }
        };
    }
}
