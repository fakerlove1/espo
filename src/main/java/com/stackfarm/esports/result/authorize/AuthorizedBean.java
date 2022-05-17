package com.stackfarm.esports.result.authorize;

import com.stackfarm.esports.pojo.authorize.MemberAuthentication;

/**
 * @author croton
 * @create 2021/9/5 15:20
 */
public class AuthorizedBean {
    private MemberAuthentication member;
    private String enrollApplicationUrl;
    private String qualificationProtocolUrl;
    private String agreementUrl;
    private String idcardUrl;
    private String photoUrl;
    private String extraEvidenceUrl;

    public AuthorizedBean() {
    }

    public AuthorizedBean(MemberAuthentication member, String enrollApplicationUrl, String qualificationProtocolUrl, String agreementUrl, String idcardUrl, String photoUrl, String extraEvidenceUrl) {
        this.member = member;
        this.enrollApplicationUrl = enrollApplicationUrl;
        this.qualificationProtocolUrl = qualificationProtocolUrl;
        this.agreementUrl = agreementUrl;
        this.idcardUrl = idcardUrl;
        this.photoUrl = photoUrl;
        this.extraEvidenceUrl = extraEvidenceUrl;
    }

    public MemberAuthentication getMember() {
        return member;
    }

    public void setMember(MemberAuthentication member) {
        this.member = member;
    }

    public String getEnrollApplicationUrl() {
        return enrollApplicationUrl;
    }

    public void setEnrollApplicationUrl(String enrollApplicationUrl) {
        this.enrollApplicationUrl = enrollApplicationUrl;
    }

    public String getQualificationProtocolUrl() {
        return qualificationProtocolUrl;
    }

    public void setQualificationProtocolUrl(String qualificationProtocolUrl) {
        this.qualificationProtocolUrl = qualificationProtocolUrl;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }

    public String getIdcardUrl() {
        return idcardUrl;
    }

    public void setIdcardUrl(String idcardUrl) {
        this.idcardUrl = idcardUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getExtraEvidenceUrl() {
        return extraEvidenceUrl;
    }

    public void setExtraEvidenceUrl(String extraEvidenceUrl) {
        this.extraEvidenceUrl = extraEvidenceUrl;
    }
}
