package com.stackfarm.esports.result.authorize;

/**
 * @author croton
 * @create 2021/10/21 14:48
 */
public class AnnualAuthenticationBean {
    private String club;
    private String state;
    private String fileUrl;

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
