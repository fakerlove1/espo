package com.stackfarm.esports.result.authorize;

import com.stackfarm.esports.pojo.authorize.SendInformation;

import java.util.List;

/**
 * @author croton
 * @create 2021/9/5 16:54
 */
public class PostBean {
    private List<SendInformation> sendInformations;
    private Integer total;
    private Integer size;
    private Integer page;

    public PostBean() {
    }

    public PostBean(List<SendInformation> sendInformations, Integer total, Integer size, Integer page) {
        this.sendInformations = sendInformations;
        this.total = total;
        this.size = size;
        this.page = page;
    }

    public List<SendInformation> getSendInformations() {
        return sendInformations;
    }

    public void setSendInformations(List<SendInformation> sendInformations) {
        this.sendInformations = sendInformations;
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
