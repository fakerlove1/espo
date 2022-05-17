package com.stackfarm.esports.pojo.user;

/**
 * @Author xiaohuang
 * @create 2021/4/1 18:40
 */
public class UserActivity {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 活动id
     */
    private Long actId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 状态
     */
    private String state;

    /**
     * 原因
     */
    private String cause;

    /**
     * 从中获得的积分
     */
    private Integer point;

    /**
     * 报名的岗位
     */
    private String staffType;


    /**
     * 小队名称
     */
    private String teamName;

    /**
     * 成员数量
     */
    private Integer memberCount;

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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public UserActivity() {
    }

    public UserActivity(Long id, Long userId, Long actId, Long createTime, String state, String cause, Integer point,
                        String staffType, String teamName, Integer memberCount) {
        this.id = id;
        this.userId = userId;
        this.actId = actId;
        this.createTime = createTime;
        this.state = state;
        this.cause = cause;
        this.point = point;
        this.staffType = staffType;
        this.teamName = teamName;
        this.memberCount = memberCount;
    }

    @Override
    public String toString() {
        return "UserActivity{" +
                "id=" + id +
                ", userId=" + userId +
                ", actId=" + actId +
                ", createTime=" + createTime +
                ", state='" + state + '\'' +
                ", cause='" + cause + '\'' +
                ", point=" + point +
                ", staffType='" + staffType + '\'' +
                ", teamName='" + teamName + '\'' +
                ", memberCount=" + memberCount +
                '}';
    }
}
