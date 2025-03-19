package com.ruoyi.rag.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.rec.api.domain.MeetingGeo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MeetingRecResponse implements Serializable {
    private Long id;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    private String location;
    private Long views;
    private Long type;
    private String meetingType;
    private String route;
}
