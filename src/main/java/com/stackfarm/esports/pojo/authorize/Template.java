package com.stackfarm.esports.pojo.authorize;

/**
 * @author croton
 * @create 2021/10/18 19:35
 */
public class Template {
    private Integer id;
    private String name;
    private String fullPath;
    private Long createTime;
    private Long updateTime;

    public Template() {
    }

    public Template(Integer id, String name, String fullPath, Long createTime, Long updateTime) {
        this.id = id;
        this.name = name;
        this.fullPath = fullPath;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
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

    @Override
    public String toString() {
        return "Template{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
