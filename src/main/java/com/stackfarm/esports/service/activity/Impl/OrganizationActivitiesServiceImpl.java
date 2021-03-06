package com.stackfarm.esports.service.activity.Impl;

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
import com.stackfarm.esports.result.activity.*;
import com.stackfarm.esports.service.activity.ActivityService;
import com.stackfarm.esports.service.activity.OrganizationActivitiesService;
import com.stackfarm.esports.system.*;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.FileUtils;
import com.stackfarm.esports.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author xiaohuang
 * @create 2021/4/1 21:10
 */
@Service
@Transactional
@Slf4j
public class OrganizationActivitiesServiceImpl implements OrganizationActivitiesService {

    private final ActivityDao activityDao;

    private final ActivityExtensionDao activityExtensionDao;

    private final SystemUserDao systemUserDao;

    private final UserActivityDao userActivityDao;

    private final ActivityUnexpectDao activityUnexpectDao;

    private final SystemUserRolesDao systemUserRolesDao;

    private final SystemRoleDao systemRoleDao;

    private final ActivityService activityService;

    private final UserInformationDao userInformationDao;

    private final UserDarkroomDao userDarkroomDao;

    private final UserExtensionPersonDao userExtensionPersonDao;

    private final UserExtensionOrganizationDao userExtensionOrganizationDao;

    public OrganizationActivitiesServiceImpl(@Autowired ActivityDao activityDao, @Autowired ActivityExtensionDao activityExtensionDao, @Autowired SystemUserDao systemUserDao, @Autowired UserActivityDao userActivityDao,
                                             @Autowired ActivityUnexpectDao activityUnexpectDao, @Autowired SystemUserRolesDao systemUserRolesDao,
                                             @Autowired SystemRoleDao systemRoleDao, @Autowired ActivityService activityService,
                                             @Autowired UserInformationDao userInformationDao, @Autowired UserDarkroomDao userDarkroomDao,
                                             @Autowired UserExtensionPersonDao userExtensionPersonDao,
                                             @Autowired UserExtensionOrganizationDao userExtensionOrganizationDao) {
        this.activityDao = activityDao;
        this.activityExtensionDao = activityExtensionDao;
        this.systemUserDao = systemUserDao;
        this.userActivityDao = userActivityDao;
        this.activityUnexpectDao = activityUnexpectDao;
        this.systemUserRolesDao = systemUserRolesDao;
        this.systemRoleDao = systemRoleDao;
        this.activityService = activityService;
        this.userInformationDao = userInformationDao;
        this.userDarkroomDao = userDarkroomDao;
        this.userExtensionPersonDao = userExtensionPersonDao;
        this.userExtensionOrganizationDao = userExtensionOrganizationDao;
    }


    /**
     * ?????????????????????/??????????????????
     *
     * @param actName         ?????????
     * @param actType         ?????????????????????????????????
     * @param enrollBeginTime ??????????????????
     * @param enrollEndTime   ??????????????????
     * @param holdBeginTime   ??????????????????
     * @param holdEndTime     ??????????????????
     * @param staffTypes      ??????????????????
     * @param staffCount      ????????????????????????
     * @param jobRequirement  ??????????????????
     * @param cost            ????????????
     * @param reward          ????????????
     * @param actAddress      ????????????
     * @param detailAddress   ????????????
     * @param contactWay      ????????????????????????
     * @param actScope        ??????????????????
     * @param actInformation  ????????????
     * @param poster          ????????????
     * @param pictures        ????????????
     * @param actPlan         ???????????????
     * @return
     */
    @Override
    public ResultBean<Void> launchActivity(String token, String actName, String actType, Long enrollBeginTime,
                                           Long enrollEndTime, Long holdBeginTime, Long holdEndTime,
                                           String staffTypes, String staffCount, String jobRequirement,
                                           BigDecimal cost, BigDecimal reward, String actAddress, String detailAddress,
                                           String contactWay, String actScope, String actInformation,
                                           MultipartFile poster, MultipartFile[] pictures, MultipartFile actPlan) throws UnhandledException, IOException {
        ResultBean<Void> result = new ResultBean<>();
        Long launcherId = systemUserDao.selectByUsername(JwtUtils.getUserName(token)).getId();

        //?????????????????????????????????????????????
        UserDarkroom userDarkroom = userDarkroomDao.selectByUserId(launcherId);
        if (userDarkroom != null) {
            if (BaseUtils.isDarkRoom(userDarkroom.getTime(), userDarkroom.getCreateTime())) {
                //time<0??????????????????
                UnhandledException unhandledException = new UnhandledException();
                if (userDarkroom.getTime() < 0) {
                    unhandledException.setMsg("????????????????????????,???????????????????????????");
                } else {
                    unhandledException.setMsg("???????????????????????????????????????,??????" + BaseUtils.calculateDarkRoom(userDarkroom.getTime(), userDarkroom.getCreateTime())
                            + "??????????????????????????????????????????");
                }
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            } else {
                //??????????????????????????????????????????
                userDarkroomDao.deleteByUserId(userDarkroom.getUserId());
            }
        }


        //??????????????????????????????,???????????????????????????????????????????????????????????????????????????
        if (activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.CHECKING) != null ||
                activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.ACCESS) != null ||
                activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.ENROLLING) != null ||
                activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.ENROLL_CLOSED) != null ||
                activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.HOLDING) != null ||
                activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.ENDED) != null) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("???????????????????????????");
            unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            throw unhandledException;
        }
        //????????????????????????????????????????????????
        if (enrollEndTime >= holdBeginTime) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("??????????????????????????????????????????????????????");
            unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            throw unhandledException;
        }

        SystemUser currentUser = systemUserDao.selectById(launcherId);
        //????????????????????????
        String posterFileName = poster.getOriginalFilename();
        String newPosterFileName = JwtUtils.getUserName(token) + " ??????" + posterFileName.substring(posterFileName.lastIndexOf("."));
        String posterPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "APPLY_ACTIVITY"
                + File.separatorChar + currentUser.getUsername() + File.separatorChar + actName
                + File.separatorChar + "POSTER_IMG" + File.separatorChar + newPosterFileName;
        String picturePath = null;
        if (pictures != null && pictures.length != 0) {
            StringBuilder sb = new StringBuilder();
            Integer number = 1;
            for (MultipartFile file : pictures) {
                String pictureFileName = file.getOriginalFilename();
                String newPictureFileName = JwtUtils.getUserName(token) + " ????????????(" + number + ")" + pictureFileName.substring(pictureFileName.lastIndexOf("."));
                number++;
                String p = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "APPLY_ACTIVITY"
                        + File.separatorChar + currentUser.getUsername() + File.separatorChar + actName
                        + File.separatorChar + "PICTURE_IMG" + File.separatorChar + newPictureFileName;
                sb.append(p + ";");
                Path picturePathFile = FileUtils.forceCreateFile(p);
                file.transferTo(picturePathFile);
            }
            picturePath = sb.toString();
        }
        String planPath = null;
        if (actPlan != null) {
            String planFileName = actPlan.getOriginalFilename();
            String newPlanFileName = JwtUtils.getUserName(token) + " ???????????????" + planFileName.substring(planFileName.lastIndexOf("."));
            planPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "APPLY_ACTIVITY"
                    + File.separatorChar + currentUser.getUsername() + File.separatorChar + actName
                    + File.separatorChar + "PLAN_FILE" + File.separatorChar + newPlanFileName;
        }
        try {
            Path posterPathFile = FileUtils.forceCreateFile(posterPath);
            poster.transferTo(posterPathFile);
            if (actPlan != null) {
                Path actPlanPathFile = FileUtils.forceCreateFile(planPath);
                actPlan.transferTo(actPlanPathFile);
            }
        } catch (Exception e) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("??????????????????");
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            throw unhandledException;
        }


        //??????????????????
        Activity activity = new Activity();
        activity.setName(actName);
        activity.setLauncherId(launcherId);
        activity.setLaunchedTime(System.currentTimeMillis());
        activity.setTypes(actType);
        activity.setEnrollBeginTime(enrollBeginTime);
        activity.setEnrollEndTime(enrollEndTime);
        activity.setBeginTime(holdBeginTime);
        activity.setEndTime(holdEndTime);
        activity.setScope(actScope);
        activity.setLocation(actAddress);
        activity.setDetailedLocation(detailAddress);
        activity.setState(ActivityStateConstant.CHECKING);
        activity.setSizeOfPeople(0);
        activityDao.insert(activity);

        //????????????????????????
        ActivityExtension activityExtension = new ActivityExtension();
        activityExtension.setActId(activity.getId());

        activityExtension.setStaffTypes(staffTypes); //?????????????????????????????? ?????????;?????????
        activityExtension.setStaffTypesCount(staffCount); //??????
        activityExtension.setRequirement(jobRequirement);
        activityExtension.setCost(cost);
        activityExtension.setReward(reward);
        activityExtension.setContactWay(contactWay);
        activityExtension.setIntroduction(actInformation);
        activityExtension.setPoster(posterPath);
        activityExtension.setPicture(picturePath);
        activityExtension.setPlan(planPath);
        activityExtensionDao.insert(activityExtension);


        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ?????????????????????/??????????????????
     *
     * @param actId  ??????Id
     * @param reason ????????????
     * @return
     */
    @Override
    public ResultBean<Void> cancelActivity(Long actId, String reason) {
        ResultBean<Void> result = new ResultBean<>();

        Activity activity = activityDao.selectById(actId);
        //???????????????????????????/?????????????????????????????????????????????
        if (activity.getState().equals(ActivityStateConstant.CHECKING) || activity.getState().equals(ActivityStateConstant.FAILED)) {
            activity.setState(ActivityStateConstant.CANCELED);
            result.setMsg("????????????");

        } else {
            //???????????????????????? ??????????????????????????????/????????????????????????????????????????????????
            //???????????????activity???state??????????????????
            activity.setState(ActivityStateConstant.CANCELING);
            result.setMsg("????????????????????????");
        }
        activity.setNote(reason);
        activityDao.update(activity);

        result.setStatus(HttpStatus.OK.value());
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ?????????????????????????????????/??????????????????
     *
     * @param actId ??????ID
     * @return
     */
    @Override
    public ResultBean<ActivityBean> getActInformation(Long actId) {

        ResultBean<ActivityBean> result = new ResultBean<>();
        Activity activity = activityDao.selectById(actId);
        ActivityExtension activityExtension = activityExtensionDao.selectByActId(actId);

        //????????????????????????????????????
        activityService.updateActState(activity);

        ActivityBean activityBean = new ActivityBean(actId, activity.getName(), activity.getLauncherId(),
                activity.getLaunchedTime(), activity.getBeginTime(), activity.getEndTime(), activity.getEnrollBeginTime(),
                activity.getEnrollEndTime(), activity.getScope(), activity.getTypes(), activity.getLocation(),
                activity.getDetailedLocation(), activity.getLevel(), activity.getNote(), activity.getSizeOfPeople(),
                activity.getState(), activity.getParticipateIds(), activityExtension.getStaffTypes(),
                activityExtension.getStaffTypesCount(), activityExtension.getRequirement(), activityExtension.getCost(),
                activityExtension.getReward(), activityExtension.getPoint(), activityExtension.getContactWay(),
                activityExtension.getIntroduction(), FileUtils.hidePath(activityExtension.getPoster()), FileUtils.hidePaths(activityExtension.getPicture()),
                FileUtils.hidePath(activityExtension.getPlan()));

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(activityBean);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ????????????
     *
     * @param actId               ??????id
     * @param teamName            ????????????
     * @param preferPositionCount ????????????
     * @param preferPosition      ???????????????
     *                            ????????????????????????????????????
     * @return
     */
    @Override
    public ResultBean<Void> enrollActivity(Long actId, String teamName, String preferPosition, String preferPositionCount) throws UnhandledException {

        ResultBean<Void> result = new ResultBean<>();
        Activity activity = activityDao.selectById(actId);
        //????????????teamName????????????,???????????????????????????????????????????????????????????????????????????????????????
        //?????????????????????????????????????????????currentUser.getRole()????????????????????????id

        SystemUser currentUser = systemUserDao.selectByUsername(teamName);

        //??????
        if (UserRolesConstant.CLUB_USER_NUM.equals(BaseUtils.getListFromString(currentUser.getRole()).get(0)) || UserRolesConstant.ENTERPRISE_USER_NUM.equals(BaseUtils.getListFromString(currentUser.getRole()).get(0))) {
            if (userExtensionOrganizationDao.selectByUserId(currentUser.getId()).getEmail() == null && "".equals(userExtensionOrganizationDao.selectByUserId(currentUser.getId()).getEmail())) {
                UnhandledException unhandledException = new UnhandledException();
                unhandledException.setMsg("?????????????????????????????????????????????");
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            }
        }
        //??????
        if (UserRolesConstant.PERSON_USER_NUM.equals(BaseUtils.getListFromString(currentUser.getRole()).get(0))) {
            if (userExtensionPersonDao.selectByUserId(currentUser.getId()).getEmail() == null && "".equals(userExtensionPersonDao.selectByUserId(currentUser.getId()).getEmail())) {
                UnhandledException unhandledException = new UnhandledException();
                unhandledException.setMsg("?????????????????????????????????????????????");
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            }
        }


        //?????????????????????????????????????????????
        UserDarkroom userDarkroom = userDarkroomDao.selectByUserId(currentUser.getId());
        if (userDarkroom != null) {
            if (BaseUtils.isDarkRoom(userDarkroom.getTime(), userDarkroom.getCreateTime())) {
                //time<0??????????????????
                UnhandledException unhandledException = new UnhandledException();
                if (userDarkroom.getTime() < 0) {
                    unhandledException.setMsg("????????????????????????,???????????????????????????");
                } else {
                    unhandledException.setMsg("???????????????????????????????????????,??????" + BaseUtils.calculateDarkRoom(userDarkroom.getTime(), userDarkroom.getCreateTime())
                            + "??????????????????????????????????????????");
                }
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            } else {
                //??????????????????????????????????????????
                userDarkroomDao.deleteByUserId(userDarkroom.getUserId());
            }
        }

        //????????????????????????????????????
        if (activity.getLauncherId() == currentUser.getId()) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("????????????????????????????????????");
            unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            throw unhandledException;
        }

        //??????????????????????????????????????????????????????????????????????????????
        UserActivity userActivityRepeat = userActivityDao.selectByUserIdAndActId(currentUser.getId(), actId);
        if (userActivityRepeat != null) {
            //?????????????????????????????????????????????????????????????????????????????????????????????
            if (userActivityRepeat.getState().equals(UserActivityStateConstant.UNDER_REVIEW) ||
                    userActivityRepeat.getState().equals(UserActivityStateConstant.PASSED) ||
                    userActivityRepeat.getState().equals(UserActivityStateConstant.ACTIVITY_DELETED) ||
                    userActivityRepeat.getState().equals(UserActivityStateConstant.COMPLETED)) {
                UnhandledException unhandledException = new UnhandledException();
                unhandledException.setMsg("?????????????????????");
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            } else {
                //?????????????????????????????????????????????????????????????????????????????????????????????actid???userId????????????????????????????????????
                userActivityDao.deleteByUserId(currentUser.getId());
            }
        }


        List<String> roles = BaseUtils.getListFromString(currentUser.getRole());
        SystemRole role = systemRoleDao.selectById(Long.parseLong(roles.get(0)));

        //??????????????????????????????????????????????????????
        if (role.getId() == 1) {
            UserInformation userInformation = userInformationDao.selectByUserId(currentUser.getId());
            if (userInformation.getName() == null || userInformation.getCardId() == null) {
                UnhandledException unhandledException = new UnhandledException();
                unhandledException.setMsg("?????????????????????");
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            }
        }

        UserActivity userActivity = new UserActivity();
        userActivity.setUserId(currentUser.getId());
        userActivity.setActId(actId);
        userActivity.setCreateTime(System.currentTimeMillis());
        userActivity.setTeamName(teamName);
        //???????????????????????????????????????
        userActivity.setState(UserActivityStateConstant.UNDER_REVIEW);
        userActivity.setStaffType(preferPosition);

        if (role.getId() == 1) {
            //????????????preferPosition="?????????;"???preferPositionCount="1"
            userActivity.setMemberCount(1);
        } else {
            //???????????? preferPosition="?????????;?????????;"???preferPositionCount="1;2;"
            List<String> roleCount = BaseUtils.getListFromString(preferPositionCount);
            Integer counts = 0;
            for (int i = 0; i < roleCount.size(); i++) {
                counts = counts + Integer.parseInt(roleCount.get(i));
            }
            userActivity.setMemberCount(counts);
        }
        userActivityDao.insert(userActivity);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ????????????
     *
     * @param userId ??????id
     * @param actId  ??????id
     * @return
     */
    @Override
    public ResultBean<Void> exitActivity(Long userId, Long actId) {
        UserActivity userActivity = userActivityDao.selectByUserIdAndActId(userId, actId);
        Activity activity = activityDao.selectById(actId);
        //???????????????????????????????????? ?????? ??????????????????????????????????????????
        if (userActivity.getState().equals(UserActivityStateConstant.UNDER_REVIEW) || activity.getBeginTime() - System.currentTimeMillis() >= 3 * 24 * 60 * 60 * 1000) {
            userActivity.setState(UserActivityStateConstant.SIGN_OUT);
            userActivityDao.update(userActivity);
        } else {
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????id???????????????-1
            //??????????????????

        }

        ResultBean<Void> result = new ResultBean<>();
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ???????????????????????????????????????????????????
     * ?????????????????????????????????????????????????????????
     *
     * @param userId ??????id
     * @return
     */
    @Override
    public ResultBean<List<ActivityGetBean>> getAllActivities(Long userId, String name) {
        SystemUser currentUser = systemUserDao.selectById(userId);
        ResultBean<List<ActivityGetBean>> result = new ResultBean<>();
        List<ActivityGetBean> activityGetBeans = new LinkedList<>();

        String roles = currentUser.getRole();
        List<String> roleList = BaseUtils.getListFromString(roles);

        //?????????????????????????????????????????????id=1?????????
        if (roleList.size() == 1) {
            SystemRole role = systemRoleDao.selectById(Long.parseLong(roleList.get(0)));
            Long roleId = role.getId();
            //?????????1????????????????????????2????????????3????????????
            String scope = "";
            if (roleId == 1) {
                scope = ActivityScopeConstant.PERSON;
            } else if (roleId == 2 || roleId == 3) {
                scope = ActivityScopeConstant.GROUP;
            }
            String newName = "";
            if (name != null) {
                newName = name;
            }
            List<Long> activityIdList = activityDao.selectByScope(scope, newName);

            //???????????????????????????
            for (int i = 0; i < activityIdList.size(); i++) {
                Long actId = activityIdList.get(i);
                UserActivity userActivity = userActivityDao.selectByUserIdAndActId(userId, actId);
                //????????????userActivity???????????????????????????????????????
                //????????????????????????????????????--->??????
                if (userActivity != null) {
                    if (!userActivity.getState().equals(UserActivityStateConstant.TURN_DOWN) && !userActivity.getState().equals(UserActivityStateConstant.SIGN_OUT)) {
                        activityIdList.remove(i);
                        i = i - 1;
                    }
                }
            }

            //??????????????????????????????????????????????????????????????????
            for (int i = 0; i < activityIdList.size(); i++) {
                Long actId = activityIdList.get(i);
                Activity activity = activityDao.selectById(actId);
                activityService.updateActState(activity);
            }

            //??????????????????????????????????????????
            for (int i = 0; i < activityIdList.size(); i++) {
                Long actId = activityIdList.get(i);
                Activity activity = activityDao.selectById(actId);
                if (!activity.getState().equals(ActivityStateConstant.ENROLLING)) {
                    activityIdList.remove(i);
                    //System.out.println("1111");
                    i = i - 1;
                }
            }

            //??????????????????????????????
            for (int i = 0; i < activityIdList.size(); i++) {
                Long actId = activityIdList.get(i);
                Activity activity = activityDao.selectById(actId);
                if (activity.getLauncherId() == userId) {
                    activityIdList.remove(i);
                    i = i - 1;
                }
            }


            //????????????id????????????activity???activityExtension?????????ActivityBean
            for (int i = 0; i < activityIdList.size(); i++) {
                Long actId = activityIdList.get(i);
                Activity activity = activityDao.selectById(actId);
                ActivityExtension activityExtension = activityExtensionDao.selectByActId(actId);


                ActivityBean activityBean = new ActivityBean(actId, activity.getName(), activity.getLauncherId(),
                        activity.getLaunchedTime(), activity.getBeginTime(), activity.getEndTime(), activity.getEnrollBeginTime(),
                        activity.getEnrollEndTime(), activity.getScope(), activity.getTypes(), activity.getLocation(),
                        activity.getDetailedLocation(), activity.getLevel(), activity.getNote(), activity.getSizeOfPeople(),
                        activity.getState(), activity.getParticipateIds(), activityExtension.getStaffTypes(),
                        activityExtension.getStaffTypesCount(), activityExtension.getRequirement(), activityExtension.getCost(),
                        activityExtension.getReward(), activityExtension.getPoint(), activityExtension.getContactWay(),
                        activityExtension.getIntroduction(), FileUtils.hidePath(activityExtension.getPoster()), FileUtils.hidePaths(activityExtension.getPicture()),
                        FileUtils.hidePath(activityExtension.getPlan()));

                String time = BaseUtils.parseTime(activity.getBeginTime());


                ActivityGetBean activityGetBean = new ActivityGetBean(activityBean, time);
                activityGetBeans.add(activityGetBean);
            }
        }

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(activityGetBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????/????????????
     *
     * @param userId ??????id
     * @return
     */
    @Override
    public ResultBean<List<ActEnrollBean>> getUserActInformation(Long userId) {
        log.info(String.valueOf(userId));
        ResultBean<List<ActEnrollBean>> result = new ResultBean<>();
        List<ActEnrollBean> actEnrollBeans = new LinkedList<>();
        List<UserActivity> userActivityList = userActivityDao.selectByUserId(userId);
        if (userActivityList.size() != 0) {
            for (int i = 0; i < userActivityList.size(); i++) {
                //????????????????????????????????????????????????ActEnrollBean
                Long actId = userActivityList.get(i).getActId();
                Activity activity = activityDao.selectById(actId);
                ActivityExtension activityExtension = activityExtensionDao.selectByActId(actId);
                //????????????????????????????????????
                activityService.updateActState(activity);

                if (userActivityList.get(i).getCause() != null) {
                    ActEnrollBean actEnrollBean = new ActEnrollBean(activity.getName(), activity.getBeginTime(), activity.getEndTime(),
                            activity.getTypes(), activity.getLocation(), activity.getDetailedLocation(), activity.getState(), activityExtension.getStaffTypes(),
                            activityExtension.getStaffTypesCount(), activityExtension.getRequirement(),
                            activityExtension.getCost(), activityExtension.getReward(), activityExtension.getContactWay(),
                            activityExtension.getIntroduction(), FileUtils.hidePath(activityExtension.getPoster()), userActivityList.get(i).getState(),
                            userActivityList.get(i).getCause(), userActivityList.get(i).getStaffType(), userActivityList.get(i).getTeamName()
                    );
                    actEnrollBeans.add(actEnrollBean);
                } else {
                    ActEnrollBean actEnrollBean = new ActEnrollBean(activity.getName(), activity.getBeginTime(), activity.getEndTime(),
                            activity.getTypes(), activity.getLocation(), activity.getDetailedLocation(), activity.getState(), activityExtension.getStaffTypes(),
                            activityExtension.getStaffTypesCount(), activityExtension.getRequirement(),
                            activityExtension.getCost(), activityExtension.getReward(), activityExtension.getContactWay(),
                            activityExtension.getIntroduction(), FileUtils.hidePath(activityExtension.getPoster()), userActivityList.get(i).getState(),
                            "no-cause", userActivityList.get(i).getStaffType(), userActivityList.get(i).getTeamName()
                    );
                    actEnrollBeans.add(actEnrollBean);
                }
            }
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(actEnrollBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param userId ????????????id
     * @return
     */
    @Override
    public ResultBean<List<ActivityNewBean>> getAllActInformation(Long userId) {
        ResultBean<List<ActivityNewBean>> result = new ResultBean<>();
        List<Activity> activities = activityDao.selectListByLauncherId(userId);
        List<ActivityNewBean> activityBeans = new LinkedList<>();
        if (activities.size() != 0) {
            for (int i = 0; i < activities.size(); i++) {
                //????????????????????????
                activityService.updateActState(activities.get(i));

                Long actId = activities.get(i).getId();
                ActivityExtension activityExtension = activityExtensionDao.selectByActId(actId);
                ActivityUnexpect activityUnexpect = activityUnexpectDao.selectByActId(actId);
                String cause = "???";
                if (activityUnexpect != null) {
                    cause = activityUnexpect.getMessage();
                }

                ActivityNewBean activityNewBean = new ActivityNewBean(actId, activities.get(i).getName(), activities.get(i).getLauncherId(),
                        activities.get(i).getLaunchedTime(), activities.get(i).getBeginTime(), activities.get(i).getEndTime(), activities.get(i).getEnrollBeginTime(),
                        activities.get(i).getEnrollEndTime(), activities.get(i).getScope(), activities.get(i).getTypes(), activities.get(i).getLocation(),
                        activities.get(i).getDetailedLocation(), activities.get(i).getLevel(), activities.get(i).getNote(), activities.get(i).getSizeOfPeople(),
                        activities.get(i).getState(), activities.get(i).getParticipateIds(), activityExtension.getStaffTypes(),
                        activityExtension.getStaffTypesCount(), activityExtension.getRequirement(), activityExtension.getCost(),
                        activityExtension.getReward(), activityExtension.getPoint(), activityExtension.getContactWay(),
                        activityExtension.getIntroduction(), FileUtils.hidePath(activityExtension.getPoster()), FileUtils.hidePaths(activityExtension.getPicture()),
                        FileUtils.hidePath(activityExtension.getPlan()), cause);
                activityBeans.add(activityNewBean);
            }
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(activityBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param actId ??????id
     * @return
     */
    @Override
    public ResultBean<?> getActEnrollInformation(Long actId) {
        ResultBean<List<ActUserBean>> result = new ResultBean<>();
        List<ActUserBean> actUserBeans = new LinkedList<>();

        Activity activity = activityDao.selectById(actId);
        List<String> userIdList = BaseUtils.getListFromString(activity.getParticipateIds());
        if (userIdList.size() != 0) {
            for (int i = 0; i < userIdList.size(); i++) {
                Long userId = Long.parseLong(userIdList.get(i));

                UserActivity userActivity = userActivityDao.selectByUserIdAndActId(userId, actId);

                UserInformation userInformation = userInformationDao.selectByUserId(userId);
                //??????getMemberCount??????????????????????????????????????????count?????????????????????
                SystemUser currentUser = systemUserDao.selectById(userId);
                List<String> roles = BaseUtils.getListFromString(currentUser.getRole());
                SystemRole role = systemRoleDao.selectById(Long.parseLong(roles.get(0)));
                ActUserBean actUserBean = new ActUserBean();
                if (role.getId() == 1) {
                    //?????????????????????
                    actUserBean.setName(userInformation.getName());
                } else if (role.getId() == 2 || role.getId() == 3) {
                    //???????????????????????????name????????????????????????username
                    actUserBean.setName(currentUser.getUsername());
                }
                actUserBean.setStaffType(userActivity.getStaffType());
                actUserBean.setMemberCount(userActivity.getMemberCount());
                actUserBean.setState(userActivity.getState());

                actUserBeans.add(actUserBean);
            }
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("????????????");
        result.setData(actUserBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

}
