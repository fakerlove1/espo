package com.stackfarm.esports.result.authorize;

import java.util.List;

/**
 * @author croton
 * @create 2021/10/21 14:29
 */
public class AnnualAuthenticationBeans {
    private List<AnnualAuthenticationBean> annualAuthenticationBeanList;
    private Integer total;
    private Integer size;
    private Integer page;
    private String filesUrl;

    public List<AnnualAuthenticationBean> getAnnualAuthenticationBeanList() {
        return annualAuthenticationBeanList;
    }

    public void setAnnualAuthenticationBeanList(List<AnnualAuthenticationBean> annualAuthenticationBeanList) {
        this.annualAuthenticationBeanList = annualAuthenticationBeanList;
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

    public String getFilesUrl() {
        return filesUrl;
    }

    public void setFilesUrl(String filesUrl) {
        this.filesUrl = filesUrl;
    }
}
