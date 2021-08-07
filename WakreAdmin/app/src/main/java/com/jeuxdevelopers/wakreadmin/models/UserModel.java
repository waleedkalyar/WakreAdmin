package com.jeuxdevelopers.wakreadmin.models;


import com.jeuxdevelopers.wakreadmin.enums.AccountState;
import com.jeuxdevelopers.wakreadmin.enums.UserType;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String uId, name, phone, address, shopName, profileImageUrl;
    private UserType userType;
    private AccountState accountState;
    private double amount;

    public UserModel() {
    }

    public UserModel(String uId, String name, String phone, String address, String shopName,
                     String profileImageUrl, UserType userType, AccountState accountState) {
        this.uId = uId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.shopName = shopName;
        this.profileImageUrl = profileImageUrl;
        this.userType = userType;
        this.accountState = accountState;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public AccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
