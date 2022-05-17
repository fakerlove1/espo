package com.stackfarm.esports.service.user.Impl;

import com.stackfarm.esports.dao.activity.ActivityDao;
import com.stackfarm.esports.dao.user.*;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.user.*;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.result.admin.*;
import com.stackfarm.esports.result.user.DarkroomBean;
import com.stackfarm.esports.result.user.UserInfoBean;
import com.stackfarm.esports.service.user.UserInfoService;
import com.stackfarm.esports.system.SystemConstant;
import com.stackfarm.esports.system.UserRolesConstant;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.FileUtils;
import com.stackfarm.esports.utils.IDCardParseUtils;
import com.stackfarm.esports.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author croton
 * @create 2021/4/5 13:01
 */
@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private UserExtensionPersonDao userExtensionPersonDao;
    @Autowired
    private UserExtensionOrganizationDao userExtensionOrganizationDao;
    @Autowired
    private UserActivitiesInfoDao userActivitiesInfoDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private SystemUserDao systemUserDao;
    @Autowired
    private UserDarkroomDao userDarkroomDao;



    /**
     * 修改个人信息
     * @param userId
     * @param realName
     * @param cardId
     * @param preferPosition
     * @param experience
     * @param email
     * @param qq
     * @param introduction
     * @param profile
     * @return
     */
    @Override
    public ResultBean<?> updateUserInformation(Long userId, String realName, String cardId, Integer age, String sex, String nation, Long birthday, String home, String preferPosition,
                                               String experience, String email, String qq, String introduction, String education, MultipartFile profile) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();
        UserExtensionPerson person = userExtensionPersonDao.selectByUserId(userId);
        UserInformation userInformation = userInformationDao.selectByUserId(userId);
        if (cardId != null) {
            Boolean isId = IDCardParseUtils.isValidDate(cardId);
            if (isId) {
                UserInformation userInfo = userInformationDao.selectByCardId(cardId);
                if (userInfo != null) {
                    throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "该身份证信息已被使用！",
                            BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
                }
                userInformation.setCardId(cardId);
                person.setCardId(cardId);
                userInformation.setAge(IDCardParseUtils.IDCardNoToAge(cardId));
                userInformation.setBirthday(IDCardParseUtils.IDCardNoToBirthday(cardId).getTime());
            } else {
                result.setStatus(HttpStatus.BAD_REQUEST.value());
                result.setData(null);
                result.setMsg("身份证信息不合法！");
                result.setTimestamp(System.currentTimeMillis());
                return result;
            }
        }
        if (email != null) {
            UserExtensionPerson userExtensionPerson = userExtensionPersonDao.selectByEmail(email);
            if (userExtensionPerson != null) {
                throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "该邮箱已被使用！",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
            }
        }
        if (systemUserDao.selectById(userId) == null || userId == null) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "该用户不存在！",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
        }
        if (realName != null ) {
            userInformation.setName(realName);
        }
        if (age != null) {
            userInformation.setAge(age);
        }
        if (sex != null) {
            userInformation.setSex(sex);
        }
        if (nation != null) {
            userInformation.setNation(nation);
        }
        if (birthday != null) {
            userInformation.setBirthday(birthday);
        }
        if (home != null) {
            userInformation.setHome(home);
        }
        userInformation.setUpdateTime(System.currentTimeMillis());
        userInformationDao.update(userInformation);

        if (preferPosition != null) {
            person.setPreferPosition(preferPosition);
        }
        if (experience != null) {
            person.setExperience(experience);
        }
        if (education != null) {
            person.setEducation(education);
        }
        if (email != null) {
            person.setEmail(email);
        }

        //TODO 手机号尚不可修改
        if (introduction != null) {
            person.setIntroduction(introduction);
        }
        if (qq != null) {
            try {
                Long QQ = Long.parseLong(qq);
                person.setQq(QQ);
            } catch (NumberFormatException e) {
                throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "qq号格式异常！", BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
            }
        }

        //个人简历上传
        if (profile != null) {
            String fileName = profile.getOriginalFilename();
            String newFileName = systemUserDao.selectById(userId).getUsername() + " 个人简历" + fileName.substring(fileName.lastIndexOf("."));
            try {
                profile.transferTo(FileUtils.forceCreateFile(SystemConstant.FILE_ROOT_PATH + File.separator
                        + "personProfiles" + File.separator + newFileName));
            } catch (IOException e) {
                throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "转储文件时发生错误", BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
            }
            person.setProfile(SystemConstant.FILE_ROOT_PATH + File.separator + "personProfiles" + File.separator + newFileName);
        }
        userExtensionPersonDao.update(person);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("修改成功");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 查看个人信息
     * @param id
     * @return
     * @throws UnhandledException
     */
    @Override
    public ResultBean<UserInfoBean> getUserInformation(Long id) throws UnhandledException {
        ResultBean<UserInfoBean> result = new ResultBean<>();
        Long userId = id;
        SystemUser user = systemUserDao.selectById(userId);
        String username = user.getUsername();
        UserDarkroom userDarkroom = userDarkroomDao.selectByUserId(id);
        DarkroomBean darkroomBean = new DarkroomBean();
        if (userDarkroom == null || userDarkroom.getCreateTime()+userDarkroom.getTime() < System.currentTimeMillis()) {
            //不在小黑屋
            darkroomBean.setDarkRoom(false);
            darkroomBean.setRemainTime("信誉良好");
        } else {
            //在小黑屋
            darkroomBean.setDarkRoom(true);
            darkroomBean.setRemainTime(BaseUtils.calculateDarkRoom(userDarkroom.getTime(), userDarkroom.getCreateTime()));
        }
        if (BaseUtils.getListFromString(user.getRole()).contains("1")) {
            UserExtensionPerson userExtensionPerson = userExtensionPersonDao.selectByUserId(userId);
            userExtensionPerson.setProfile(FileUtils.hidePath(userExtensionPerson.getProfile()));
            String cardId = userExtensionPerson.getCardId();
            UserInformation userInformation = userInformationDao.selectByUserId(userId);
            if (cardId != null) {
                userExtensionPerson.setCardId(cardId.substring(0, 3) + "***********" + cardId.substring(14, 18));
                userInformation.setCardId(cardId.substring(0, 3) + "***********" + cardId.substring(14, 18));
            }
            UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("查看成功");
            result.setData(new UserInfoBean(username, userInformation, userExtensionPerson, userActivitiesInfo, null, darkroomBean));
            result.setTimestamp(System.currentTimeMillis());
        } else if (BaseUtils.getListFromString(user.getRole()).contains("2") || BaseUtils.getListFromString(user.getRole()).contains("3")) {
            UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(userId);
            UserInformation userInformation = userInformationDao.selectByUserId(userId);
            UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("查看成功");
            result.setData(new UserInfoBean(username, userInformation, null, userActivitiesInfo, userExtensionOrganization, darkroomBean));
            result.setTimestamp(System.currentTimeMillis());
        }
        return result;
    }

    /**
     * 查看用户角色
     * @param token
     * @return
     * @throws UnhandledException
     */
    public ResultBean<List<String>> getUserRole(String token) throws UnhandledException {
        ResultBean<List<String>> result = new ResultBean();
        String roles = systemUserDao.selectByUsername(JwtUtils.getUserName(token)).getRole();
        List<String> rolesList = BaseUtils.getListFromString(roles);
        List<String> roleNames = new ArrayList<>();
        for (String s : rolesList) {
            if (Long.parseLong(s) == UserRolesConstant.PERSON_USER_NUM) {
                roleNames.add(UserRolesConstant.PERSON_USER);
            } else if (Long.parseLong(s) == UserRolesConstant.ENTERPRISE_USER_NUM) {
                roleNames.add(UserRolesConstant.ENTERPRISE_USER);
            } else if (Long.parseLong(s) == UserRolesConstant.CLUB_USER_NUM) {
                roleNames.add(UserRolesConstant.CLUB_USER);
            } else if (Long.parseLong(s) == UserRolesConstant.ADMIN_NUM) {
                roleNames.add(UserRolesConstant.ADMIN);
            }
        }
        result.setStatus(HttpStatus.OK.value());
        result.setData(roleNames);
        result.setMsg("查看成功");
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 根据角色查看用户信息
     * @param roleName
     * @return
     * @throws UnhandledException
     */
    @Override
    public ResultBean<?> getUserByRole(String roleName, Integer page, Integer number, String name) throws UnhandledException {
        ResultBean result = new ResultBean<>();
        List<SystemUser> systemUsers = new ArrayList<>();
        if ("PERSON".equals(roleName)) {
            List<PersonBean> personBeans = new ArrayList<>();
            if (name == null) {
                systemUsers = systemUserDao.selectByRoleAndPageAndNumber("1;", (page-1)*number, number);
            } else if (name != null) {
                systemUsers = systemUserDao.selectByRoleAndUsernameAndPageAndNumber("1;", name,(page-1)*number, number);
            }
            Integer userCount = systemUserDao.selectByRole("1;");
            Integer pageCount = (userCount+number-1)/number;
            if (systemUsers.size() != 0) {
                for (SystemUser systemUser : systemUsers) {
                    Long userId = systemUser.getId();
                    Long darkroom = null;
                    UserInformation userInformation = userInformationDao.selectByUserId(userId);

                    Integer age = userInformation.getAge();
                    if (age == null) {
                        age = 0;
                    }
                    UserDarkroom userDarkroom = userDarkroomDao.selectByUserId(userId);
                    if (userDarkroom == null) {
                        darkroom = 0l;
                    } else if (userDarkroom != null) {
                        Long time = userDarkroom.getTime();
                        if (time < 0) {
                            darkroom = -1l;
                        } else {
                            darkroom = userDarkroom.getCreateTime()+time-System.currentTimeMillis();
                        }
                    }
                    Boolean isRealName = true;
                    PersonBean personBean = null;
                    UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
                    if (userInformation.getCardId() == null || "".equals(userInformation.getCardId())) {
                        isRealName = false;
                        personBean = new PersonBean(systemUser.getUsername(), "未实名",age,
                                userActivitiesInfo.getSuccessTimes(), userActivitiesInfo.getPoint(), isRealName, userId, darkroom);
                    } else if (userInformation.getCardId() != null && !"".equals(userInformation.getCardId())) {
                        personBean = new PersonBean(systemUser.getUsername(), userInformation.getName(), age,
                                userActivitiesInfo.getSuccessTimes(), userActivitiesInfo.getPoint(), isRealName, userId, darkroom);
                    }
                    personBeans.add(personBean);
                }
                PersonPageBean personPageBean = new PersonPageBean(personBeans, pageCount, userCount);
                result.setStatus(HttpStatus.OK.value());
                result.setMsg("查看成功");
                result.setData(personPageBean);
                result.setTimestamp(System.currentTimeMillis());
            } else {
                PersonPageBean personPageBean = new PersonPageBean(personBeans, pageCount, userCount);
                result.setStatus(HttpStatus.OK.value());
                result.setMsg("");
                result.setData(personPageBean);
                result.setTimestamp(System.currentTimeMillis());
            }

        } else if ("GROUP".equals(roleName)) {
            if (name == null) {
                systemUsers = systemUserDao.selectByRoleAndPageAndNumber("2;", (page-1)*number, number);
            } else if (name != null) {
                systemUsers = systemUserDao.selectByRoleAndUsernameAndPageAndNumber("2;", name,(page-1)*number, number);
            }
            Integer userCount = systemUserDao.selectByRole("2;");
            Integer pageCount = (userCount+number-1)/number;
            List<GroupBean> groupBeans = new ArrayList<>();
            if (systemUsers.size() != 0) {
                for (SystemUser systemUser : systemUsers) {
                    Long userId = systemUser.getId();
                    Long darkroom = null;
                    int size = activityDao.selectSuccessByUserId(userId).size();
                    UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(userId);
                    UserDarkroom userDarkroom = userDarkroomDao.selectByUserId(userId);
                    if (userDarkroom == null) {
                        darkroom = 0l;
                    } else if (userDarkroom != null) {
                        Long time = userDarkroom.getTime();
                        if (time < 0) {
                            darkroom = -1l;
                        } else {
                            darkroom = userDarkroom.getCreateTime()+time-System.currentTimeMillis();
                        }
                    }
                    UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
                    GroupBean groupBean = new GroupBean(systemUser.getUsername(), userExtensionOrganization.getTeamName(), userExtensionOrganization.getOrganizationType(), userExtensionOrganization.getBusinessScope(),
                            size, userActivitiesInfo.getSuccessTimes(), userActivitiesInfo.getPoint(), userId, darkroom);
                    groupBeans.add(groupBean);
                }
                GroupPageBean groupPageBean = new GroupPageBean(groupBeans, pageCount, userCount);
                result.setStatus(HttpStatus.OK.value());
                result.setMsg("查看成功");
                result.setData(groupPageBean);
                result.setTimestamp(System.currentTimeMillis());
            } else {
                GroupPageBean groupPageBean = new GroupPageBean(groupBeans, pageCount, userCount);
                result.setStatus(HttpStatus.OK.value());
                result.setMsg("");
                result.setData(groupPageBean);
                result.setTimestamp(System.currentTimeMillis());
            }

        } else if ("CLUB".equals(roleName)) {
            if (name == null) {
                systemUsers = systemUserDao.selectByRoleAndPageAndNumber("3;", (page-1)*number, number);
            } else if (name != null) {
                systemUsers = systemUserDao.selectByRoleAndUsernameAndPageAndNumber("3;", name,(page-1)*number, number);
            }
            Integer userCount = systemUserDao.selectByRole("3;");
            Integer pageCount = (userCount+number-1)/number;
            List<ClubBean> clubBeans = new ArrayList<>();
            if (systemUsers.size() != 0) {
                for (SystemUser systemUser : systemUsers) {
                    Long userId = systemUser.getId();
                    Long darkroom = null;
                    int size = activityDao.selectSuccessByUserId(userId).size();
                    UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(userId);
                    UserDarkroom userDarkroom = userDarkroomDao.selectByUserId(userId);
                    if (userDarkroom == null) {
                        darkroom = 0l;
                    } else if (userDarkroom != null) {
                        Long time = userDarkroom.getTime();
                        if (time < 0) {
                            darkroom = -1l;
                        } else {
                            darkroom = userDarkroom.getCreateTime()+time-System.currentTimeMillis();
                        }
                    }
                    UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(userId);
                    ClubBean clubBean = new ClubBean(systemUser.getUsername(), userExtensionOrganization.getTeamName(), userExtensionOrganization.getBusinessScope(), size,
                            userActivitiesInfo.getSuccessTimes(), userActivitiesInfo.getPoint(), userId, darkroom);
                    clubBeans.add(clubBean);
                }
                ClubPageBean clubPageBean = new ClubPageBean(clubBeans, pageCount, userCount);
                result.setStatus(HttpStatus.OK.value());
                result.setMsg("查看成功");
                result.setData(clubPageBean);
                result.setTimestamp(System.currentTimeMillis());
            } else {
                ClubPageBean clubPageBean = new ClubPageBean(clubBeans, pageCount, userCount);
                result.setStatus(HttpStatus.OK.value());
                result.setMsg("");
                result.setData(clubPageBean);
                result.setTimestamp(System.currentTimeMillis());
            }
        }
        return result;
    }
}
