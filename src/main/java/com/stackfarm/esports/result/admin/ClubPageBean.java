package com.stackfarm.esports.result.admin;

import java.util.List;

/**
 * @author croton
 * @create 2021/4/7 17:55
 */
public class ClubPageBean {
    private List<ClubBean> clubBeans;
    private Integer pageCount;
    private Integer userCount;

    public ClubPageBean(List<ClubBean> clubBeans, Integer pageCount, Integer userCount) {
        this.clubBeans = clubBeans;
        this.pageCount = pageCount;
        this.userCount = userCount;
    }

    public List<ClubBean> getClubBeans() {
        return clubBeans;
    }

    public void setClubBeans(List<ClubBean> clubBeans) {
        this.clubBeans = clubBeans;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}
