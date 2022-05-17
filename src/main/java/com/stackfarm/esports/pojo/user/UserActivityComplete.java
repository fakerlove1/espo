package com.stackfarm.esports.pojo.user;

/**
 * @author croton
 * @create 2021/4/7 10:55
 */
public class UserActivityComplete {
    private Long userId;
    private Long actId;
    private Boolean state;
    private String comment;

    public UserActivityComplete() {
    }

    public UserActivityComplete(Long userId, Long actId, Boolean state, String comment) {
        this.userId = userId;
        this.actId = actId;
        this.state = state;
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "UserActivityComplete{" +
                "userId=" + userId +
                ", actId=" + actId +
                ", state='" + state + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
