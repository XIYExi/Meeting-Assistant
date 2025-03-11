package com.ruoyi.rec.service;

import com.ruoyi.rec.domain.ContentMeetingRec;
import com.ruoyi.rec.domain.Meeting;
import com.ruoyi.rec.domain.MeetingResponse;

import java.util.List;


public interface RecService {
    public List<MeetingResponse> selectContentRecMeetingList(Long meetingId);

    public List<MeetingResponse> selectStaticLLMRecMeetingList();

    public List<MeetingResponse> selectStreamRecMeetingList(Long userId);
}
