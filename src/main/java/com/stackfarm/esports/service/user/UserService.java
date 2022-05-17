package com.stackfarm.esports.service.user;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author croton
 * @create 2021/4/1 17:55
 */
public interface UserService {

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    ResultBean<?> login(String username, String password);

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
    ResultBean<?> register(String username, String password, String info,
                           String verificationCode,
                           String invitationCode, Long type,
                           String organizationName,
                           String organizationType, String organizationCode,
                           String businessScope, String tel, String website,
                           String officialId, String weiBo, String address,
                           String introduction, MultipartFile profile) throws UnhandledException, IOException;

    /**
     * 找回密码
     * @param info
     * @param verificationCode
     * @param newPwd
     * @return
     */
    ResultBean<?> findPassword(String info, String verificationCode, String newPwd) throws UnhandledException;
}
