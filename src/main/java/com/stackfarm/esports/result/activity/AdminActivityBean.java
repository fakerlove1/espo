package com.stackfarm.esports.result.activity;

import com.stackfarm.esports.pojo.activity.Activity;
import com.stackfarm.esports.pojo.activity.ActivityExtension;

/**
 * @author croton
 * @create 2021/4/6 19:44
 */
public class AdminActivityBean {

    private Activity activity;
    private ActivityExtension activityExtension;
    private String launcherName;
    private String zipUrl;

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

    public String getLauncherName() {
        return launcherName;
    }

    public void setLauncherName(String launcherName) {
        this.launcherName = launcherName;
    }

    public String getZipUrl() {
        return zipUrl;
    }

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    public AdminActivityBean() {
    }

    public AdminActivityBean(Activity activity, ActivityExtension activityExtension, String launcherName, String zipUrl) {
        this.activity = activity;
        this.activityExtension = activityExtension;
        this.launcherName = launcherName;
        this.zipUrl = zipUrl;
    }

    @Override
    public String toString() {
        return "AdminActivityBean{" +
                "activity=" + activity +
                ", activityExtension=" + activityExtension +
                ", launcherName='" + launcherName + '\'' +
                ", zipUrl='" + zipUrl + '\'' +
                '}';
    }
}
