package com.ruoyi.rec.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation implements Serializable {
    private Integer meetingId;
    private Double score;
}
