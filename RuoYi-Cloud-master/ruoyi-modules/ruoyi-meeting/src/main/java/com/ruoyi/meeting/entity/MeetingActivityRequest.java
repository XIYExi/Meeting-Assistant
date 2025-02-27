package com.ruoyi.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingActivityRequest implements Serializable {
    private Long id;
    private Long sectorId;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date time;
    private String url;
    private Long type;
    private Long redirect;
    private String content;
    private String delFlag;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String remark;
    private String imageId;
    private MultipartFile file;
}
