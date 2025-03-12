package com.ruoyi.rag.domain.query;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("meeting_geo")
public class MeetingGeo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String country;
    private String formattedAddress;
    private String province;
    private String city;
    private String citycode;
    private String district;
    private String street;
    private String number;
    private String adcode;
    private String location;
    private String delFlag;

    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
