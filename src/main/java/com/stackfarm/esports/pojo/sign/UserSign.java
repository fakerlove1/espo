package com.stackfarm.esports.pojo.sign;

/**
 * @author croton
 * @create 2021/7/4 10:41
 */
public class UserSign {
    private Long id;
    private Long userId;
    private Long actId;
    private Boolean state;
    private Long createTime;

    public UserSign() {
    }

    public UserSign(Long id, Long userId, Long actId, Boolean state, Long createTime) {
        this.id = id;
        this.userId = userId;
        this.actId = actId;
        this.state = state;
        this.createTime = createTime;
    }

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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
