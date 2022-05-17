package com.stackfarm.esports.result.admin;

import java.util.List;

/**
 * @author croton
 * @create 2021/4/7 17:58
 */
public class PersonPageBean {
    private List<PersonBean> personBean;
    private Integer pageCount;
    private Integer userCount;

    public PersonPageBean(List<PersonBean> personBean, Integer pageCount, Integer userCount) {
        this.personBean = personBean;
        this.pageCount = pageCount;
        this.userCount = userCount;
    }

    public List<PersonBean> getPersonBean() {
        return personBean;
    }

    public void setPersonBean(List<PersonBean> personBean) {
        this.personBean = personBean;
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
