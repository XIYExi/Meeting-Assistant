package com.ruoyi.meeting.qo;

import com.ruoyi.meeting.domain.Meeting;
import org.springframework.web.multipart.MultipartFile;


public class MeetingInsertQuery extends Meeting {
    private static final long serialVersionUID = 1L;

    private String imageId;

    private MultipartFile file;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
