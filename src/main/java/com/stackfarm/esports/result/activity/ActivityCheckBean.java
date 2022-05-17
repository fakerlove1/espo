package com.stackfarm.esports.result.activity;

import com.stackfarm.esports.pojo.activity.Activity;
import com.stackfarm.esports.pojo.activity.ActivityExtension;

/**
 * @author croton
 * @create 2021/4/6 14:15
 */
public class ActivityCheckBean {
    private Activity activity;
    private ActivityExtension activityExtension;
    private String zipUrl;

    public ActivityCheckBean() {
    }

    public ActivityCheckBean(Activity activity, ActivityExtension activityExtension, String zipUrl) {
        this.activity = activity;
        this.activityExtension = activityExtension;
        this.zipUrl = zipUrl;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ActivityExtension getActivityExtension() {
        return activityExtension;
    }

    public void setActivityExtension(ActivityExtension activityExtension) {
        this.activityExtension = activityExtension;
    }

    public String getZipUrl() {
        return zipUrl;
    }

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    @Override
    public String toString() {
        return "ActivityCheckBean{" +
                "activity=" + activity +
                ", activityExtension=" + activityExtension +
                ", zipUrl='" + zipUrl + '\'' +
                '}';
    }
}
