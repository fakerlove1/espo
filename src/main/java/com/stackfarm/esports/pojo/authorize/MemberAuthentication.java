package com.stackfarm.esports.pojo.authorize;

/**
 * @author croton
 * @create 2021/9/5 8:10
 */
public class MemberAuthentication {
    private Long id;
    private String name;
    private String sex;
    private String birth;
    private String project;
    private String type;
    private String level;
    private String club;
    private String clubType;
    private String enrollApplication;
    private String qualificationProtocol;
    private String agreement;
    private String idcard;
    private String photo;
    private String extraEvidence;
    private Long createTime;
    private Long updateTime;
    private Long checkTime;
    private Long effectiveTime;
    private String state;
    private String number;
    private String idcardNumber;
    private String origin;
    private String otherFileZip;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    private String cardType;

    public MemberAuthentication() {
    }

    public MemberAuthentication(Long id, String name, String sex, String birth, String project, String type, String level, String club, String clubType, String enrollApplication, String qualificationProtocol, String agreement, String idcard, String photo, String extraEvidence, Long createTime, Long updateTime, Long checkTime, Long effectiveTime, String state, String number, String idcardNumber, String origin, String otherFileZip, String cardType) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birth = birth;
        this.project = project;
        this.type = type;
        this.level = level;
        this.club = club;
        this.clubType = clubType;
        this.enrollApplication = enrollApplication;
        this.qualificationProtocol = qualificationProtocol;
        this.agreement = agreement;
        this.idcard = idcard;
        this.photo = photo;
        this.extraEvidence = extraEvidence;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.checkTime = checkTime;
        this.effectiveTime = effectiveTime;
        this.state = state;
        this.number = number;
        this.idcardNumber = idcardNumber;
        this.origin = origin;
        this.otherFileZip = otherFileZip;
        this.cardType = cardType;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getClubType() {
        return clubType;
    }

    public void setClubType(String clubType) {
        this.clubType = clubType;
    }

    public String getEnrollApplication() {
        return enrollApplication;
    }

    public void setEnrollApplication(String enrollApplication) {
        this.enrollApplication = enrollApplication;
    }

    public String getQualificationProtocol() {
        return qualificationProtocol;
    }

    public void setQualificationProtocol(String qualificationProtocol) {
        this.qualificationProtocol = qualificationProtocol;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getExtraEvidence() {
        return extraEvidence;
    }

    public void setExtraEvidence(String extraEvidence) {
        this.extraEvidence = extraEvidence;
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

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public Long getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Long effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIdCardNumber() {
        return idcardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idcardNumber = idCardNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getIdcardNumber() {
        return idcardNumber;
    }

    public void setIdcardNumber(String idcardNumber) {
        this.idcardNumber = idcardNumber;
    }

    public String getOtherFileZip() {
        return otherFileZip;
    }

    public void setOtherFileZip(String otherFileZip) {
        this.otherFileZip = otherFileZip;
    }

    @Override
    public String toString() {
        return "MemberAuthentication{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birth='" + birth + '\'' +
                ", project='" + project + '\'' +
                ", type='" + type + '\'' +
                ", level=" + level +
                ", club='" + club + '\'' +
                ", clubType='" + clubType + '\'' +
                ", enrollApplication='" + enrollApplication + '\'' +
                ", qualificationProtocol='" + qualificationProtocol + '\'' +
                ", agreement='" + agreement + '\'' +
                ", idcard='" + idcard + '\'' +
                ", photo='" + photo + '\'' +
                ", extraEvidence='" + extraEvidence + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", checkTime=" + checkTime +
                ", effectiveTime=" + effectiveTime +
                ", state='" + state + '\'' +
                ", number='" + number + '\'' +
                ", idcardNumber='" + idcardNumber + '\'' +
                ", origin='" + origin + '\'' +
                ", otherFileZip='" + otherFileZip + '\'' +
                ", cardType='" + cardType + '\'' +
                '}';
    }
}
