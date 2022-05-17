package com.stackfarm.esports.pojo.authorize;

/**
 * @author croton
 * @create 2021/9/5 8:31
 */
public class Application {
    private Long id;
    private Long memberId;
    private String applicationType;
    private Long createTime;
    private Long checkTime;
    private Boolean result;
    private String cause;
    private String profile;

    public Application() {
    }

    public Application(Long id, Long memberId, String applicationType, Long createTime, Long checkTime, Boolean result, String cause, String profile) {
        this.id = id;
        this.memberId = memberId;
        this.applicationType = applicationType;
        this.createTime = createTime;
        this.checkTime = checkTime;
        this.result = result;
        this.cause = cause;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "Application{" +
                "memberId=" + memberId +
                ", applicationType='" + applicationType + '\'' +
                ", createTime=" + createTime +
                ", checkTime=" + checkTime +
                ", result=" + result +
                ", cause='" + cause + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
