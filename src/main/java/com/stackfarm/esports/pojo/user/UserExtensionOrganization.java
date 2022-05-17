package com.stackfarm.esports.pojo.user;

/**
 * @author croton
 * @create 2021/4/3 13:51
 */
public class UserExtensionOrganization {
    private Long userId;
    private String teamName;
    private Long type;
    private String organizationType;
    private String organizationCode;
    private String businessScope;
    private String email;
    private String tel;
    private String weibo;
    private String officialCode;
    private String website;
    private String alipay;
    private String address;
    private String introduction;
    private String profile;

    public UserExtensionOrganization() {
    }

    public UserExtensionOrganization(Long userId, String teamName, Long type, String organizationType, String organizationCode, String businessScope, String email, String tel, String weibo, String officialCode, String website, String alipay, String address, String introduction, String profile) {
        this.userId = userId;
        this.teamName = teamName;
        this.type = type;
        this.organizationType = organizationType;
        this.organizationCode = organizationCode;
        this.businessScope = businessScope;
        this.email = email;
        this.tel = tel;
        this.weibo = weibo;
        this.officialCode = officialCode;
        this.website = website;
        this.alipay = alipay;
        this.address = address;
        this.introduction = introduction;
        this.profile = profile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getOfficialCode() {
        return officialCode;
    }

    public void setOfficialCode(String officialCode) {
        this.officialCode = officialCode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public String toString() {
        return "UserExtensionOrganization{" +
                "userId=" + userId +
                ", teamName='" + teamName + '\'' +
                ", type='" + type + '\'' +
                ", organizationType='" + organizationType + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", weibo='" + weibo + '\'' +
                ", officialCode='" + officialCode + '\'' +
                ", website='" + website + '\'' +
                ", alipay='" + alipay + '\'' +
                ", address='" + address + '\'' +
                ", introduction='" + introduction + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
