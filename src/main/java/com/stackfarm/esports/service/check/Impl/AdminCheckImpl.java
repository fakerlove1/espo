package com.stackfarm.esports.service.check.Impl;

import com.stackfarm.esports.dao.activity.ActivityDao;
import com.stackfarm.esports.dao.activity.ActivityExtensionDao;
import com.stackfarm.esports.dao.activity.ActivityUnexpectDao;
import com.stackfarm.esports.dao.user.*;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.activity.Activity;
import com.stackfarm.esports.pojo.activity.ActivityExtension;
import com.stackfarm.esports.pojo.activity.ActivityUnexpect;
import com.stackfarm.esports.pojo.user.*;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.result.activity.ActivityCheckBean;
import com.stackfarm.esports.result.activity.ActivityPageBean;
import com.stackfarm.esports.result.activity.AdminActivityBean;
import com.stackfarm.esports.result.user.UserCheckBean;
import com.stackfarm.esports.pojo.user.UserDarkroom;
import com.stackfarm.esports.service.activity.ActivityService;
import com.stackfarm.esports.service.check.AdminCheckService;
import com.stackfarm.esports.system.ActivityStateConstant;
import com.stackfarm.esports.system.UserActivityStateConstant;
import com.stackfarm.esports.system.UserRolesConstant;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.EmailUtils;
import com.stackfarm.esports.utils.FileUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.util.*;

/**
 * @author croton
 * @create 2021/4/3 20:40
 */
@Service
@Transactional
public class AdminCheckImpl implements AdminCheckService {

    @Autowired
    private SystemUserDao systemUserDao;
    @Autowired
    private SystemUserRolesDao systemUserRolesDao;
    @Autowired
    private UserExtensionPersonDao userExtensionPersonDao;
    @Autowired
    private UserExtensionOrganizationDao userExtensionOrganizationDao;
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private UserActivitiesInfoDao userActivitiesInfoDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityUnexpectDao activityUnexpectDao;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserActivityDao userActivityDao;
    @Autowired
    private ActivityExtensionDao activityExtensionDao;
    @Autowired
    private SystemRoleDao systemRoleDao;
    @Autowired
    private UserDarkroomDao userDarkroomDao;



    /**
     * ????????????
     * @param userId
     * @param isPassed
     * @param cause
     * @return
     */
    @Override
    public ResultBean<Void> checkUser(Long userId, Boolean isPassed, String cause) throws TencentCloudSDKException {
        ResultBean<Void> result = new ResultBean<>();
        SystemUser user = systemUserDao.selectById(userId);
        UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(userId);
        if (isPassed) {
            user.setState(true);
            //???????????????
            Long type = userExtensionOrganization.getType();
            user.setRole(type.toString()+";");
            List<SystemUserRoles> userRoles = systemUserRolesDao.selectListByUserId(userId);
            SystemUserRoles userRole = userRoles.get(0);
            userRole.setRoleId(type);
            userRole.setUpdatedTime(System.currentTimeMillis());
            user.setUpdateTime(System.currentTimeMillis());
            systemUserDao.update(user);
            systemUserRolesDao.update(userRole);
            //????????????????????????
            UserInformation userInformation = new UserInformation();
            userInformation.setUserId(userId);
            userInformation.setCreateTime(System.currentTimeMillis());
            userInformation.setUpdateTime(System.currentTimeMillis());
            userInformationDao.insert(userInformation);
            //????????????-??????????????????
            UserActivitiesInfo userActivitiesInfo = new UserActivitiesInfo();
            userActivitiesInfo.setUserId(userId);
            userActivitiesInfo.setPoint(0);
            userActivitiesInfo.setTimes(0);
            userActivitiesInfo.setSuccessTimes(0);
            userActivitiesInfoDao.insert(userActivitiesInfo);
            BaseUtils.sendAccessMail(userExtensionOrganizationDao.selectByUserId(userId).getEmail(), user.getUsername());
        } else {
            systemUserDao.realDeleteByUserId(userId);
            systemUserRolesDao.realDeleteByUserId(userId);
            userExtensionOrganizationDao.deleteByUserId(userId);
            FileUtils.deleteFile(Paths.get(userExtensionOrganization.getProfile()));
            BaseUtils.sendRefuseMail(userExtensionOrganization.getEmail(), cause);
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("???????????????");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ?????????????????????????????????
     * @return
     */
    @Override
    public ResultBean<List<UserCheckBean>> getUserApplications(String username) {
        ResultBean<List<UserCheckBean>> result = new ResultBean<>();
        List<SystemUser> systemUsers = new ArrayList<>();
        if (username == null) {
            systemUsers = systemUserDao.selectByState(false);
        } else if (username != null) {
            systemUsers = systemUserDao.selectByStateAndUsername(false, username);
        }
        List<UserCheckBean> userCheckBeans = new ArrayList<>();
        for (SystemUser systemUser : systemUsers) {
            List<String> roles = BaseUtils.getListFromString(systemUser.getRole());
            List<String> rolesName = new ArrayList<>();
            UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(systemUser.getId());
            String profileUrl = FileUtils.hidePath(userExtensionOrganization.getProfile());
            for (String role : roles) {
                rolesName.add(systemRoleDao.selectById(Long.parseLong(role)).getName());
            }
            UserCheckBean userCheckBean = new UserCheckBean(systemUser.getId(), systemUser.getUsername(),
                    systemUser.getNickname(), rolesName, systemUser.getState(), systemUser.getCause(), userExtensionOrganization, profileUrl);
            userCheckBeans.add(userCheckBean);
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("???????????????");
        result.setData(userCheckBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ????????????
     * @param actId ???????????????id
     * @param isPassed ????????????
     * @param cause ??????????????????
     * @param level ??????
     * @return
     */
    @Override
    public ResultBean<Void> checkActivities(Long actId, Boolean isPassed, String cause, Integer level) throws TencentCloudSDKException {
        ResultBean<Void> result = new ResultBean<>();
        Activity activity = activityDao.selectById(actId);
        if(isPassed) {
            //??????????????????--->activity???state????????????ACCESS
            activity.setState(ActivityStateConstant.ACCESS);
            if (level != null) {
                activity.setLevel(level);
            } else {
                activity.setLevel(5);
            }
            BaseUtils.sendSuccessMail(userExtensionOrganizationDao.selectByUserId(activity.getLauncherId()).getEmail(), activity.getName());
        } else {
            //???????????????--->activity???state????????????FAILED,??????????????????activity_unexpect
            activity.setState(ActivityStateConstant.FAILED);
            ActivityUnexpect activityUnexpect = new ActivityUnexpect();
            activityUnexpect.setUserId(activity.getLauncherId());
            activityUnexpect.setActivityId(actId);
            activityUnexpect.setMessage(cause);
            activityUnexpectDao.insert(activityUnexpect);
        }
        activityDao.update(activity);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ??????????????????????????????
     * @return
     */
    @Override
    public ResultBean<List<ActivityCheckBean>> getActivityApplications() throws UnhandledException {
        ResultBean<List<ActivityCheckBean>> result = new ResultBean<>();
        List<Activity> activities1 = activityDao.selectByState("CHECKING");
        List<ActivityCheckBean> activities = new LinkedList<>();
        if(activities1.size() != 0) {
            for(int i = 0; i < activities1.size(); i++) {
                Activity activity = activities1.get(i);
                ActivityExtension activityExtension = activityExtensionDao.selectByActId(activity.getId());
                String poster = activityExtension.getPoster();
                String picture = activityExtension.getPicture();
                String plan = activityExtension.getPlan();
                if (poster == null || "".equals(poster)) {
                    throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "????????????????????????",
                            BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
                }
                String pictureUrl = FileUtils.hidePaths(picture);
                String businessPlanUrl = FileUtils.hidePath(plan);
                String posterUrl = FileUtils.hidePath(poster);
                String zipUuid = posterUrl + "_" + pictureUrl + "_" + businessPlanUrl + "_";
                ActivityCheckBean activityCheckBean = new ActivityCheckBean(activity, activityExtension, zipUuid);
                activities.add(activityCheckBean);
            }
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(activities);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }


    /**
     * ???????????????????????????
     * @param actId ??????id
     * @param isPassed ????????????
     * @param cause ???????????????
     * @return
     */
    @Override
    public ResultBean<Void> checkCancelActivity(Long actId, Boolean isPassed, String cause) throws TencentCloudSDKException {
        ResultBean<Void> result = new ResultBean<>();
        Activity activity = activityDao.selectById(actId);
        if(isPassed) {
            //??????????????????????????????--->activity???state??????CANCELED
            //???????????????????????????????????????????????????????????????????????????
            activity.setState(ActivityStateConstant.CANCELED);
            //user_activity????????????????????????????????????state????????????????????????
            List<UserActivity> userActivityList = userActivityDao.selectByActId(actId);
            if(userActivityList.size() != 0) {
                for(int i = 0 ; i < userActivityList.size(); i++) {
                    userActivityList.get(i).setState(UserActivityStateConstant.ACTIVITY_DELETED);
                    userActivityDao.update(userActivityList.get(i));
                }
            }
        } else {
            //???????????????????????????--->?????????????????????????????????????????????????????????activity_unexpect
            ActivityUnexpect activityUnexpect = new ActivityUnexpect();
            activityUnexpect.setActivityId(actId);
            activityUnexpect.setMessage(cause);
            activityUnexpect.setUserId(activity.getLauncherId());
            activityUnexpectDao.insert(activityUnexpect);

            //??????????????????????????????????????????????????????????????????/?????????????????????????????????????????????????????????
            if(activity.getEnrollBeginTime() <= System.currentTimeMillis()
                    && activity.getEnrollEndTime() > System.currentTimeMillis()) {
                //?????????
                activity.setState(ActivityStateConstant.ENROLLING);
            } else if(activity.getEnrollEndTime() <= System.currentTimeMillis()
                    && activity.getBeginTime() > System.currentTimeMillis()) {
                //????????????
                activity.setState(ActivityStateConstant.ENROLL_CLOSED);
            } else if(activity.getBeginTime() <= System.currentTimeMillis()
                    && activity.getEndTime() > System.currentTimeMillis()) {
                //???????????????
                activity.setState(ActivityStateConstant.HOLDING);
            } else {
                //?????????????????????????????????
                activity.setState(ActivityStateConstant.ACCESS);
            }
            Long oId = activity.getLauncherId();
            String email = userExtensionOrganizationDao.selectByUserId(oId).getEmail();
            BaseUtils.sendCancelNotPassMsgByEmail(email, activity.getName(), cause);
        }
        activityDao.update(activity);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ???????????????????????????
     * @return
     */
    @Override
    public ResultBean<ActivityPageBean> getAllActivities(Integer page, Integer number, String name) {
        ResultBean<ActivityPageBean> result = new ResultBean<>();

        String newName = "";
        if(name != null) {
            newName = name;
        }

        List<Activity> activities = activityDao.selectAll((page-1)*number, number, newName);

        List<AdminActivityBean> adminActivityBeans = new ArrayList<>();
        //????????????????????????
        if(activities.size() != 0) {
            for(int i = 0; i < activities.size(); i++) {
                activityService.updateActState(activities.get(i));
            }
        }

        for (Activity activity : activities) {
            ActivityExtension activityExtension = activityExtensionDao.selectByActId(activity.getId());
            String posterUrl = FileUtils.hidePath(activityExtension.getPoster());
            activityExtension.setPoster(posterUrl);
            String pictureUrl = FileUtils.hidePaths(activityExtension.getPicture());
            activityExtension.setPicture(pictureUrl);
            String businessPlanUrl = FileUtils.hidePath(activityExtension.getPlan());
            activityExtension.setPlan(businessPlanUrl);
            StringBuffer zipUrl = new StringBuffer(posterUrl + "_");
            zipUrl.append(pictureUrl + "_");
            zipUrl.append(businessPlanUrl + "_");

            AdminActivityBean adminActivityBean = new AdminActivityBean(activity, activityExtension,
                    systemUserDao.selectById(activity.getLauncherId()).getUsername(), zipUrl.toString());
            adminActivityBeans.add(adminActivityBean);
        }

        ActivityPageBean activityPageBean = new ActivityPageBean();
        if(activities.size() != 0) {
            activityPageBean.setAdminActivityBeanList(adminActivityBeans);
            Integer pageCount = (activityDao.selectCount() + number - 1) / number;
            activityPageBean.setPageCount(pageCount);
            activityPageBean.setActivityCount(activityDao.selectCount());
        } else {
            activityPageBean.setAdminActivityBeanList(null);
            activityPageBean.setPageCount(0);
            activityPageBean.setActivityCount(0);
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(activityPageBean);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ?????????????????????????????????
     * @return
     */
    @Override
    public ResultBean<?> getAllCancelActivity() {
        ResultBean<List<Activity>> result = new ResultBean<>();
        List<Activity> activities = activityDao.selectByState(ActivityStateConstant.CANCELING);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(activities);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ???????????????/????????????
     * @param userId ??????id
     * @param isBlacklist ????????????
     * @param time ??????????????????
     * @return
     */
    @Override
    public ResultBean<Void> blackListUser(Long userId, Boolean isBlacklist, Long time, String cause) throws TencentCloudSDKException {
        ResultBean<Void> result = new ResultBean<>();
        SystemUser user = systemUserDao.selectById(userId);
        String role = user.getRole();
        String email = "";
        if (role.charAt(0) == UserRolesConstant.CLUB_USER_NUM.toString().charAt(0) || role.charAt(0) == UserRolesConstant.ENTERPRISE_USER_NUM.toString().charAt(0)) {
            //????????????
            email = userExtensionOrganizationDao.selectByUserId(userId).getEmail();
        } else if (role.charAt(0) == UserRolesConstant.PERSON_USER_NUM.toString().charAt(0)) {
            //??????
            email = userExtensionPersonDao.selectByUserId(userId).getEmail();
        } else {
            email = "";
        }
        if ("".equals(email)) {
            result.setMsg("??????????????????????????????????????????");
            result.setStatus(HttpStatus.OK.value());
            result.setData(null);
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }
        if(isBlacklist) {
            //?????????
            if(userDarkroomDao.selectByUserId(userId) != null){
                userDarkroomDao.deleteByUserId(userId);
            }
            UserDarkroom userDarkroom = new UserDarkroom();
            userDarkroom.setUserId(userId);
            userDarkroom.setTime(time);
            userDarkroom.setCreateTime(System.currentTimeMillis());
            userDarkroom.setCause(cause);
            userDarkroomDao.insert(userDarkroom);
            BaseUtils.sendDarkroomMsgByEmail(email, user.getUsername(), time/1000/60/60+"", cause);

            result.setMsg("????????????");
        } else {
            //?????????
            userDarkroomDao.deleteByUserId(userId);
            result.setMsg("????????????");
        }
        result.setStatus(HttpStatus.OK.value());
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

}
