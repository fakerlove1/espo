package com.stackfarm.esports.service.msg.Impl;

import com.stackfarm.esports.dao.user.UserExtensionOrganizationDao;
import com.stackfarm.esports.dao.user.UserExtensionPersonDao;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.user.UserExtensionOrganization;
import com.stackfarm.esports.pojo.user.UserExtensionPerson;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.msg.MsgService;
import com.stackfarm.esports.system.SMSSenderConstant;
import com.stackfarm.esports.utils.BaseUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author croton
 * @create 2021/4/2 20:32
 */
@Service
@Transactional
public class MsgServiceImpl implements MsgService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserExtensionOrganizationDao userExtensionOrganizationDao;
    @Autowired
    private UserExtensionPersonDao userExtensionPersonDao;
    /**
     * 发送注册验证码
     * @param info
     * @return
     */
    @Override
    public ResultBean<Void> verCodeSendRegister(String info) throws UnhandledException, TencentCloudSDKException {
        ResultBean<Void> result = new ResultBean<>();
        if (BaseUtils.isMobile(info)) {
            UserExtensionPerson person = userExtensionPersonDao.selectByPhoneNumber(Long.parseLong(info));
            if (person != null) {
                throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "手机号已被注册",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
            }
        } else if (BaseUtils.isEmail(info)) {
            UserExtensionOrganization organization = userExtensionOrganizationDao.selectByEmail(info);
            if (organization != null) {
                throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "邮箱已被注册",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
            }
        }
        if (redisTemplate.hasKey("flag_" + info)) {
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setMsg("请不要频繁发送");
            result.setData(null);
            result.setTimestamp(System.currentTimeMillis());
            return result;
        } else if (!redisTemplate.hasKey("flag_" + info) && redisTemplate.hasKey("VER_CODE_" + info)) {
            redisTemplate.delete("VER_CODE_" + info);
        }
        String verCode = BaseUtils.sendVerCode(info, SMSSenderConstant.SMS_TEMPLATE_REGISTER);
        if (verCode != null) {
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("发送成功");
            result.setData(null);
            result.setTimestamp(System.currentTimeMillis());
            redisTemplate.opsForValue().set("VER_CODE_" + info, verCode, 10, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set("flag_" + info, 1, 60, TimeUnit.SECONDS);
        } else {
            result.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            result.setMsg("发送失败！请重试");
            result.setData(null);
            result.setTimestamp(System.currentTimeMillis());
        }
        return result;
    }

    /**
     * 发送重置密码验证码
     * @param info
     * @return
     */
    @Override
    public ResultBean<Void> verCodeSendReset(String info) throws UnhandledException, TencentCloudSDKException {
        ResultBean<Void> result = new ResultBean<>();
        if (BaseUtils.isMobile(info)) {
            UserExtensionPerson person = userExtensionPersonDao.selectByPhoneNumber(Long.parseLong(info));
            if (person == null) {
                throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "手机号未注册",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
            }
        } else if (BaseUtils.isEmail(info)) {
            UserExtensionOrganization organization = userExtensionOrganizationDao.selectByEmail(info);
            if (organization == null) {
                throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "邮箱未注册",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
            }
        }
        if (redisTemplate.hasKey("flag_" + info)) {
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setMsg("请不要频繁发送");
            result.setData(null);
            result.setTimestamp(System.currentTimeMillis());
            return result;
        } else if (!redisTemplate.hasKey("flag_" + info) && redisTemplate.hasKey("VER_CODE_" + info)) {
            redisTemplate.delete("VER_CODE_" + info);
        }
        String verCode = BaseUtils.sendVerCode(info, SMSSenderConstant.SMS_TEMPLATE_RESET);
        if (verCode != null) {
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("发送成功");
            result.setData(null);
            result.setTimestamp(System.currentTimeMillis());
            redisTemplate.opsForValue().set("VER_CODE_" + info, verCode, 10, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set("flag_" + info, 1, 60, TimeUnit.SECONDS);
        } else {
            result.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            result.setMsg("发送失败！请重试");
            result.setData(null);
            result.setTimestamp(System.currentTimeMillis());
        }
        return result;
    }

    /**
     * 发送邀请码
     * @param userId
     * @param params
     * @return
     */
    @Override
    public ResultBean<?> inviteCodeSend(Long userId, Map<String, String> params) {
        return null;
    }


}
