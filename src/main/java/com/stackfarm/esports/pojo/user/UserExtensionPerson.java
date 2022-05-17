package com.stackfarm.esports.pojo.user;

/**
 * @author croton
 * @create 2021/4/3 13:45
 */
public class UserExtensionPerson {
    private Long userId;
    private Long phoneNumber;
    private String email;
    private Long qq;
    private String wechat;
    private String alipay;
    private String cardId;
    private String preferPosition;
    private String experience;
    private String introduction;
    private String profile;
    private String invitationCode;
    private String education;

    public UserExtensionPerson() {
    }

    public UserExtensionPerson(Long userId, Long phoneNumber, String email, Long qq, String wechat, String alipay, String cardId, String preferPosition, String experience, String introduction, String profile, String invitationCode, String education) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.qq = qq;
        this.wechat = wechat;
        this.alipay = alipay;
        this.cardId = cardId;
        this.preferPosition = preferPosition;
        this.experience = experience;
        this.introduction = introduction;
        this.profile = profile;
        this.invitationCode = invitationCode;
        this.education = education;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPreferPosition() {
        return preferPosition;
    }

    public void setPreferPosition(String preferPosition) {
        this.preferPosition = preferPosition;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @Override
    public String toString() {
        return "UserExtensionPerson{" +
                "userId=" + userId +
                ", phoneNumber=" + phoneNumber +
                ", email='" + email + '\'' +
                ", qq=" + qq +
                ", wechat='" + wechat + '\'' +
                ", alipay='" + alipay + '\'' +
                ", cardId='" + cardId + '\'' +
                ", preferPosition='" + preferPosition + '\'' +
                ", experience='" + experience + '\'' +
                ", introduction='" + introduction + '\'' +
                ", profile='" + profile + '\'' +
                ", invitationCode='" + invitationCode + '\'' +
                ", education='" + education + '\'' +
                '}';
    }
}
