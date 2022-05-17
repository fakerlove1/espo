package com.stackfarm.esports.result.activity;

/**
 * @Author xiaohuang
 * @create 2021/4/5 16:09
 */
public class ActUserBean {
    /**
     * 真实姓名
     */
    private String name;
    /**
     * 报名的岗位
     */
    private String staffType;
    /**
     * 成员数量
     */
    private Integer memberCount;
    /**
     * 状态
     */
    private String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ActUserBean() {
    }

    public ActUserBean(String name, String staffType, Integer memberCount, String state) {
        this.name = name;
        this.staffType = staffType;
        this.memberCount = memberCount;
        this.state = state;
    }

    @Override
    public String toString() {
        return "ActUserBean{" +
                "name='" + name + '\'' +
                ", staffType='" + staffType + '\'' +
                ", memberCount=" + memberCount +
                ", state='" + state + '\'' +
                '}';
    }
}
