package com.stackfarm.esports.result.authorize;

import java.util.List;

/**
 * @author croton
 * @create 2021/9/5 13:56
 */
public class ApplicationBeans {
    private List<ApplicationBean> applications;
    private Integer total;
    private Integer size;
    private Integer page;

    public ApplicationBeans() {
    }

    public ApplicationBeans(List<ApplicationBean> applications, Integer total, Integer size, Integer page) {
        this.applications = applications;
        this.total = total;
        this.size = size;
        this.page = page;
    }

    public List<ApplicationBean> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationBean> applications) {
        this.applications = applications;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
