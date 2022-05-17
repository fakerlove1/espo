package com.stackfarm.esports.pojo.user;

/**
 * @author croton
 * @create 2021/4/1 12:52
 */
public class SystemRole {
    private Long id;
    private String name;
    private String desc;
    private Boolean isAvailable;

    public SystemRole() {
    }

    public SystemRole(Long id, String name, String desc, Boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean is_available) {
        this.isAvailable = is_available;
    }

    @Override
    public String toString() {
        return "SystemRole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", is_available=" + isAvailable +
                '}';
    }
}
