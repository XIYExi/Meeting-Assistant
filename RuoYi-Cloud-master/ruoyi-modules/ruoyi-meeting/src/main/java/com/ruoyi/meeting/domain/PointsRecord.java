package com.ruoyi.meeting.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 积分记录对象 points_record
 * 
 * @author xiye
 * @date 2025-02-24
 */
public class PointsRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 对应任务id */
    @Excel(name = "对应任务id")
    private Long pointId;

    /** 获得积分 */
    @Excel(name = "获得积分")
    private Long getPoints;

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
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setPointId(Long pointId) 
    {
        this.pointId = pointId;
    }

    public Long getPointId() 
    {
        return pointId;
    }
    public void setGetPoints(Long getPoints) 
    {
        this.getPoints = getPoints;
    }

    public Long getGetPoints() 
    {
        return getPoints;
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
            .append("userId", getUserId())
            .append("pointId", getPointId())
            .append("getPoints", getGetPoints())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
