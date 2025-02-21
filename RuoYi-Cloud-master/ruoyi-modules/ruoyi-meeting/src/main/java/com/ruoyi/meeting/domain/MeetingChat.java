package com.ruoyi.meeting.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 会议聊天对象 meeting_chat
 * 
 * @author xiye
 * @date 2024-12-25
 */
public class MeetingChat extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 聊天表id */
    private Long id;

    /** 会议表id */
    @Excel(name = "会议表id")
    private Long meetingId;

    private Long userId;

    private Long roomId;

    private String nickName;

    /** 聊天信息 */
    @Excel(name = "聊天信息")
    private String content;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMeetingId(Long meetingId) 
    {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() 
    {
        return meetingId;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "MeetingChat{" +
                "id=" + id +
                ", meetingId=" + meetingId +
                ", userId=" + userId +
                ", roomId=" + roomId +
                ", nickName='" + nickName + '\'' +
                ", content='" + content + '\'' +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
