package com.stackfarm.esports.pojo.activity;

import java.math.BigDecimal;

/**
 * @Author xiaohuang
 * @create 2021/4/1 17:53
 */
public class ActivityExtension {

    /**
     * 活动id
     */
    private Long actId;

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

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
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

    public ActivityExtension() {
    }

    public ActivityExtension(Long actId, String staffTypes, String staffTypesCount,
                             String requirement, BigDecimal cost, BigDecimal reward, Integer point,
                             String contactWay, String introduction, String poster, String picture, String plan) {
        this.actId = actId;
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
    }

    @Override
    public String toString() {
        return "ActivityExtension{" +
                "actId=" + actId +
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
                '}';
    }
}
