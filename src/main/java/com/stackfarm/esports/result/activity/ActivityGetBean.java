package com.stackfarm.esports.result.activity;

/**
 * @Author xiaohuang
 * @create 2021/4/7 15:07
 */
public class ActivityGetBean {

    private ActivityBean activityBean;

    private String time;

    public ActivityBean getActivityBean() {
        return activityBean;
    }

    public void setActivityBean(ActivityBean activityBean) {
        this.activityBean = activityBean;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ActivityGetBean(ActivityBean activityBean, String time) {
        this.activityBean = activityBean;
        this.time = time;
    }

    public ActivityGetBean() {
    }

    @Override
    public String toString() {
        return "ActivityGetBean{" +
                "activityBean=" + activityBean +
                ", time='" + time + '\'' +
                '}';
    }
}
