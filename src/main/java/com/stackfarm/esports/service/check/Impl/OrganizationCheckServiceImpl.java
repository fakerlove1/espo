package com.stackfarm.esports.service.check.Impl;

import com.stackfarm.esports.dao.activity.ActivityDao;
import com.stackfarm.esports.dao.activity.ActivityExtensionDao;
import com.stackfarm.esports.dao.qrcode.QRCodeDao;
import com.stackfarm.esports.dao.sign.UserSignDao;
import com.stackfarm.esports.dao.user.*;
import com.stackfarm.esports.pojo.activity.Activity;
import com.stackfarm.esports.pojo.qrcode.QRCode;
import com.stackfarm.esports.pojo.sign.UserSign;
import com.stackfarm.esports.pojo.user.*;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.result.user.UserGetBean;
import com.stackfarm.esports.service.check.OrganizationCheckService;
import com.stackfarm.esports.system.QRCodeConstant;
import com.stackfarm.esports.system.UserActivityStateConstant;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.FileUtils;
import com.stackfarm.esports.utils.IDCardParseUtils;
import com.stackfarm.esports.utils.QRCodeUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author xiaohuang
 * @create 2021/4/3 17:39
 */
@Service
@Transactional
public class OrganizationCheckServiceImpl implements OrganizationCheckService {

    private final UserActivityDao userActivityDao;

    private final SystemUserDao systemUserDao;

    private final ActivityDao activityDao;

    private final SystemRoleDao systemRoleDao;

    private final ActivityExtensionDao activityExtensionDao;

    private final UserInformationDao userInformationDao;

    private final UserActivitiesInfoDao userActivitiesInfoDao;

    private final UserActivityCompleteDao userActivityCompleteDao;

    private final UserExtensionOrganizationDao userExtensionOrganizationDao;

    private final UserExtensionPersonDao userExtensionPersonDao;

    private final QRCodeDao qrCodeDao;

    private final UserSignDao userSignDao;


    public OrganizationCheckServiceImpl(@Autowired UserActivityDao userActivityDao, @Autowired SystemUserDao systemUserDao,
                                        @Autowired ActivityDao activityDao, @Autowired SystemRoleDao systemRoleDao,
                                        @Autowired ActivityExtensionDao activityExtensionDao, @Autowired UserInformationDao userInformationDao,
                                        @Autowired UserActivitiesInfoDao userActivitiesInfoDao, @Autowired UserActivityCompleteDao userActivityCompleteDao,
                                        @Autowired UserExtensionOrganizationDao userExtensionOrganizationDao, @Autowired UserExtensionPersonDao userExtensionPersonDao,
                                        @Autowired QRCodeDao qrCodeDao, @Autowired UserSignDao userSignDao) {
        this.userActivityDao = userActivityDao;
        this.systemUserDao = systemUserDao;
        this.activityDao = activityDao;
        this.systemRoleDao = systemRoleDao;
        this.activityExtensionDao = activityExtensionDao;
        this.userInformationDao = userInformationDao;
        this.userActivitiesInfoDao = userActivitiesInfoDao;
        this.userActivityCompleteDao = userActivityCompleteDao;
        this.userExtensionOrganizationDao = userExtensionOrganizationDao;
        this.userExtensionPersonDao = userExtensionPersonDao;
        this.qrCodeDao = qrCodeDao;
        this.userSignDao = userSignDao;
    }

    /**
     *审核用户报名活动
     * @param actId 活动id
     * @param userId 用户id
     * @param isPassed 是否通过
     * @param cause 拒绝原因（可为空）
     * @return
     */
    @Override
    public ResultBean<Void> checkUserActivity(Long userId, Long actId, Boolean isPassed, String cause) throws TencentCloudSDKException {
        ResultBean<Void> result = new ResultBean<>();
        Activity activity = activityDao.selectById(actId);
        UserActivity userActivity = userActivityDao.selectByUserIdAndActId(userId, actId);
        SystemUser currentUser = systemUserDao.selectById(userId);
        if(isPassed) {
            //如果审核通过--->user_activity的state改变，activity的参赛总人数与参赛者们id变化，
            userActivity.setState(UserActivityStateConstant.PASSED);
            userActivityDao.update(userActivity);
            activity.setSizeOfPeople(activity.getSizeOfPeople() + userActivity.getMemberCount());
            String newPartIds;
            if(activity.getParticipateIds() != null) {
                newPartIds = activity.getParticipateIds() + currentUser.getId() + ";";
            } else {
                newPartIds = currentUser.getId() + ";";
            }
            activity.setParticipateIds(newPartIds);
            activityDao.update(activity);

            UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
            userActivitiesInfo.setTimes(userActivitiesInfo.getTimes() + 1);
            userActivitiesInfoDao.update(userActivitiesInfo);
            SystemUser user = systemUserDao.selectById(userId);
            if ("1;".equals(user.getRole())) {
                BaseUtils.sendAccessMsg(userExtensionPersonDao.selectByUserId(userId).getPhoneNumber().toString(), activity.getName());
            } else if ("2;".equals(user.getRole()) || "3;".equals(user.getRole())) {
                BaseUtils.sendAccessOMail(userExtensionOrganizationDao.selectByUserId(userId).getEmail(), activity.getName());
            }
            //如果是個人用戶
            if (user.getRole().equals("1;")) {
                //添加二维码到数据库
                String content = "/sign/" + userId + "_" + actId;
                String path = QRCodeConstant.QRCODE_PATH + File.separator + user.getUsername();
                QRCodeUtils.generateQRCodeImg(QRCodeConstant.QRCODE_PATH, content, user.getUsername()+"-"+activity.getName());
                QRCode qrCode = new QRCode();
                qrCode.setActivityId(actId);
                qrCode.setUserId(userId);
                qrCode.setQrCode(path + File.separator + user.getUsername()+"-"+activity.getName() + ".png");
                qrCode.setAvailable(true);
                qrCodeDao.insert(qrCode);
                //添加一條用戶-簽到信息
                UserSign userSign = new UserSign();
                userSign.setUserId(userId);
                userSign.setActId(actId);
                userSign.setState(false);
                userSignDao.update(userSign);
            }
        } else {
            //审核不通过--->user_activity的state变成拒绝，cause中添加原因,后期可以考虑拒绝审核给用户发信息
            userActivity.setState(UserActivityStateConstant.TURN_DOWN);
            userActivity.setCause(cause);
            userActivityDao.update(userActivity);
        }

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("审核成功");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 获取待审核/审核过用户活动列表
     * @param actId 活动id
     * @return
     */
    @Override
    public ResultBean<List<UserGetBean>> getUserActivity(Long actId) {
        ResultBean<List<UserGetBean>> result = new ResultBean<>();
        List<UserActivity> userActivities = userActivityDao.selectByActIdAndState(actId, UserActivityStateConstant.UNDER_REVIEW);
        List<UserActivity> userActivities2 = userActivityDao.selectByActIdAndState(actId, UserActivityStateConstant.PASSED);
        List<UserActivity> userActivities3 = userActivityDao.selectByActIdAndState(actId, UserActivityStateConstant.COMPLETED);
        List<UserActivity> userActivities4 = userActivityDao.selectByActIdAndState(actId, UserActivityStateConstant.UNCOMPLETED);
        List<UserGetBean> userActivityList = new LinkedList<>();
        if(userActivities.size() != 0) {
            for (int i = 0; i < userActivities.size(); i++) {
                UserActivity userActivity = userActivities.get(i);
                Long userId = userActivity.getUserId();
                UserInformation userInformation = userInformationDao.selectByUserId(userId);
                //如果是企业年龄默认为0
                SystemUser user = systemUserDao.selectById(userId);
                List<String> roles = BaseUtils.getListFromString(user.getRole());
                SystemRole role = systemRoleDao.selectById(Long.parseLong(roles.get(0)));
                Integer age;
                String realName = "";
                if(role.getId() == 2 || role.getId() == 3) {
                    age = 0;
                    realName = userExtensionOrganizationDao.selectByUserId(userId).getTeamName();
                } else {
                    age = IDCardParseUtils.IDCardNoToAge(userInformation.getCardId());
                    realName = userInformation.getName();
                }
                UserActivity currentUserActivity = userActivityDao.selectByUserIdAndActId(userId, actId);
                String completion = "";
                if(currentUserActivity.getState().equals(UserActivityStateConstant.COMPLETED)) {
                    completion = "已完成";
                } else {
                    completion = "未完成";
                }
                UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
                //获取个人简历url
                String profileUrl = "无";
                String phoneNumber = "无";
                if(role.getId() == 1) {
                    UserExtensionPerson userExtensionPerson = userExtensionPersonDao.selectByUserId(userId);
                    if(userExtensionPerson.getProfile() != null) {
                        profileUrl = FileUtils.hidePath(userExtensionPerson.getProfile());
                    }
                    if(userExtensionPerson.getPhoneNumber() != 0) {
                        phoneNumber = String.valueOf(userExtensionPerson.getPhoneNumber());
                    }
                } else {
                    UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(userId);
                    if(userExtensionOrganization != null) {
                        phoneNumber = String.valueOf(userExtensionOrganization.getTel());
                    }
                }
                UserGetBean bean = new UserGetBean(userActivity, age, role.getName(), userActivitiesInfo.getSuccessTimes(),completion, realName, profileUrl, phoneNumber, userActivitiesInfo.getTimes());
                userActivityList.add(bean);
            }
        }
        if(userActivities2.size() != 0) {
            for (int i = 0; i < userActivities2.size(); i++) {
                UserActivity userActivity = userActivities2.get(i);
                Long userId = userActivity.getUserId();
                UserInformation userInformation = userInformationDao.selectByUserId(userId);
                //如果是企业年龄默认为0
                SystemUser user = systemUserDao.selectById(userId);
                List<String> roles = BaseUtils.getListFromString(user.getRole());
                SystemRole role = systemRoleDao.selectById(Long.parseLong(roles.get(0)));
                Integer age;
                String realName = "";
                if(role.getId() == 2 || role.getId() == 3) {
                    age = 0;
                    realName = userExtensionOrganizationDao.selectByUserId(userId).getTeamName();
                } else {
                    age = IDCardParseUtils.IDCardNoToAge(userInformation.getCardId());
                    realName = userInformation.getName();
                }
                UserActivity currentUserActivity = userActivityDao.selectByUserIdAndActId(userId, actId);
                String completion = "";
                if(currentUserActivity.getState().equals(UserActivityStateConstant.COMPLETED)) {
                    completion = "已完成";
                } else {
                    completion = "未完成";
                }
                UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
                //获取个人简历url
                String profileUrl = "无";
                String phoneNumber = "无";
                if(role.getId() == 1) {
                    UserExtensionPerson userExtensionPerson = userExtensionPersonDao.selectByUserId(userId);
                    if(userExtensionPerson.getProfile() != null) {
                        profileUrl = FileUtils.hidePath(userExtensionPerson.getProfile());
                    }
                    if(userExtensionPerson.getPhoneNumber() != 0) {
                        phoneNumber = String.valueOf(userExtensionPerson.getPhoneNumber());
                    }
                } else {
                    UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(userId);
                    if(userExtensionOrganization != null) {
                        phoneNumber = String.valueOf(userExtensionOrganization.getTel());
                    }
                }
                UserGetBean bean = new UserGetBean(userActivity, age, role.getName(), userActivitiesInfo.getSuccessTimes(),completion, realName, profileUrl, phoneNumber,userActivitiesInfo.getTimes());
                userActivityList.add(bean);
            }
        }
        if(userActivities3.size() != 0) {
            for (int i = 0; i < userActivities3.size(); i++) {
                UserActivity userActivity = userActivities3.get(i);
                Long userId = userActivity.getUserId();
                UserInformation userInformation = userInformationDao.selectByUserId(userId);
                //如果是企业年龄默认为0
                SystemUser user = systemUserDao.selectById(userId);
                List<String> roles = BaseUtils.getListFromString(user.getRole());
                SystemRole role = systemRoleDao.selectById(Long.parseLong(roles.get(0)));
                Integer age;
                String realName = "";
                if(role.getId() == 2 || role.getId() == 3) {
                    age = 0;
                    realName = userExtensionOrganizationDao.selectByUserId(userId).getTeamName();
                } else {
                    age = IDCardParseUtils.IDCardNoToAge(userInformation.getCardId());
                    realName = userInformation.getName();
                }
                UserActivity currentUserActivity = userActivityDao.selectByUserIdAndActId(userId, actId);
                String completion = "";
                if(currentUserActivity.getState().equals(UserActivityStateConstant.COMPLETED)) {
                    completion = "已完成";
                } else {
                    completion = "未完成";
                }
                UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
                //获取个人简历url
                String profileUrl = "无";
                String phoneNumber = "无";
                if(role.getId() == 1) {
                    UserExtensionPerson userExtensionPerson = userExtensionPersonDao.selectByUserId(userId);
                    if(userExtensionPerson.getProfile() != null) {
                        profileUrl = FileUtils.hidePath(userExtensionPerson.getProfile());
                    }
                    if(userExtensionPerson.getPhoneNumber() != 0) {
                        phoneNumber = String.valueOf(userExtensionPerson.getPhoneNumber());
                    }
                } else {
                    UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(userId);
                    if(userExtensionOrganization != null) {
                        phoneNumber = String.valueOf(userExtensionOrganization.getTel());
                    }
                }
                UserGetBean bean = new UserGetBean(userActivity, age, role.getName(), userActivitiesInfo.getSuccessTimes(),completion, realName, profileUrl, phoneNumber,userActivitiesInfo.getTimes());
                userActivityList.add(bean);
            }
        }
        if(userActivities4.size() != 0) {
            for (int i = 0; i < userActivities4.size(); i++) {
                UserActivity userActivity = userActivities4.get(i);
                Long userId = userActivity.getUserId();
                UserInformation userInformation = userInformationDao.selectByUserId(userId);
                //如果是企业年龄默认为0
                SystemUser user = systemUserDao.selectById(userId);
                List<String> roles = BaseUtils.getListFromString(user.getRole());
                SystemRole role = systemRoleDao.selectById(Long.parseLong(roles.get(0)));
                Integer age;
                String realName = "";
                if(role.getId() == 2 || role.getId() == 3) {
                    age = 0;
                    realName = userExtensionOrganizationDao.selectByUserId(userId).getTeamName();
                } else {
                    age = IDCardParseUtils.IDCardNoToAge(userInformation.getCardId());
                    realName = userInformation.getName();
                }
                UserActivity currentUserActivity = userActivityDao.selectByUserIdAndActId(userId, actId);
                String completion = "";
                if(currentUserActivity.getState().equals(UserActivityStateConstant.COMPLETED)) {
                    completion = "已完成";
                } else {
                    completion = "未完成";
                }
                UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
                //获取个人简历url
                String profileUrl = "无";
                String phoneNumber = "无";
                if(role.getId() == 1) {
                    UserExtensionPerson userExtensionPerson = userExtensionPersonDao.selectByUserId(userId);
                    if(userExtensionPerson.getProfile() != null) {
                        profileUrl = FileUtils.hidePath(userExtensionPerson.getProfile());
                    }
                    if(userExtensionPerson.getPhoneNumber() != 0) {
                        phoneNumber = String.valueOf(userExtensionPerson.getPhoneNumber());
                    }
                } else {
                    UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(userId);
                    if(userExtensionOrganization != null) {
                        phoneNumber = String.valueOf(userExtensionOrganization.getTel());
                    }
                }
                UserGetBean bean = new UserGetBean(userActivity, age, role.getName(), userActivitiesInfo.getSuccessTimes(),completion, realName, profileUrl, phoneNumber,userActivitiesInfo.getTimes());
                userActivityList.add(bean);
            }
        }

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功");
        result.setData(userActivityList);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 审核用户活动情况
     * @param userId 用户id
     * @param actId 活动id
     * @param state 是否完成
     * @param comment 已完成的评价
     * @return
     */
    @Override
    public ResultBean<?> checkUserActivityState(Long userId, Long actId, Boolean state, String comment) {
        ResultBean<Void> result = new ResultBean<>();
        UserActivityComplete userActivityComplete = new UserActivityComplete(userId,actId,state,comment);
        UserActivity userActivity = userActivityDao.selectByUserIdAndActId(userId, actId);
        UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
        if(state) {
            //如果活动已经成功完成-->user_activity中的state变为COMPLETED-->user_activities_info中的成功次数和次数都加1
            userActivity.setState(UserActivityStateConstant.COMPLETED);
            //后期将会加上积分
            userActivitiesInfo.setSuccessTimes(userActivitiesInfo.getSuccessTimes() + 1);
        } else {
            //没有成功完成活动-->user_activity中的state变为COMPLETED-->user_activities_info中的次数加1
            userActivity.setState(UserActivityStateConstant.UNCOMPLETED);
        }
        userActivitiesInfoDao.update(userActivitiesInfo);
        userActivityDao.update(userActivity);
        userActivityCompleteDao.insert(userActivityComplete);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("审核成功");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }


}
