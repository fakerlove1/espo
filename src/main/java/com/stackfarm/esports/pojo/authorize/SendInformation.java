package com.stackfarm.esports.pojo.authorize;

/**
 * @author croton
 * @create 2021/9/5 8:35
 */
public class SendInformation {
    private Long id;
    private Long memberId;
    private String receiverName;
    private String phoneNumber;
    private String address;
    private Integer cost;
    private Boolean needReceipt;
    private String cause;
    private String evidence;
    private String trackingNumber;
    private Boolean state;

    public SendInformation() {
    }

    public SendInformation(Long id, Long memberId, String receiverName, String phoneNumber, String address, Integer cost, Boolean needReceipt, String cause, String evidence, String trackingNumber, Boolean state) {
        this.id = id;
        this.memberId = memberId;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.cost = cost;
        this.needReceipt = needReceipt;
        this.cause = cause;
        this.evidence = evidence;
        this.trackingNumber = trackingNumber;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Boolean getNeedReceipt() {
        return needReceipt;
    }

    public void setNeedReceipt(Boolean needReceipt) {
        this.needReceipt = needReceipt;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SendInformation{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", receiverName='" + receiverName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", cost=" + cost +
                ", needReceipt=" + needReceipt +
                ", cause='" + cause + '\'' +
                ", evidence='" + evidence + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", state=" + state +
                '}';
    }
}
