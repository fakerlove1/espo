package com.stackfarm.esports.result.user;

import com.stackfarm.esports.pojo.user.UserActivitiesInfo;
import com.stackfarm.esports.pojo.user.UserExtensionOrganization;
import com.stackfarm.esports.pojo.user.UserExtensionPerson;
import com.stackfarm.esports.pojo.user.UserInformation;

/**
 * @author croton
 * @create 2021/4/5 16:03
 */
public class UserInfoBean {
    private String username;
    private UserInformation userInformation;
    private UserExtensionPerson userExtensionPerson;
    private UserActivitiesInfo userActivitiesInfo;
    private UserExtensionOrganization userExtensionOrganization;
    private DarkroomBean darkroomBean;

    public UserInfoBean() {
    }

    public UserInfoBean(String username, UserInformation userInformation, UserExtensionPerson userExtensionPerson, UserActivitiesInfo userActivitiesInfo, UserExtensionOrganization userExtensionOrganization, DarkroomBean darkroomBean) {
        this.username = username;
        this.userInformation = userInformation;
        this.userExtensionPerson = userExtensionPerson;
        this.userActivitiesInfo = userActivitiesInfo;
        this.userExtensionOrganization = userExtensionOrganization;
        this.darkroomBean = darkroomBean;
    }

    public DarkroomBean getDarkroomBean() {
        return darkroomBean;
    }

    public void setDarkroomBean(DarkroomBean darkroomBean) {
        this.darkroomBean = darkroomBean;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    public UserExtensionPerson getUserExtensionPerson() {
        return userExtensionPerson;
    }

    public void setUserExtensionPerson(UserExtensionPerson userExtensionPerson) {
        this.userExtensionPerson = userExtensionPerson;
    }

    public UserActivitiesInfo getUserActivitiesInfo() {
        return userActivitiesInfo;
    }

    public void setUserActivitiesInfo(UserActivitiesInfo userActivitiesInfo) {
        this.userActivitiesInfo = userActivitiesInfo;
    }

    public UserExtensionOrganization getUserExtensionOrganization() {
        return userExtensionOrganization;
    }

    public void setUserExtensionOrganization(UserExtensionOrganization userExtensionOrganization) {
        this.userExtensionOrganization = userExtensionOrganization;
    }


}
