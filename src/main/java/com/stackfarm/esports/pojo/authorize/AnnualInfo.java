package com.stackfarm.esports.pojo.authorize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author croton
 * @create 2021/10/21 13:16
 */

public class AnnualInfo {
    private Integer id;
    private String club;
    private String fullPath;
    private Integer yearNumber;
    private Long createTime;
    private Long updateTime;
    private Integer state;

    public AnnualInfo(Integer id, String club, String fullPath, Integer yearNumber, Long createTime, Long updateTime, Integer state) {
        this.id = id;
        this.club = club;
        this.fullPath = fullPath;
        this.yearNumber = yearNumber;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.state = state;
    }

    public AnnualInfo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Integer getYearNumber() {
        return yearNumber;
    }

    public void setYearNumber(Integer yearNumber) {
        this.yearNumber = yearNumber;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AnnualInfo{" +
                "id=" + id +
                ", club='" + club + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", yearNumber=" + yearNumber +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", state=" + state +
                '}';
    }
}
