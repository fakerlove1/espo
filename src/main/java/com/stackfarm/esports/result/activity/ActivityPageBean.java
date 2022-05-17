package com.stackfarm.esports.result.activity;

import java.util.List;

/**
 * @Author xiaohuang
 * @create 2021/4/7 17:53
 */
public class ActivityPageBean {

    private List<AdminActivityBean> adminActivityBeanList;

    private Integer pageCount;

    private Integer activityCount;

    public List<AdminActivityBean> getAdminActivityBeanList() {
        return adminActivityBeanList;
    }

    public void setAdminActivityBeanList(List<AdminActivityBean> adminActivityBeanList) {
        this.adminActivityBeanList = adminActivityBeanList;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(Integer activityCount) {
        this.activityCount = activityCount;
    }

    public ActivityPageBean() {
    }

    public ActivityPageBean(List<AdminActivityBean> adminActivityBeanList, Integer pageCount, Integer activityCount) {
        this.adminActivityBeanList = adminActivityBeanList;
        this.pageCount = pageCount;
        this.activityCount = activityCount;
    }

    @Override
    public String toString() {
        return "ActivityPageBean{" +
                "adminActivityBeanList=" + adminActivityBeanList +
                ", pageCount=" + pageCount +
                ", activityCount=" + activityCount +
                '}';
    }
}
