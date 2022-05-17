package com.stackfarm.esports.service.user.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.stackfarm.esports.dao.user.*;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.user.*;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.user.UserService;
import com.stackfarm.esports.system.SystemConstant;
import com.stackfarm.esports.system.UserRolesConstant;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.EncryptDecryptUtils;
import com.stackfarm.esports.utils.FileUtils;
import com.stackfarm.esports.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * @author croton
 * @create 2021/4/1 17:56
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

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
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResultBean<Token> login(String username, String password) {
        ResultBean<Token> result = new ResultBean<>();
        SystemUser user = null;
        if (BaseUtils.isMobile(username)) {
            UserExtensionPerson userExtensionPerson = userExtensionPersonDao.selectByPhoneNumber(Long.parseLong(username));
            if (userExtensionPerson != null) {
                user = systemUserDao.selectById(userExtensionPerson.getUserId());
            }
        } else if (BaseUtils.isEmail(username)) {
            UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByEmail(username);
            if (userExtensionOrganization != null) {
                user = systemUserDao.selectById(userExtensionOrganization.getUserId());
            }
        } else {
            user = systemUserDao.selectByUsername(username);
        }
        if (user == null) {
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setData(null);
            result.setMsg("用户未注册！");
            result.setTimestamp(System.currentTimeMillis());
        } else {
            if (!user.getPassword().equals(EncryptDecryptUtils.encryptMD5(password, user.getSalt()))){
                result.setStatus(HttpStatus.UNAUTHORIZED.value());
                result.setData(null);
                result.setMsg("密码错误！");
                result.setTimestamp(System.currentTimeMillis());
            } else if (user.getState() == false) {
                result.setStatus(HttpStatus.BAD_REQUEST.value());
                result.setData(null);
                result.setMsg("身份审核中...暂时无法操作!");
                result.setTimestamp(System.currentTimeMillis());
            } else {
                result.setStatus(HttpStatus.OK.value());
                result.setData(new Token(JwtUtils.sign(username, password)));
                result.setMsg("登录成功！");
                result.setTimestamp(System.currentTimeMillis());
            }
        }
        return result;
    }

    /**
     * 注册
     * @param username
     * @param password
     * @param info
     * @param verificationCode
     * @param invitationCode
     * @param organizationName
     * @param organizationType
     * @param organizationCode
     * @param businessScope
     * @param tel
     * @param website
     * @param officialId
     * @param weiBo
     * @param address
     * @param introduction
     * @param profile
     * @return
     */
    @Override
    public ResultBean<Void> register(String username, String password, String info,
                                     String verificationCode,
                                     String invitationCode, Long type,
                                     String organizationName,
                                     String organizationType, String organizationCode,
                                     String businessScope, String tel, String website,
                                     String officialId, String weiBo, String address,
                                     String introduction, MultipartFile profile) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();
        SystemUser user = systemUserDao.selectByUsername(username);
        if (type == 4l) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "越权！",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
        }
        if (BaseUtils.isEmail(info)) {
            if (organizationName == null || organizationCode == null || businessScope == null
                    || tel == null || address == null || introduction == null || profile ==null) {
                throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "请仔细填写必填信息！",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
            }
            if (type == 2) {
                if (organizationType == null) {
                    throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "请仔细填写必填信息！",
                            BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
                }
            }
        }
        if(user != null) {
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setData(null);
            result.setMsg("用户名已存在！");
            result.setTimestamp(System.currentTimeMillis());
        } else {
            // 进行验证码验证
            String verCodeCache = redisTemplate.opsForValue().get("VER_CODE_" + info) + "";
            if ("".equals(verCodeCache) || "null".equals(verCodeCache) || !verCodeCache.equals(verificationCode)) {
                throw new UnhandledException(HttpStatus.BAD_REQUEST.value(),
                        "验证码错误或已过期，请检查验证码或尝试重新发送！",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                        new Date());
            }
            //用户基本信息
            String salt = BaseUtils.getSalt();
            SystemUser newUser = new SystemUser();
            newUser.setSalt(salt);
            newUser.setPassword(EncryptDecryptUtils.encryptMD5(password, salt));
            newUser.setUsername(username);
            newUser.setNickname(username);
            if (BaseUtils.isMobile(info)) {
                newUser.setRole(UserRolesConstant.PERSON_USER_NUM.toString()+";");
                newUser.setState(true);
            } else if (BaseUtils.isEmail(info)) {
                newUser.setRole(UserRolesConstant.UNCHECKED_ORGANIZATION_NUM.toString()+";");
                newUser.setState(false);
            }
            newUser.setCreateTime(System.currentTimeMillis());
            newUser.setUpdateTime(System.currentTimeMillis());
            systemUserDao.insert(newUser);
            Long id = systemUserDao.selectByUsername(username).getId();
            //用户拓展信息
            //个人用户
            if (BaseUtils.isMobile(info)) {
                UserExtensionPerson person = new UserExtensionPerson();
                person.setInvitationCode(invitationCode);
                person.setUserId(id);
                person.setPhoneNumber(Long.parseLong(info));
                userExtensionPersonDao.insert(person);
                //新增用户个人信息
                UserInformation userInformation = new UserInformation();
                userInformation.setUserId(id);
                userInformation.setCreateTime(System.currentTimeMillis());
                userInformation.setUpdateTime(System.currentTimeMillis());
                userInformationDao.insert(userInformation);
                //新增用户-相关活动信息
                UserActivitiesInfo userActivitiesInfo = new UserActivitiesInfo();
                userActivitiesInfo.setUserId(id);
                userActivitiesInfo.setPoint(0);
                userActivitiesInfo.setTimes(0);
                userActivitiesInfo.setSuccessTimes(0);
                userActivitiesInfoDao.insert(userActivitiesInfo);
            } else if (BaseUtils.isEmail(info)) {
                //团体用户
                UserExtensionOrganization organization = new UserExtensionOrganization();
                organization.setUserId(id);
                organization.setEmail(info);
                organization.setType(type);
                organization.setTeamName(organizationName);
                organization.setOrganizationType(organizationType);
                organization.setOrganizationCode(organizationCode);
                organization.setBusinessScope(businessScope);
                organization.setTel(tel);
                organization.setWebsite(website);
                organization.setOfficialCode(officialId);
                organization.setWeibo(weiBo);
                organization.setAddress(address);
                organization.setIntroduction(introduction);
                //资质证书上传
                if (profile == null) {
                    result.setStatus(HttpStatus.BAD_REQUEST.value());
                    result.setMsg("资质证书不可为空！");
                    result.setData(null);
                    result.setTimestamp(System.currentTimeMillis());
                    return result;
                }
                String fileName = profile.getOriginalFilename();
                String newFileName = organizationName + " 资质证书" + fileName.substring(fileName.lastIndexOf("."));
                try {
                    profile.transferTo(FileUtils.forceCreateFile(SystemConstant.FILE_ROOT_PATH + File.separator
                                                                        + "organizationProfiles" + File.separator + newFileName));
                } catch (IOException e) {
                    throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "转储文件时发生错误", BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
                }
                organization.setProfile(SystemConstant.FILE_ROOT_PATH + File.separator
                        + "organizationProfiles" + File.separator + newFileName);
                userExtensionOrganizationDao.insert(organization);
            }
            //用户-角色信息
            SystemUserRoles userRoles = new SystemUserRoles();
            userRoles.setUserId(id);
            userRoles.setCreatedTime(System.currentTimeMillis());
            userRoles.setUpdatedTime(System.currentTimeMillis());
            if (BaseUtils.isMobile(info)) {
                userRoles.setRoleId(UserRolesConstant.PERSON_USER_NUM);
            } else if (BaseUtils.isEmail(info)) {
                userRoles.setRoleId(UserRolesConstant.UNCHECKED_ORGANIZATION_NUM);
            }
            userRoles.setAvailable(true);
            systemUserRolesDao.insert(userRoles);
            result.setStatus(HttpStatus.OK.value());
            result.setData(null);
            result.setMsg("注册成功");
            result.setTimestamp(System.currentTimeMillis());
        }
        return result;
    }

    /**
     * 找回密码
     * @param info
     * @param verificationCode
     * @param newPwd
     * @return
     */
    @Override
    public ResultBean<Void> findPassword(String info, String verificationCode, String newPwd) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();
        String verCodeCache = redisTemplate.opsForValue().get("VER_CODE_" + info) + "";
        if ("null".equals(verCodeCache) || "".equals(verCodeCache) || !verCodeCache.equals(verificationCode)) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "验证码错误", BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
        }
        String salt = BaseUtils.getSalt();
        if (BaseUtils.isEmail(info)) {
            Long userId = userExtensionOrganizationDao.selectByEmail(info).getUserId();
            SystemUser user = systemUserDao.selectById(userId);
            user.setPassword(EncryptDecryptUtils.encryptMD5(newPwd, salt));
            user.setSalt(salt);
            systemUserDao.update(user);
        } else if (BaseUtils.isMobile(info)) {
            Long userId = userExtensionPersonDao.selectByPhoneNumber(Long.parseLong(info)).getUserId();
            SystemUser user = systemUserDao.selectById(userId);
            user.setPassword(EncryptDecryptUtils.encryptMD5(newPwd, salt));
            user.setSalt(salt);
            systemUserDao.update(user);
        } else {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "邮箱或手机号格式错误", BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("修改成功！");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * token私有类
     */
    private static class Token {

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Token(String token) {
            this.token = token;
        }
    }
}
