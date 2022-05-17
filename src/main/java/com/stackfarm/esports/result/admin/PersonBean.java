package com.stackfarm.esports.result.admin;

/**
 * @author croton
 * @create 2021/4/7 16:02
 */
public class PersonBean {

    private String username;
    private String name;
    private Integer age;
    private Integer successTimes;
    private Integer point;
    private Boolean isRealName;
    private Long id;
    private Long darkroom;

    public PersonBean(String username, String name, Integer age, Integer successTimes, Integer point, Boolean isRealName, Long id, Long darkroom) {
        this.username = username;
        this.name = name;
        this.age = age;
        this.successTimes = successTimes;
        this.point = point;
        this.isRealName = isRealName;
        this.id = id;
        this.darkroom = darkroom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSuccessTimes() {
        return successTimes;
    }

    public void setSuccessTimes(Integer successTimes) {
        this.successTimes = successTimes;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Boolean getRealName() {
        return isRealName;
    }

    public void setRealName(Boolean realName) {
        isRealName = realName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDarkroom() {
        return darkroom;
    }

    public void setDarkroom(Long darkroom) {
        this.darkroom = darkroom;
    }
}
