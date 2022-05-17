package com.stackfarm.esports.result.user;

/**
 * @author croton
 * @create 2021/8/20 17:44
 */
public class DarkroomBean {
    private Boolean isDarkRoom;
    private String remainTime;

    public DarkroomBean() {
    }

    public DarkroomBean(Boolean isDarkRoom, String remainTime) {
        this.isDarkRoom = isDarkRoom;
        this.remainTime = remainTime;
    }

    public Boolean getDarkRoom() {
        return isDarkRoom;
    }

    public void setDarkRoom(Boolean darkRoom) {
        isDarkRoom = darkRoom;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }
}
