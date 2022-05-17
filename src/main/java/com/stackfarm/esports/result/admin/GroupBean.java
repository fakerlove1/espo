package com.stackfarm.esports.result.admin;

/**
 * @author croton
 * @create 2021/4/7 16:02
 */
public class GroupBean {
    private String username;
    private String groupName;
    private String groupType;
    private String businessScope;
    private Integer launchedActivities;
    private Integer participatedActivities;
    private Integer point;
    private Long id;
    private Long darkroom;

    public GroupBean(String username, String groupName, String groupType, String businessScope, Integer launchedActivities, Integer participatedActivities, Integer point, Long id, Long darkroom) {
        this.username = username;
        this.groupName = groupName;
        this.groupType = groupType;
        this.businessScope = businessScope;
        this.launchedActivities = launchedActivities;
        this.participatedActivities = participatedActivities;
        this.point = point;
        this.id = id;
        this.darkroom = darkroom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public Integer getLaunchedActivities() {
        return launchedActivities;
    }

    public void setLaunchedActivities(Integer launchedActivities) {
        this.launchedActivities = launchedActivities;
    }

    public Integer getParticipatedActivities() {
        return participatedActivities;
    }

    public void setParticipatedActivities(Integer participatedActivities) {
        this.participatedActivities = participatedActivities;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDarkroom() {
        return darkroom;
    }

    public void setDarkroom(Long darkroom) {
        this.darkroom = darkroom;
    }
}
