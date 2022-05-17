package com.stackfarm.esports.result.authorize;

import java.util.List;

/**
 * @author croton
 * @create 2021/9/5 15:21
 */
public class AuthorizedBeans {
    private List<AuthorizedBean> authorizedBeans;
    private Integer total;
    private Integer size;
    private Integer page;

    public AuthorizedBeans() {
    }

    public AuthorizedBeans(List<AuthorizedBean> authorizedBeans, Integer total, Integer size, Integer page) {
        this.authorizedBeans = authorizedBeans;
        this.total = total;
        this.size = size;
        this.page = page;
    }

    public List<AuthorizedBean> getAuthorizedBeans() {
        return authorizedBeans;
    }

    public void setAuthorizedBeans(List<AuthorizedBean> authorizedBeans) {
        this.authorizedBeans = authorizedBeans;
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

    @Override
    public String toString() {
        return "AuthorizedBeans{" +
                "authorizedBeans=" + authorizedBeans +
                ", total=" + total +
                ", size=" + size +
                ", page=" + page +
                '}';
    }
}
