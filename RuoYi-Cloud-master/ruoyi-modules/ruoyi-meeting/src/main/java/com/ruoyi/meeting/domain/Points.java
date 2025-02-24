package com.ruoyi.meeting.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 积分对象 points
 * 
 * @author xiye
 * @date 2025-02-24
 */
public class Points extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 积分任务名称 */
    @Excel(name = "积分任务名称")
    private String title;

    /** 疾风任务描述 */
    @Excel(name = "疾风任务描述")
    private String description;

    /** 任务奖励积分数量 */
    @Excel(name = "任务奖励积分数量")
    private Long point;

    /** 任务可获取次数 1-仅可完成一次 2-每日一次 */
    @Excel(name = "任务可获取次数 1-仅可完成一次 2-每日一次")
    private Long type;

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
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setPoint(Long point) 
    {
        this.point = point;
    }

    public Long getPoint() 
    {
        return point;
    }
    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
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
            .append("title", getTitle())
            .append("description", getDescription())
            .append("point", getPoint())
            .append("type", getType())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
