package com.stackfarm.esports.result.user;

import com.stackfarm.esports.dao.user.UserActivityDao;
import com.stackfarm.esports.pojo.user.UserActivity;

/**
 * @Author xiaohuang
 * @create 2021/4/7 9:52
 */
public class UserGetBean {

    private UserActivity userActivity;

    private Integer age;

    private String type;

    private Integer successTimes;

    private String completion;

    private String realName;

    private String profileUrl;

    private String phoneNumber;

    private Integer times;

    public UserActivity getUserActivity() {
        return userActivity;
    }

    public void setUserActivity(UserActivity userActivity) {
        this.userActivity = userActivity;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSuccessTimes() {
        return successTimes;
    }

    public void setSuccessTimes(Integer successTimes) {
        this.successTimes = successTimes;
    }

    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public UserGetBean() {
    }

    public UserGetBean(UserActivity userActivity, Integer age, String type, Integer successTimes, String completion, String realName, String profileUrl, String phoneNumber, Integer times) {
        this.userActivity = userActivity;
        this.age = age;
        this.type = type;
        this.successTimes = successTimes;
        this.completion = completion;
        this.realName = realName;
        this.profileUrl = profileUrl;
        this.phoneNumber = phoneNumber;
        this.times = times;
    }

    @Override
    public String toString() {
        return "UserGetBean{" +
                "userActivity=" + userActivity +
                ", age=" + age +
                ", type='" + type + '\'' +
                ", successTimes=" + successTimes +
                ", completion='" + completion + '\'' +
                ", realName='" + realName + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", times=" + times +
                '}';
    }
}
