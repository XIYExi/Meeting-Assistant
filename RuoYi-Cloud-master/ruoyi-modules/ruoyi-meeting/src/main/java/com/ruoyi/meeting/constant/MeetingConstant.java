package com.ruoyi.meeting.constant;

public interface MeetingConstant {

    /**
     * 会议状态
     * */
    public static final Long MEETING_STATUE_NOT_YET_STARTED = 1L;
    public static final Long MEETING_STATUE_IN_PROGRESS = 2L;
    public static final Long MEETING_STATUE_FINISHED = 3L;


    /**
     * 会议预约返回值
     * */
    public static final String RETURN_IS_OPENING = "当前会议不可预约";


}
