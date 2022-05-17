package com.stackfarm.esports.result.admin;

import java.util.List;

/**
 * @author croton
 * @create 2021/4/7 17:57
 */
public class GroupPageBean {
    private List<GroupBean> groupBean;
    private Integer pageCount;
    private Integer userCount;

    public GroupPageBean(List<GroupBean> groupBean, Integer pageCount, Integer userCount) {
        this.groupBean = groupBean;
        this.pageCount = pageCount;
        this.userCount = userCount;
    }

    public List<GroupBean> getGroupBean() {
        return groupBean;
    }

    public void setGroupBean(List<GroupBean> groupBean) {
        this.groupBean = groupBean;
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
