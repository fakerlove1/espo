package com.stackfarm.esports.result.user;

import com.stackfarm.esports.pojo.user.UserExtensionOrganization;

import java.util.List;

/**
 * @author croton
 * @create 2021/4/6 10:52
 */
public class UserCheckBean {

    private Long id;

    private String username;

    private String nickname;

    private List<String> roles;

    private Boolean state;

    private String cause;

    private UserExtensionOrganization userExtensionOrganization;

    private String profileUrl;

    public UserCheckBean() {
    }

    public UserCheckBean(Long id, String username, String nickname, List<String> roles, Boolean state, String cause, UserExtensionOrganization userExtensionOrganization, String profileUrl) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
        this.state = state;
        this.cause = cause;
        this.userExtensionOrganization = userExtensionOrganization;
        this.profileUrl = profileUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public UserExtensionOrganization getUserExtensionOrganization() {
        return userExtensionOrganization;
    }

    public void setUserExtensionOrganization(UserExtensionOrganization userExtensionOrganization) {
        this.userExtensionOrganization = userExtensionOrganization;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public String toString() {
        return "UserCheckBean{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", roles=" + roles +
                ", state=" + state +
                ", cause='" + cause + '\'' +
                ", userExtensionOrganization=" + userExtensionOrganization +
                ", profileUrl='" + profileUrl + '\'' +
                '}';
    }
}
