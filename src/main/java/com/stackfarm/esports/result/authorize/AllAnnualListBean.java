package com.stackfarm.esports.result.authorize;

import com.stackfarm.esports.pojo.authorize.AnnualInfo;

import java.util.List;

/**
 * @author croton
 * @create 2021/11/14 13:48
 */
public class AllAnnualListBean {
    private List<AnnualInfo> annualInfos;
    private Integer total;
    private Integer size;
    private Integer page;

    public List<AnnualInfo> getAnnualInfos() {
        return annualInfos;
    }

    public void setAnnualInfos(List<AnnualInfo> annualInfos) {
        this.annualInfos = annualInfos;
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
