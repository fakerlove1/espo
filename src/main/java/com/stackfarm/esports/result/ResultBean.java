package com.stackfarm.esports.result;

import java.util.Date;

/**
 * @author croton
 * @create 2021/3/31 20:14
 */
public class ResultBean<T> {
    private Integer status;

    private String msg;

    private T data;

    private Long timestamp;

    public ResultBean() {
    }

    public ResultBean(Integer status, String msg, T data, Long timestamp) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
