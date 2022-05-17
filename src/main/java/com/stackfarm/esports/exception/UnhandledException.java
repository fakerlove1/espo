package com.stackfarm.esports.exception;

import java.util.Date;

/**
 * @author croton
 * @create 2021/3/31 19:31
 */
public class UnhandledException extends Exception {
    /**
     * HTTP状态码
     */
    private Integer httpStatus;

    /**
     * 异常信息
     */
    private String msg;

    /**
     * 异常位置，详见BaseUtil.getRunLocation()方法
     */
    private String location;

    /**
     * 时间戳
     */
    private Date timestamp;

    public UnhandledException() {
    }

    public UnhandledException(Integer httpStatus, String msg, String location, Date timestamp) {
        this.httpStatus = httpStatus;
        this.msg = msg;
        this.location = location;
        this.timestamp = timestamp;
    }



    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UnhandledException{" +
                "httpStatus=" + httpStatus +
                ", msg='" + msg + '\'' +
                ", location='" + location + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
