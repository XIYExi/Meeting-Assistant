package com.ruoyi.meeting.qo;

import com.ruoyi.common.core.constant.ScheduleConstants;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysJobQuery extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long jobId;

    /** 任务名称 */
    private String jobName;

    /** 任务组名 */
    private String jobGroup;

    /** 调用目标字符串 */
    private String invokeTarget;

    /** cron执行表达式 */
    private String cronExpression;

    /** cron计划策略 */
    //@Excel(name = "计划策略 ", readConverterExp = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /** 是否并发执行（0允许 1禁止） */
    //@Excel(name = "并发执行", readConverterExp = "0=允许,1=禁止")
    private String concurrent;

    /** 任务状态（0正常 1暂停） */
    //@Excel(name = "任务状态", readConverterExp = "0=正常,1=暂停")
    private String status;
}
