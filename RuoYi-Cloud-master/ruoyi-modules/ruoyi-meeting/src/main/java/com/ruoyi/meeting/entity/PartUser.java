package com.ruoyi.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class PartUser implements Serializable {

    private static final long serialVersionUID = 1L;

    // meeting_schedule.id
    private Long id;

    private Long userId;

    private String nickName;

    private String email;

    private String phonenumber;

    private String sex;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date partTime;


    @Override
    public String toString() {
        return "PartUser{" +
                "id=" + id +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", sex='" + sex + '\'' +
                ", partTime=" + partTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getPartTime() {
        return partTime;
    }

    public void setPartTime(Date partTime) {
        this.partTime = partTime;
    }
}
