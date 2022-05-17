package com.stackfarm.esports.pojo.user;

/**
 * @author croton
 * @create 2021/4/3 13:55
 */
public class UserInformation {
    private Long userId;
    private Long organizationId;
    private String name;
    private Integer age;
    private String sex;
    private String nation;
    private Long birthday;
    private String home;
    private String cardId;
    private Long createTime;
    private Long updateTime;

    public UserInformation() {
    }

    public UserInformation(Long userId, Long organizationId, String name, Integer age, String sex, String nation, Long birthday, String home, String cardId, Long createTime, Long updateTime) {
        this.userId = userId;
        this.organizationId = organizationId;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.nation = nation;
        this.birthday = birthday;
        this.home = home;
        this.cardId = cardId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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
        return "UserInformation{" +
                "userId=" + userId +
                ", organizationId=" + organizationId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", birthday=" + birthday +
                ", home='" + home + '\'' +
                ", cardId='" + cardId + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
