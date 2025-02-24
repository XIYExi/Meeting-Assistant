package com.ruoyi.meeting.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 积分物品对象 points_items
 * 
 * @author xiye
 * @date 2025-02-24
 */
public class PointsItems extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 物品名称 */
    @Excel(name = "物品名称")
    private String title;

    /** 物品描述 */
    @Excel(name = "物品描述")
    private String description;

    /** 对话花费 */
    @Excel(name = "对话花费")
    private Long cost;

    /** 剩余存量 */
    @Excel(name = "剩余存量")
    private Long remaining;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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
    public void setCost(Long cost) 
    {
        this.cost = cost;
    }

    public Long getCost() 
    {
        return cost;
    }
    public void setRemaining(Long remaining) 
    {
        this.remaining = remaining;
    }

    public Long getRemaining() 
    {
        return remaining;
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
            .append("cost", getCost())
            .append("remaining", getRemaining())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
