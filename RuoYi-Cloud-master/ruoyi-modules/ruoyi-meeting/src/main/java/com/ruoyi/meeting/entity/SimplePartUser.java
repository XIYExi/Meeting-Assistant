package com.ruoyi.meeting.entity;

import java.util.List;

public class SimplePartUser {

    private Integer parts;

    private List<String> avatars;

    public Integer getParts() {
        return parts;
    }

    public void setParts(Integer parts) {
        this.parts = parts;
    }

    public List<String> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<String> avatars) {
        this.avatars = avatars;
    }
}
