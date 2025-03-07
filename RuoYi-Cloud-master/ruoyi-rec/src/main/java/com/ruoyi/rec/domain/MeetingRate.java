package com.ruoyi.rec.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@TableName("meeting_rate")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MeetingRate {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long meetingId;
    private Integer score;
    private Integer views;
    private BigDecimal rate;
}
