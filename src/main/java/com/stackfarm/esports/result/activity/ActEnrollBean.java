package com.stackfarm.esports.result.activity;

import java.math.BigDecimal;

/**
 * @Author xiaohuang
 * @create 2021/4/5 15:25
 */
public class ActEnrollBean {
    /**
     * 活动名称
     */
    private String name;
    /**
     * 活动开始时间
     */
    private Long beginTime;
    /**
     * 活动结束
     */
    private Long endTime;
    /**
     * 活动类型
     */
    private String types;
    /**
     * 活动地点
     */
    private String location;
    /**
     * 详细地址
     */
    private String detailedLocation;
    /**
     * 活动状态
     */
    private String state;
    /**
     * 活动人员类别
     */
    private String staffTypes;
    /**
     * 活动人员类别数量
     */
    private String staffTypesCount;
    /**
     * 岗位要求
     */
    private String requirement;
    /**
     * 报名费用
     */
    private BigDecimal cost;
    /**
     * 活动奖金
     */
    private BigDecimal reward;

    /**
     * 活动联系方式
     */
    private String contactWay;

    /**
     * 活动简介
     */
    private String introduction;
    /**
     * 活动海报
     */
    private String poster;
    /**
     * 用户参加活动状态
     */
    private String userActState;
    /**
     * 报名未通过原因
     */
    private String cause;
    /**
     * 报名的岗位
     */
    private String staffType;
    /**
     * 小队名称
     */
    private String teamName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetailedLocation() {
        return detailedLocation;
    }

    public void setDetailedLocation(String detailedLocation) {
        this.detailedLocation = detailedLocation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStaffTypes() {
        return staffTypes;
    }

    public void setStaffTypes(String staffTypes) {
        this.staffTypes = staffTypes;
    }

    public String getStaffTypesCount() {
        return staffTypesCount;
    }

    public void setStaffTypesCount(String staffTypesCount) {
        this.staffTypesCount = staffTypesCount;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getUserActState() {
        return userActState;
    }

    public void setUserActState(String userActState) {
        this.userActState = userActState;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ActEnrollBean() {
    }

    public ActEnrollBean(String name, Long beginTime, Long endTime, String types, String location, String detailedLocation, String state, String staffTypes, String staffTypesCount, String requirement, BigDecimal cost, BigDecimal reward, String contactWay, String introduction, String poster, String userActState, String cause, String staffType, String teamName) {
        this.name = name;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.types = types;
        this.location = location;
        this.detailedLocation = detailedLocation;
        this.state = state;
        this.staffTypes = staffTypes;
        this.staffTypesCount = staffTypesCount;
        this.requirement = requirement;
        this.cost = cost;
        this.reward = reward;
        this.contactWay = contactWay;
        this.introduction = introduction;
        this.poster = poster;
        this.userActState = userActState;
        this.cause = cause;
        this.staffType = staffType;
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "ActEnrollBean{" +
                "name='" + name + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", types='" + types + '\'' +
                ", location='" + location + '\'' +
                ", detailedLocation='" + detailedLocation + '\'' +
                ", state='" + state + '\'' +
                ", staffTypes='" + staffTypes + '\'' +
                ", staffTypesCount='" + staffTypesCount + '\'' +
                ", requirement='" + requirement + '\'' +
                ", cost=" + cost +
                ", reward=" + reward +
                ", contactWay='" + contactWay + '\'' +
                ", introduction='" + introduction + '\'' +
                ", poster='" + poster + '\'' +
                ", userActState='" + userActState + '\'' +
                ", cause='" + cause + '\'' +
                ", staffType='" + staffType + '\'' +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}
