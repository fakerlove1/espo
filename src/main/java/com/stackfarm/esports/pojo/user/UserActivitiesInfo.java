package com.stackfarm.esports.pojo.user;

/**
 * @author croton
 * @create 2021/4/2 14:53
 */
public class UserActivitiesInfo {
    private Long userId;
    private Integer point;
    private Integer times;
    private Integer successTimes;

    public UserActivitiesInfo() {
    }

    public UserActivitiesInfo(Long userId, Integer point, Integer times, Integer successTimes) {
        this.userId = userId;
        this.point = point;
        this.times = times;
        this.successTimes = successTimes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getSuccessTimes() {
        return successTimes;
    }

    public void setSuccessTimes(Integer successTimes) {
        this.successTimes = successTimes;
    }

    @Override
    public String toString() {
        return "UserActivitiesInfo{" +
                "userId=" + userId +
                ", point=" + point +
                ", times=" + times +
                ", successTimes=" + successTimes +
                '}';
    }
}
