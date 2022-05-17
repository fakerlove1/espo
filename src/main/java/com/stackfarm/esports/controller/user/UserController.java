package com.stackfarm.esports.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackfarm.esports.dao.user.SystemUserDao;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.user.UserService;
import com.stackfarm.esports.utils.BaseUtils;
import com.sun.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @author croton
 * @create 2021/4/1 17:46
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     * @throws UnhandledException
     */
    @PutMapping("/user/login")
    public ResultBean<?> login(@RequestParam("username") String username,
                               @RequestParam("password") String password) throws UnhandledException {
        BaseUtils.getValueWithThrow(username, BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
        BaseUtils.getValueWithThrow(password, BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
        return userService.login(username, password);
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
    @PostMapping("/user/register")
    public ResultBean<?> register(@RequestParam("username") String username, @RequestParam("password")String password,
                                  @RequestParam("info") String info,
                                  @RequestParam("verificationCode")String verificationCode,
                                  @RequestParam(value = "invitationCode", required = false) String invitationCode,
                                  @RequestParam(value = "type", required = false) Long type,
                                  @RequestParam(value = "organizationName", required = false) String organizationName,
                                  @RequestParam(value = "organizationType", required = false) String organizationType,
                                  @RequestParam(value = "organizationCode", required = false) String organizationCode,
                                  @RequestParam(value = "businessScope", required = false) String businessScope,
                                  @RequestParam(value = "tel", required = false) String tel,
                                  @RequestParam(value = "website", required = false) String website,
                                  @RequestParam(value = "officialId", required = false) String officialId,
                                  @RequestParam(value = "weiBo", required = false) String weiBo,
                                  @RequestParam(value = "address", required = false) String address,
                                  @RequestParam(value = "introduction", required = false) String introduction,
                                  @RequestParam(value = "profile", required = false) MultipartFile profile) throws IOException, UnhandledException {
        return userService.register(username, password, info, verificationCode, invitationCode, type, organizationName, organizationType, organizationCode, businessScope, tel, website, officialId, weiBo, address, introduction, profile);
    }

    /**
     * 找回密码
     * @param info
     * @param verificationCode
     * @param newPwd
     * @return
     */
    @PutMapping("/user/findPassword")
    public ResultBean<?> findPassword(@RequestParam("info") String info, @RequestParam("verificationCode") String verificationCode,
                                      @RequestParam("newPwd") String newPwd) throws UnhandledException {
        return userService.findPassword(info, verificationCode, newPwd);
    }



}
