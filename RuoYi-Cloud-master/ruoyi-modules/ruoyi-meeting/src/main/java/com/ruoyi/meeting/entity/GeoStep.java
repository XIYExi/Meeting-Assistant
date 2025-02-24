package com.ruoyi.meeting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GeoStep implements Serializable {
    private String orientation;
    private String step_distance;
    private String instruction;
    private String polyline;
}
