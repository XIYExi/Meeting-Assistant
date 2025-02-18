package com.ruoyi.live.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
@TableName("living_room")
public class LiveRoom {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long anchorId;
    private Integer type;
    private Integer status;
    private String roomName;
    private Integer watchNum;
    private Integer goodNum;
    private Date startTime;
    private String delFlag;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

}
