package com.stackfarm.esports.pojo.qrcode;

/**
 * @author croton
 * @create 2021/6/16 19:40
 */
public class QRCode {
    private Long id;
    private Long userId;
    private Long activityId;
    private String qrCode;
    private Boolean isAvailable;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public QRCode(Long id, Long userId, Long activityId, String qrCode, Boolean isAvailable) {
        this.id = id;
        this.userId = userId;
        this.activityId = activityId;
        this.qrCode = qrCode;
        this.isAvailable = isAvailable;
    }

    public QRCode() {
    }
}
