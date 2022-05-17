package com.stackfarm.esports.pojo.user;

/**
 * @author croton
 * @create 2021/4/1 12:54
 */
public class SystemUserRoles {
    private Long id;
    private Long userId;
    private Long roleId;
    private Long createdTime;
    private Long updatedTime;
    private Boolean isAvailable;

    public SystemUserRoles() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "SystemUserRoles{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleId=" + roleId +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
