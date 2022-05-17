package com.stackfarm.esports.pojo.activity;

/**
 * @author croton
 * @create 2021/4/1 12:57
 */
public class Activity {
    private Long id;
    private String name;
    private Long launcherId;
    private Long launchedTime;
    private Long beginTime;
    private Long endTime;
    private Long enrollBeginTime;
    private Long enrollEndTime;
    private String scope;
    private String types;
    private String location;
    private String detailedLocation;
    private Integer level;
    private String note;
    private Integer sizeOfPeople;
    private String state;
    private String participateIds;

    public Activity() {
    }

    public Activity(Long id, String name, Long launcherId, Long launchedTime, Long beginTime, Long endTime, Long enrollBeginTime, Long enrollEndTime, String scope, String types, String location, String detailedLocation, Integer level, String note, Integer sizeOfPeople, String state, String participateIds) {
        this.id = id;
        this.name = name;
        this.launcherId = launcherId;
        this.launchedTime = launchedTime;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.enrollBeginTime = enrollBeginTime;
        this.enrollEndTime = enrollEndTime;
        this.scope = scope;
        this.types = types;
        this.location = location;
        this.detailedLocation = detailedLocation;
        this.level = level;
        this.note = note;
        this.sizeOfPeople = sizeOfPeople;
        this.state = state;
        this.participateIds = participateIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLauncherId() {
        return launcherId;
    }

    public void setLauncherId(Long launcherId) {
        this.launcherId = launcherId;
    }

    public Long getLaunchedTime() {
        return launchedTime;
    }

    public void setLaunchedTime(Long launchedTime) {
        this.launchedTime = launchedTime;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getEnrollBeginTime() {
        return enrollBeginTime;
    }

    public void setEnrollBeginTime(Long enrollBeginTime) {
        this.enrollBeginTime = enrollBeginTime;
    }

    public Long getEnrollEndTime() {
        return enrollEndTime;
    }

    public void setEnrollEndTime(Long enrollEndTime) {
        this.enrollEndTime = enrollEndTime;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetailedLocation() {
        return detailedLocation;
    }

    public void setDetailedLocation(String detailedLocation) {
        this.detailedLocation = detailedLocation;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSizeOfPeople() {
        return sizeOfPeople;
    }

    public void setSizeOfPeople(Integer sizeOfPeople) {
        this.sizeOfPeople = sizeOfPeople;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParticipateIds() {
        return participateIds;
    }

    public void setParticipateIds(String participateIds) {
        this.participateIds = participateIds;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", launcherId=" + launcherId +
                ", launchedTime=" + launchedTime +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", enrollBeginTime=" + enrollBeginTime +
                ", enrollEndTime=" + enrollEndTime +
                ", scope='" + scope + '\'' +
                ", types='" + types + '\'' +
                ", location='" + location + '\'' +
                ", detailedLocation='" + detailedLocation + '\'' +
                ", level=" + level +
                ", note='" + note + '\'' +
                ", sizeOfPeople=" + sizeOfPeople +
                ", state='" + state + '\'' +
                ", participateIds='" + participateIds + '\'' +
                '}';
    }
}
