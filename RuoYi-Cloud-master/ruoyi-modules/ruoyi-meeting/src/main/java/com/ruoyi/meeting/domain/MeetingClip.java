package com.ruoyi.meeting.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 会议附件对象 meeting_clip
 * 
 * @author xiye
 * @date 2025-02-22
 */
public class MeetingClip extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 会议id */
    @Excel(name = "会议id")
    private Long meetingId;

    /** 议程id */
    @Excel(name = "议程id")
    private Long agendaId;

    /** 附件类型，1表示总附件查询meetingId， 2表示子议程附件，查询agendaId，3表示软件附件，不在会议详细中展示 */
    @Excel(name = "附件类型，1表示总附件查询meetingId， 2表示子议程附件，查询agendaId，3表示软件附件，不在会议详细中展示")
    private Long clipType;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 上传者 */
    @Excel(name = "上传者")
    private Long uploadUserId;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String fileType;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long fileSize;

    /** cos存储地址 */
    @Excel(name = "cos存储地址")
    private String url;

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
    public void setAgendaId(Long agendaId) 
    {
        this.agendaId = agendaId;
    }

    public Long getAgendaId() 
    {
        return agendaId;
    }
    public void setClipType(Long clipType) 
    {
        this.clipType = clipType;
    }

    public Long getClipType() 
    {
        return clipType;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setUploadUserId(Long uploadUserId) 
    {
        this.uploadUserId = uploadUserId;
    }

    public Long getUploadUserId() 
    {
        return uploadUserId;
    }
    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }
    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("meetingId", getMeetingId())
            .append("agendaId", getAgendaId())
            .append("clipType", getClipType())
            .append("fileName", getFileName())
            .append("uploadUserId", getUploadUserId())
            .append("fileType", getFileType())
            .append("fileSize", getFileSize())
            .append("url", getUrl())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
