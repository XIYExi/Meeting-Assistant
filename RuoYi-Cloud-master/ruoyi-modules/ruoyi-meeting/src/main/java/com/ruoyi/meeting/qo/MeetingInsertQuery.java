package com.ruoyi.meeting.qo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


public class MeetingInsertQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 会议名称 */
    private String title;

    /** 会议开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /** 会议结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 会议地点 */
    private String path;
    private String location;

    /** 会议封面海报图 */
    private String url;

    /** 查看次数 */
    private Long views;

    /** 会议类型 */
    private Long type;

    /** 会议状态 */
    private Long status;

    /** 会议开展类型 */
    private Long meetingType;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private String imageId;

    private MultipartFile file;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(Long meetingType) {
        this.meetingType = meetingType;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "MeetingInsertQuery{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", path='" + path + '\'' +
                ", location='" + location + '\'' +
                ", url='" + url + '\'' +
                ", views=" + views +
                ", type=" + type +
                ", status=" + status +
                ", meetingType=" + meetingType +
                ", delFlag='" + delFlag + '\'' +
                ", imageId='" + imageId + '\'' +
                ", file=" + file +
                '}';
    }
}
