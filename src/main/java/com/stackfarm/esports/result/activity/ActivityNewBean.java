package com.stackfarm.esports.result.activity;

import java.math.BigDecimal;

/**
 * @Author xiaohuang
 * @create 2021/4/9 13:37
 */
public class ActivityNewBean {
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 发布者ID
     */
    private Long launcherId;

    /**
     * 发布时间
     */
    private Long launchedTime;

    /**
     * 活动开始时间
     */
    private Long beginTime;

    /**
     * 活动结束时间
     */
    private Long endTime;

    /**
     * 报名开始时间
     */
    private Long enrollBeginTime;

    /**
     * 报名截止时间
     */
    private Long enrollEndTime;

    /**
     * 活动面向对象
     */
    private String scope;

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
     * 活动等级
     */
    private Integer level;

    /**
     * 备注
     */
    private String note;

    /**
     * 参赛总人数
     */
    private Integer sizeOfPeople;

    /**
     * 活动状态
     */
    private String state;

    /**
     * 参赛者们ID
     */
    private String participateIds;


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
     * 活动可获得积分
     */
    private Integer point;

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
     * 活动组图
     */
    private String picture;

    /**
     * 活动企划书
     */
    private String plan;

    /**
     * 撤回原因
     */
    private String cause;

    public ActivityNewBean() {
    }

    public ActivityNewBean(Long id, String name, Long launcherId, Long launchedTime, Long beginTime, Long endTime, Long enrollBeginTime, Long enrollEndTime, String scope, String types, String location, String detailedLocation, Integer level, String note, Integer sizeOfPeople, String state, String participateIds, String staffTypes, String staffTypesCount, String requirement, BigDecimal cost, BigDecimal reward, Integer point, String contactWay, String introduction, String poster, String picture, String plan, String cause) {
        this.id = id;
        this.name = name;
        this.launcherId = launcherId;
        this.launchedTime = launchedTime;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.enrollBeginTime = enrollBeginTime;
        this.enrollEndTime = enrollEndTime;
        this.scope = scope;
        this.types = types;
        this.location = location;
        this.detailedLocation = detailedLocation;
        this.level = level;
        this.note = note;
        this.sizeOfPeople = sizeOfPeople;
        this.state = state;
        this.participateIds = participateIds;
        this.staffTypes = staffTypes;
        this.staffTypesCount = staffTypesCount;
        this.requirement = requirement;
        this.cost = cost;
        this.reward = reward;
        this.point = point;
        this.contactWay = contactWay;
        this.introduction = introduction;
        this.poster = poster;
        this.picture = picture;
        this.plan = plan;
        this.cause = cause;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLauncherId() {
        return launcherId;
    }

    public void setLauncherId(Long launcherId) {
        this.launcherId = launcherId;
    }

    public Long getLaunchedTime() {
        return launchedTime;
    }

    public void setLaunchedTime(Long launchedTime) {
        this.launchedTime = launchedTime;
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

    public Long getEnrollBeginTime() {
        return enrollBeginTime;
    }

    public void setEnrollBeginTime(Long enrollBeginTime) {
        this.enrollBeginTime = enrollBeginTime;
    }

    public Long getEnrollEndTime() {
        return enrollEndTime;
    }

    public void setEnrollEndTime(Long enrollEndTime) {
        this.enrollEndTime = enrollEndTime;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSizeOfPeople() {
        return sizeOfPeople;
    }

    public void setSizeOfPeople(Integer sizeOfPeople) {
        this.sizeOfPeople = sizeOfPeople;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParticipateIds() {
        return participateIds;
    }

    public void setParticipateIds(String participateIds) {
        this.participateIds = participateIds;
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

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "ActivityNewBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", launcherId=" + launcherId +
                ", launchedTime=" + launchedTime +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", enrollBeginTime=" + enrollBeginTime +
                ", enrollEndTime=" + enrollEndTime +
                ", scope='" + scope + '\'' +
                ", types='" + types + '\'' +
                ", location='" + location + '\'' +
                ", detailedLocation='" + detailedLocation + '\'' +
                ", level=" + level +
                ", note='" + note + '\'' +
                ", sizeOfPeople=" + sizeOfPeople +
                ", state='" + state + '\'' +
                ", participateIds='" + participateIds + '\'' +
                ", staffTypes='" + staffTypes + '\'' +
                ", staffTypesCount='" + staffTypesCount + '\'' +
                ", requirement='" + requirement + '\'' +
                ", cost=" + cost +
                ", reward=" + reward +
                ", point=" + point +
                ", contactWay='" + contactWay + '\'' +
                ", introduction='" + introduction + '\'' +
                ", poster='" + poster + '\'' +
                ", picture='" + picture + '\'' +
                ", plan='" + plan + '\'' +
                ", cause='" + cause + '\'' +
                '}';
    }
}
