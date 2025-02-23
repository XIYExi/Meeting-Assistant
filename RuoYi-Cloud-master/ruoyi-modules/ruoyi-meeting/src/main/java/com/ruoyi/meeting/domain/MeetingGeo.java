package com.ruoyi.meeting.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 会议地图对象 meeting_geo
 * 
 * @author xiye
 * @date 2025-02-23
 */
public class MeetingGeo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 国家 */
    @Excel(name = "国家")
    private String country;

    /** 格式化地址 */
    @Excel(name = "格式化地址")
    private String formattedAddress;

    /** 省份 */
    @Excel(name = "省份")
    private String province;

    /** 城市 */
    @Excel(name = "城市")
    private String city;

    /** 城市编码 */
    @Excel(name = "城市编码")
    private String citycode;

    /** 区 */
    @Excel(name = "区")
    private String district;

    /** 街道 */
    @Excel(name = "街道")
    private String street;

    /** 门牌 */
    @Excel(name = "门牌")
    private String number;

    /** 区域编码 */
    @Excel(name = "区域编码")
    private String adcode;

    /** 经度，纬度 */
    @Excel(name = "经度，纬度")
    private String location;

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
    public void setCountry(String country) 
    {
        this.country = country;
    }

    public String getCountry() 
    {
        return country;
    }
    public void setFormattedAddress(String formattedAddress) 
    {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedAddress() 
    {
        return formattedAddress;
    }
    public void setProvince(String province) 
    {
        this.province = province;
    }

    public String getProvince() 
    {
        return province;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setCitycode(String citycode) 
    {
        this.citycode = citycode;
    }

    public String getCitycode() 
    {
        return citycode;
    }
    public void setDistrict(String district) 
    {
        this.district = district;
    }

    public String getDistrict() 
    {
        return district;
    }
    public void setStreet(String street) 
    {
        this.street = street;
    }

    public String getStreet() 
    {
        return street;
    }
    public void setNumber(String number) 
    {
        this.number = number;
    }

    public String getNumber() 
    {
        return number;
    }
    public void setAdcode(String adcode) 
    {
        this.adcode = adcode;
    }

    public String getAdcode() 
    {
        return adcode;
    }
    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
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
            .append("country", getCountry())
            .append("formattedAddress", getFormattedAddress())
            .append("province", getProvince())
            .append("city", getCity())
            .append("citycode", getCitycode())
            .append("district", getDistrict())
            .append("street", getStreet())
            .append("number", getNumber())
            .append("adcode", getAdcode())
            .append("location", getLocation())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
