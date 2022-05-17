package com.stackfarm.esports.result.authorize;

import java.util.List;

/**
 * @author croton
 * @create 2021/11/14 12:57
 */
public class AnnualAuthenticationResultBeans {
    private List<AnnualAuthenticationResultBean> annualAuthenticationResultBeans;
    private Integer total;
    private Integer size;
    private Integer page;

    public List<AnnualAuthenticationResultBean> getAnnualAuthenticationResultBeans() {
        return annualAuthenticationResultBeans;
    }

    public void setAnnualAuthenticationResultBeans(List<AnnualAuthenticationResultBean> annualAuthenticationResultBeans) {
        this.annualAuthenticationResultBeans = annualAuthenticationResultBeans;
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
