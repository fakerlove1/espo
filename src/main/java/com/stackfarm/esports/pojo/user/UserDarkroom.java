package com.stackfarm.esports.pojo.user;

/**
 * @Author xiaohuang
 * @create 2021/4/7 20:22
 */
public class UserDarkroom {

    private Long id;

    private Long userId;

    private Long time;

    private Long createTime;

    private String cause;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public UserDarkroom() {
    }

    public UserDarkroom(Long id, Long userId, Long time, Long createTime, String cause) {
        this.id = id;
        this.userId = userId;
        this.time = time;
        this.createTime = createTime;
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "UserDarkroom{" +
                "id=" + id +
                ", userId=" + userId +
                ", time=" + time +
                ", createTime=" + createTime +
                ", cause='" + cause + '\'' +
                '}';
    }
}
