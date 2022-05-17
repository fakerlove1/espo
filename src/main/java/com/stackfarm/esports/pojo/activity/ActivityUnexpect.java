package com.stackfarm.esports.pojo.activity;

/**
 * @Author xiaohuang
 * @create 2021/4/1 20:12
 */
public class ActivityUnexpect {

    private Long activityId;

    private Long userId;

    /**
     * 驳回原因
     */
    private String message;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ActivityUnexpect() {
    }

    public ActivityUnexpect(Long activityId, Long userId, String message) {
        this.activityId = activityId;
        this.userId = userId;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ActivityUnexpect{" +
                "activityId=" + activityId +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                '}';
    }
}
