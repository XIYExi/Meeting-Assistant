package com.ruoyi.meeting.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 会议嘉宾对象 meeting_guest
 * 
 * @author xiye
 * @date 2024-12-25
 */
public class MeetingGuest extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 嘉宾表id */
    private Long id;

    /** 嘉宾姓名 */
    @Excel(name = "嘉宾姓名")
    private String name;

    /** 嘉宾头衔 */
    @Excel(name = "嘉宾头衔")
    private String title;

    /** 嘉宾主讲内容 */
    @Excel(name = "嘉宾主讲内容")
    private String content;

    /** 查看统计 */
    @Excel(name = "查看统计")
    private Long views;

    /** 点赞统计 */
    @Excel(name = "点赞统计")
    private Long likes;

    /** 嘉宾头像 */
    @Excel(name = "嘉宾头像")
    private String avatar;

    /** 嘉宾海报 */
    @Excel(name = "嘉宾海报")
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
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setViews(Long views) 
    {
        this.views = views;
    }

    public Long getViews() 
    {
        return views;
    }
    public void setLikes(Long likes) 
    {
        this.likes = likes;
    }

    public Long getLikes() 
    {
        return likes;
    }
    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
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
            .append("name", getName())
            .append("title", getTitle())
            .append("content", getContent())
            .append("views", getViews())
            .append("likes", getLikes())
            .append("avatar", getAvatar())
            .append("url", getUrl())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
