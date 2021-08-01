package com.jeuxdevelopers.wakreadmin.models;

import java.io.Serializable;

public class TransactionUser implements Serializable {
    private String userId, name, profileImage, phone;

    public TransactionUser() {
    }

    public TransactionUser(String userId, String name, String profileImage, String phone) {
        this.userId = userId;
        this.name = name;
        this.profileImage = profileImage;
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
