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
     * 发布活动（企业/俱乐部专用）
     * @param actName 活动名
     * @param actType 活动类型（如线下赛事）
     * @param enrollBeginTime 报名开始时间
     * @param enrollEndTime 报名截止时间
     * @param holdBeginTime 活动开始时间
     * @param holdEndTime   活动截止时间
     * @param staffTypes    活动人员类别
     * @param staffCount    活动人员类别数量
     * @param jobRequirement    活动岗位要求
     * @param cost  报名费用
     * @param reward    活动奖金
     * @param actAddress    活动地点
     * @param detailAddress 详细地址
     * @param contactWay    活动报名联系方式
     * @param actScope  活动面向对象
     * @param actInformation    活动简介
     * @param poster    活动海报
     * @param pictures   活动组图
     * @param actPlan 活动企划书
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

        //被拉进小黑屋的人不允许报名活动
        UserDarkroom userDarkroom = userDarkroomDao.selectByUserId(launcherId);
        if(userDarkroom != null) {
            if(BaseUtils.isDarkRoom(userDarkroom.getTime(), userDarkroom.getCreateTime())) {
                //time<0表示永久拉黑
                UnhandledException unhandledException = new UnhandledException();
                if(userDarkroom.getTime() < 0) {
                    unhandledException.setMsg("您已经被永久封禁,有疑惑请联系管理员");
                } else {
                    unhandledException.setMsg("您已进入黑名单无法发布活动,还剩" + BaseUtils.calculateDarkRoom(userDarkroom.getTime(), userDarkroom.getCreateTime())
                            +"解封，如有疑惑请联系管理员！");
                }
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            } else {
                //时间到了，解禁了，移除小黑屋
                userDarkroomDao.deleteByUserId(userDarkroom.getUserId());
            }
        }



        //设置为活动名称不重复,因为同一机构不允许申请同名活动（除非活动已经撤销）
        if(activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.CHECKING) != null ||
                activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.ACCESS) != null ||
                activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.ENROLLING) != null ||
                activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.ENROLL_CLOSED) != null ||
                activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.HOLDING) != null ||
                activityDao.selectByActNameAndUserIdAndState(actName, launcherId, ActivityStateConstant.ENDED) != null ) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("请不要申请同名活动");
            unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            throw unhandledException;
        }
        //判断开始时间要在报名截止时间之后
        if(enrollEndTime >= holdBeginTime) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("活动开始时间请不要在报名截止时间之前");
            unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            throw unhandledException;
        }

        SystemUser currentUser = systemUserDao.selectById(launcherId);
        //进行相关文件操作
        String posterFileName = poster.getOriginalFilename();
        String newPosterFileName = JwtUtils.getUserName(token) + " 海报" + posterFileName.substring(posterFileName.lastIndexOf("."));
        String posterPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "APPLY_ACTIVITY"
                + File.separatorChar + currentUser.getUsername() + File.separatorChar + actName
                + File.separatorChar + "POSTER_IMG" + File.separatorChar + newPosterFileName;
        String picturePath = null;
        if(pictures != null && pictures.length != 0) {
            StringBuilder sb = new StringBuilder();
            Integer number = 1;
            for (MultipartFile file: pictures) {
                String pictureFileName = file.getOriginalFilename();
                String newPictureFileName = JwtUtils.getUserName(token) + " 活动组图(" + number + ")" + pictureFileName.substring(pictureFileName.lastIndexOf("."));
                number++;
                String p = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "APPLY_ACTIVITY"
                        + File.separatorChar + currentUser.getUsername() + File.separatorChar + actName
                        + File.separatorChar + "PICTURE_IMG" + File.separatorChar + newPictureFileName;
                sb.append(p+";");
                Path picturePathFile = FileUtils.forceCreateFile(p);
                file.transferTo(picturePathFile);
            }
            picturePath = sb.toString();
        }
        String planPath = null;
        if(actPlan != null) {
            String planFileName = actPlan.getOriginalFilename();
            String newPlanFileName = JwtUtils.getUserName(token) + " 活动计划书" + planFileName.substring(planFileName.lastIndexOf("."));
            planPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "APPLY_ACTIVITY"
                    + File.separatorChar + currentUser.getUsername() + File.separatorChar + actName
                    + File.separatorChar + "PLAN_FILE" + File.separatorChar + newPlanFileName;
        }
        try {
            Path posterPathFile = FileUtils.forceCreateFile(posterPath);
            poster.transferTo(posterPathFile);
            if(actPlan != null) {
                Path actPlanPathFile = FileUtils.forceCreateFile(planPath);
                actPlan.transferTo(actPlanPathFile);
            }
        } catch (Exception e) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("文件存储失败");
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            throw unhandledException;
        }


        //设置活动信息
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

        //设置活动拓展信息
        ActivityExtension activityExtension = new ActivityExtension();
        activityExtension.setActId(activity.getId());

        activityExtension.setStaffTypes(staffTypes); //默认前端传过来的就是 导引类;接待类
        activityExtension.setStaffTypesCount(staffCount); //同上
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
        result.setMsg("发布成功");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 撤销活动（企业/俱乐部专有）
     * @param actId 活动Id
     * @param reason 撤销原因
     * @return
     */
    @Override
    public ResultBean<Void> cancelActivity(Long actId ,String reason) {
        ResultBean<Void> result = new ResultBean<>();

        Activity activity = activityDao.selectById(actId);
        //如果活动处于待审核/审核失败中，让活动状态直接取消
        if(activity.getState().equals(ActivityStateConstant.CHECKING) || activity.getState().equals(ActivityStateConstant.FAILED)) {
            activity.setState(ActivityStateConstant.CANCELED);
            result.setMsg("撤销成功");

        } else {
            //如果已经审核通过 或者活动已经在报名中/进行中，则需提交申请让管理员审核
            //这里采用让activity的state变为活动取消
            activity.setState(ActivityStateConstant.CANCELING);
            result.setMsg("请等待管理员审核");
        }
        activity.setNote(reason);
        activityDao.update(activity);

        result.setStatus(HttpStatus.OK.value());
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 查看某一活动详情（企业/俱乐部专有）
     * @param actId 活动ID
     * @return
     */
    @Override
    public ResultBean<ActivityBean> getActInformation(Long actId) {

        ResultBean<ActivityBean> result = new ResultBean<>();
        Activity activity = activityDao.selectById(actId);
        ActivityExtension activityExtension = activityExtensionDao.selectByActId(actId);

        //更新已通过活动的时间状态
        activityService.updateActState(activity);

        ActivityBean activityBean = new ActivityBean(actId, activity.getName(),activity.getLauncherId(),
                activity.getLaunchedTime(),activity.getBeginTime(),activity.getEndTime(),activity.getEnrollBeginTime(),
                activity.getEnrollEndTime(),activity.getScope(),activity.getTypes(),activity.getLocation(),
                activity.getDetailedLocation(),activity.getLevel(),activity.getNote(),activity.getSizeOfPeople(),
                activity.getState(),activity.getParticipateIds(),activityExtension.getStaffTypes(),
                activityExtension.getStaffTypesCount(),activityExtension.getRequirement(), activityExtension.getCost(),
                activityExtension.getReward(), activityExtension.getPoint(),activityExtension.getContactWay(),
                activityExtension.getIntroduction(),FileUtils.hidePath(activityExtension.getPoster()),FileUtils.hidePaths(activityExtension.getPicture()),
                FileUtils.hidePath(activityExtension.getPlan()));

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功");
        result.setData(activityBean);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 报名活动
     * @param actId 活动id
     * @param teamName 小队名称
     * @param preferPositionCount 参加人数
     * @param preferPosition 倾向的岗位
     * 还没有设置不允许重复参加
     * @return
     */
    @Override
    public ResultBean<Void> enrollActivity(Long actId, String teamName, String preferPosition, String preferPositionCount) throws UnhandledException {

        ResultBean<Void> result = new ResultBean<>();
        Activity activity = activityDao.selectById(actId);
        //通过唯一teamName找到用户,如果是个人参加人数就一个人，岗位也就一个。企业的话进行判断
        //当前版本先不存在多重身份，所以currentUser.getRole()只会获得一个角色id

        SystemUser currentUser = systemUserDao.selectByUsername(teamName);

        //团体
        if (UserRolesConstant.CLUB_USER_NUM.equals(BaseUtils.getListFromString(currentUser.getRole()).get(0))||UserRolesConstant.ENTERPRISE_USER_NUM.equals(BaseUtils.getListFromString(currentUser.getRole()).get(0))) {
            if (userExtensionOrganizationDao.selectByUserId(currentUser.getId()).getEmail() == null && "".equals(userExtensionOrganizationDao.selectByUserId(currentUser.getId()).getEmail())) {
                UnhandledException unhandledException = new UnhandledException();
                unhandledException.setMsg("邮箱信息未填写，不可参加活动！");
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            }
        }
        //个人
        if (UserRolesConstant.PERSON_USER_NUM.equals(BaseUtils.getListFromString(currentUser.getRole()).get(0))) {
            if (userExtensionPersonDao.selectByUserId(currentUser.getId()).getEmail() == null && "".equals(userExtensionPersonDao.selectByUserId(currentUser.getId()).getEmail())) {
                UnhandledException unhandledException = new UnhandledException();
                unhandledException.setMsg("邮箱信息未填写，不可参加活动！");
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            }
        }


        //被拉进小黑屋的人不允许报名活动
        UserDarkroom userDarkroom = userDarkroomDao.selectByUserId(currentUser.getId());
        if(userDarkroom != null) {
            if(BaseUtils.isDarkRoom(userDarkroom.getTime(), userDarkroom.getCreateTime())) {
                //time<0表示永久拉黑
                UnhandledException unhandledException = new UnhandledException();
                if(userDarkroom.getTime() < 0) {
                    unhandledException.setMsg("您已经被永久封禁,有疑惑请联系管理员");
                } else {
                    unhandledException.setMsg("您已进入黑名单无法报名活动,还剩" + BaseUtils.calculateDarkRoom(userDarkroom.getTime(), userDarkroom.getCreateTime())
                            +"解封，如有疑惑请联系管理员！");
                }
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            } else {
                //时间到了，解禁了，移除小黑屋
                userDarkroomDao.deleteByUserId(userDarkroom.getUserId());
            }
        }

        //不允许报名自己发布的活动
        if(activity.getLauncherId() ==  currentUser.getId()) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("请不要报名自己发布的活动");
            unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            throw unhandledException;
        }

        //首先需要判断有没有重复报名（被拒绝的也可以重新报名）
        UserActivity userActivityRepeat = userActivityDao.selectByUserIdAndActId(currentUser.getId(), actId);
        if(userActivityRepeat != null) {
            //证明数据库中确实有此报名记录，除了被拒绝或者退出的可以重复报名
            if (userActivityRepeat.getState().equals(UserActivityStateConstant.UNDER_REVIEW) ||
                    userActivityRepeat.getState().equals(UserActivityStateConstant.PASSED) ||
                    userActivityRepeat.getState().equals(UserActivityStateConstant.ACTIVITY_DELETED) ||
                    userActivityRepeat.getState().equals(UserActivityStateConstant.COMPLETED)) {
                UnhandledException unhandledException = new UnhandledException();
                unhandledException.setMsg("请不要重复报名");
                unhandledException.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                throw unhandledException;
            } else {
                //如果是已经有被拒绝或者退出的状态就先删除这条记录，不然后续根据actid和userId查询会报错（会查出两个）
                userActivityDao.deleteByUserId(currentUser.getId());
            }
        }


        List<String> roles = BaseUtils.getListFromString(currentUser.getRole());
        SystemRole role = systemRoleDao.selectById(Long.parseLong(roles.get(0)));

        //如果是个人用户，需判断有没有实名认证
        if(role.getId() == 1) {
            UserInformation userInformation = userInformationDao.selectByUserId(currentUser.getId());
            if(userInformation.getName() == null || userInformation.getCardId() == null) {
                UnhandledException unhandledException = new UnhandledException();
                unhandledException.setMsg("请先去实名认证");
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
        //参加活动后，状态设成待审核
        userActivity.setState(UserActivityStateConstant.UNDER_REVIEW);
        userActivity.setStaffType(preferPosition);

        if(role.getId() == 1) {
            //个人参赛preferPosition="导引类;"，preferPositionCount="1"
            userActivity.setMemberCount(1);
        } else {
            //团体参赛 preferPosition="导引类;接引类;"，preferPositionCount="1;2;"
            List<String> roleCount = BaseUtils.getListFromString(preferPositionCount);
            Integer counts = 0;
            for(int i = 0; i < roleCount.size(); i++) {
                counts = counts + Integer.parseInt(roleCount.get(i));
            }
            userActivity.setMemberCount(counts);
        }
        userActivityDao.insert(userActivity);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("报名成功");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 退出活动
     * @param userId 用户id
     * @param actId 活动id
     * @return
     */
    @Override
    public ResultBean<Void> exitActivity(Long userId, Long actId) {
        UserActivity userActivity = userActivityDao.selectByUserIdAndActId(userId, actId);
        Activity activity = activityDao.selectById(actId);
        //如果是待审核状态允许退出 或者 活动开始前三天，可以退出报名
        if(userActivity.getState().equals(UserActivityStateConstant.UNDER_REVIEW) || activity.getBeginTime() - System.currentTimeMillis() >= 3*24*60*60*1000) {
            userActivity.setState(UserActivityStateConstant.SIGN_OUT);
            userActivityDao.update(userActivity);
        } else {
            //如果是已经审核通过了，则需要申请理由并且管理员同意，然后我再在活动中删除他的参加id与参加人数-1
            //暂时不需要做

        }

        ResultBean<Void> result = new ResultBean<>();
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("退出成功");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 查看自己可见范围内可报名活动的信息
     * （只查看正在报名中且本人未报名的活动）
     * @param userId 用户id
     * @return
     */
    @Override
    public ResultBean<List<ActivityGetBean>> getAllActivities(Long userId, String name) {
        SystemUser currentUser = systemUserDao.selectById(userId);
        ResultBean<List<ActivityGetBean>> result = new ResultBean<>();
        List<ActivityGetBean> activityGetBeans = new LinkedList<>();

        String roles = currentUser.getRole();
        List<String> roleList = BaseUtils.getListFromString(roles);

        //如果只有一个身份，比如说他只有id=1的角色
        if(roleList.size() == 1) {
            SystemRole role = systemRoleDao.selectById(Long.parseLong(roleList.get(0)));
            Long roleId = role.getId();
            //如果是1号就是个人用户，2号企业，3号俱乐部
            String scope = "";
            if(roleId == 1) {
                scope = ActivityScopeConstant.PERSON;
            } else if(roleId == 2 || roleId == 3) {
                scope = ActivityScopeConstant.GROUP;
            }
            String newName = "";
            if(name != null) {
                newName = name;
            }
            List<Long> activityIdList = activityDao.selectByScope(scope, newName);

            //只查看未报名的活动
            for (int i = 0; i < activityIdList.size(); i++) {
                Long actId = activityIdList.get(i);
                UserActivity userActivity = userActivityDao.selectByUserIdAndActId(userId, actId);
                //如果存在userActivity的话，证明已经报名，剔除她
                //已经退出的活动还可以看吗--->可以
                if(userActivity != null ) {
                    if(!userActivity.getState().equals(UserActivityStateConstant.TURN_DOWN) && !userActivity.getState().equals(UserActivityStateConstant.SIGN_OUT)) {
                        activityIdList.remove(i);
                        i = i - 1;
                    }
                }
            }

            //任何对活动状态进行的操作，都要先进行更新状态
            for(int i = 0; i < activityIdList.size(); i++) {
                Long actId = activityIdList.get(i);
                Activity activity = activityDao.selectById(actId);
                activityService.updateActState(activity);
            }

            //并且活动状态要处于正在报名中
            for(int i = 0; i < activityIdList.size(); i++) {
                Long actId = activityIdList.get(i);
                Activity activity = activityDao.selectById(actId);
                if(!activity.getState().equals(ActivityStateConstant.ENROLLING)) {
                    activityIdList.remove(i);
                    //System.out.println("1111");
                    i = i - 1;
                }
            }

            //不看见自己发布的活动
            for(int i = 0; i < activityIdList.size(); i++) {
                Long actId = activityIdList.get(i);
                Activity activity = activityDao.selectById(actId);
                if(activity.getLauncherId() == userId) {
                    activityIdList.remove(i);
                    i = i - 1;
                }
            }


            //根据活动id找到每个activity与activityExtension，组成ActivityBean
            for(int i = 0; i < activityIdList.size(); i++) {
                Long actId = activityIdList.get(i);
                Activity activity = activityDao.selectById(actId);
                ActivityExtension activityExtension = activityExtensionDao.selectByActId(actId);


                ActivityBean activityBean = new ActivityBean(actId, activity.getName(),activity.getLauncherId(),
                        activity.getLaunchedTime(),activity.getBeginTime(),activity.getEndTime(),activity.getEnrollBeginTime(),
                        activity.getEnrollEndTime(),activity.getScope(),activity.getTypes(),activity.getLocation(),
                        activity.getDetailedLocation(),activity.getLevel(),activity.getNote(),activity.getSizeOfPeople(),
                        activity.getState(),activity.getParticipateIds(),activityExtension.getStaffTypes(),
                        activityExtension.getStaffTypesCount(),activityExtension.getRequirement(), activityExtension.getCost(),
                        activityExtension.getReward(), activityExtension.getPoint(),activityExtension.getContactWay(),
                        activityExtension.getIntroduction(),FileUtils.hidePath(activityExtension.getPoster()),FileUtils.hidePaths(activityExtension.getPicture()),
                        FileUtils.hidePath(activityExtension.getPlan()));

                String time = BaseUtils.parseTime(activity.getBeginTime());


                ActivityGetBean activityGetBean = new ActivityGetBean(activityBean, time);
                activityGetBeans.add(activityGetBean);
            }
        }

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功");
        result.setData(activityGetBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 用户查看自己全部参与信息情况，包括已通过（活动状态）/未审核等
     * @param userId 用户id
     * @return
     */
    @Override
    public ResultBean<List<ActEnrollBean>> getUserActInformation(Long userId) {
        ResultBean<List<ActEnrollBean>> result = new ResultBean<>();
        List<ActEnrollBean> actEnrollBeans = new LinkedList<>();
        List<UserActivity> userActivityList = userActivityDao.selectByUserId(userId);
        if(userActivityList.size() != 0) {
            for(int i = 0; i < userActivityList.size(); i++) {
                //对每一个用户参加的活动进行封装成ActEnrollBean
                Long actId = userActivityList.get(i).getActId();
                Activity activity = activityDao.selectById(actId);
                ActivityExtension activityExtension = activityExtensionDao.selectByActId(actId);
                //对每一个活动状态进行更新
                activityService.updateActState(activity);

                if(userActivityList.get(i).getCause() != null) {
                    ActEnrollBean actEnrollBean = new ActEnrollBean(activity.getName(),activity.getBeginTime(),activity.getEndTime(),
                            activity.getTypes(),activity.getLocation(), activity.getDetailedLocation(),activity.getState(),activityExtension.getStaffTypes(),
                            activityExtension.getStaffTypesCount(), activityExtension.getRequirement(),
                            activityExtension.getCost(), activityExtension.getReward(),activityExtension.getContactWay(),
                            activityExtension.getIntroduction(),FileUtils.hidePath(activityExtension.getPoster()),userActivityList.get(i).getState(),
                            userActivityList.get(i).getCause(),userActivityList.get(i).getStaffType(),userActivityList.get(i).getTeamName()
                            );
                    actEnrollBeans.add(actEnrollBean);
                } else {
                    ActEnrollBean actEnrollBean = new ActEnrollBean(activity.getName(),activity.getBeginTime(),activity.getEndTime(),
                            activity.getTypes(),activity.getLocation(), activity.getDetailedLocation(),activity.getState(), activityExtension.getStaffTypes(),
                            activityExtension.getStaffTypesCount(), activityExtension.getRequirement(),
                            activityExtension.getCost(), activityExtension.getReward(),activityExtension.getContactWay(),
                            activityExtension.getIntroduction(),FileUtils.hidePath(activityExtension.getPoster()),userActivityList.get(i).getState(),
                            "no-cause",userActivityList.get(i).getStaffType(),userActivityList.get(i).getTeamName()
                    );
                    actEnrollBeans.add(actEnrollBean);
                }
            }
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功");
        result.setData(actEnrollBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 企业查看自己发布的所有活动详情
     * @param userId 企业用户id
     * @return
     */
    @Override
    public ResultBean<List<ActivityNewBean>> getAllActInformation(Long userId) {
        ResultBean<List<ActivityNewBean>> result = new ResultBean<>();
        List<Activity> activities = activityDao.selectListByLauncherId(userId);
        List<ActivityNewBean> activityBeans = new LinkedList<>();
        if(activities.size() != 0) {
            for(int i = 0; i < activities.size(); i++) {
                //更新一下活动状态
                activityService.updateActState(activities.get(i));

                Long actId = activities.get(i).getId();
                ActivityExtension activityExtension = activityExtensionDao.selectByActId(actId);
                ActivityUnexpect activityUnexpect = activityUnexpectDao.selectByActId(actId);
                String cause = "无";
                if(activityUnexpect != null) {
                    cause = activityUnexpect.getMessage();
                }

                ActivityNewBean activityNewBean = new ActivityNewBean(actId, activities.get(i).getName(),activities.get(i).getLauncherId(),
                        activities.get(i).getLaunchedTime(),activities.get(i).getBeginTime(),activities.get(i).getEndTime(),activities.get(i).getEnrollBeginTime(),
                        activities.get(i).getEnrollEndTime(),activities.get(i).getScope(),activities.get(i).getTypes(),activities.get(i).getLocation(),
                        activities.get(i).getDetailedLocation(),activities.get(i).getLevel(),activities.get(i).getNote(),activities.get(i).getSizeOfPeople(),
                        activities.get(i).getState(),activities.get(i).getParticipateIds(),activityExtension.getStaffTypes(),
                        activityExtension.getStaffTypesCount(),activityExtension.getRequirement(), activityExtension.getCost(),
                        activityExtension.getReward(), activityExtension.getPoint(),activityExtension.getContactWay(),
                        activityExtension.getIntroduction(),FileUtils.hidePath(activityExtension.getPoster()),FileUtils.hidePaths(activityExtension.getPicture()),
                        FileUtils.hidePath(activityExtension.getPlan()), cause);
                activityBeans.add(activityNewBean);
            }
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功");
        result.setData(activityBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 企业查看某一活动的报名人员信息
     * @param actId 活动id
     * @return
     */
    @Override
    public ResultBean<?> getActEnrollInformation(Long actId) {
        ResultBean<List<ActUserBean>> result = new ResultBean<>();
        List<ActUserBean> actUserBeans = new LinkedList<>();

        Activity activity = activityDao.selectById(actId);
        List<String> userIdList = BaseUtils.getListFromString(activity.getParticipateIds());
        if(userIdList.size() != 0) {
            for (int i = 0; i < userIdList.size(); i++) {
                Long userId = Long.parseLong(userIdList.get(i));

                UserActivity userActivity = userActivityDao.selectByUserIdAndActId(userId, actId);

                UserInformation userInformation = userInformationDao.selectByUserId(userId);
                //这里getMemberCount可能为空???他既然报名了，那么count应该不可能为空
                SystemUser currentUser = systemUserDao.selectById(userId);
                List<String> roles = BaseUtils.getListFromString(currentUser.getRole());
                SystemRole role = systemRoleDao.selectById(Long.parseLong(roles.get(0)));
                ActUserBean actUserBean = new ActUserBean();
                if(role.getId() == 1) {
                    //如果是个人用户
                    actUserBean.setName(userInformation.getName());
                } else if(role.getId() == 2 || role.getId() == 3) {
                    //如果是企业是不会有name的，所以展示的是username
                    actUserBean.setName(currentUser.getUsername());
                }
                actUserBean.setStaffType(userActivity.getStaffType());
                actUserBean.setMemberCount(userActivity.getMemberCount());
                actUserBean.setState(userActivity.getState());

                actUserBeans.add(actUserBean);
            }
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功");
        result.setData(actUserBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

}
