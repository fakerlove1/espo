package com.stackfarm.esports.controller.user;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.user.UserInfoService;
import com.stackfarm.esports.system.UserRolesConstant;
import com.stackfarm.esports.utils.JwtUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author croton
 * @create 2021/4/5 12:32
 */
@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 修改个人信息
     * @param name
     * @param cardId
     * @param preferPosition
     * @param experience
     * @param email
     * @param qq
     * @param introduction
     * @param profile
     * @return
     */
    @RequiresRoles(UserRolesConstant.PERSON_USER)
    @PostMapping("/info/user/update")
    public ResultBean<?> updateUserInformation(@RequestParam("userId") Long useId,
                                               @RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "cardId", required = false) String cardId,
                                               @RequestParam(value = "age", required = false) Integer age,
                                               @RequestParam(value = "sex", required = false) String sex,
                                               @RequestParam(value = "nation", required = false) String nation,
                                               @RequestParam(value = "birthday", required = false) Long birthday,
                                               @RequestParam(value = "home", required = false) String home,
                                               @RequestParam(value = "preferPosition", required = false) String preferPosition,
                                               @RequestParam(value = "experience", required = false) String experience,
                                               @RequestParam(value = "email", required = false) String email,
                                               @RequestParam(value = "qq", required = false) String qq,
                                               @RequestParam(value = "introduction", required = false) String introduction,
                                               @RequestParam(value = "education", required = false) String education,
                                               @RequestParam(value = "profile", required = false) MultipartFile profile) throws UnhandledException {
        return userInfoService.updateUserInformation(useId, name, cardId, age, sex,
                nation, birthday, home, preferPosition, experience, email, qq, introduction, education, profile);
    }

    /**
     * 查看个人信息
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.PERSON_USER, UserRolesConstant.ADMIN}, logical = Logical.OR)
    @GetMapping("/info/user/get/{userId}")
    public ResultBean<?> getUserInformation(@PathVariable("userId") Long userId, HttpServletRequest request) throws UnhandledException {
        String token = request.getHeader("token");
        return userInfoService.getUserInformation(JwtUtils.getUserId(token));
    }

    /**
     * 根据id查看个人信息
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.PERSON_USER, UserRolesConstant.ADMIN}, logical = Logical.OR)
    @PostMapping("/info/user/select")
    public ResultBean<?> selectUserInformation(@RequestParam("userId") Long userId) throws UnhandledException {
        return userInfoService.getUserInformation(userId);
    }


    /**
     * 查看用户角色
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.PERSON_USER, UserRolesConstant.ADMIN}, logical = Logical.OR)
    @GetMapping("/info/user/get/role")
    public ResultBean<?> getUserRole(HttpServletRequest request) throws UnhandledException {
        String token = request.getHeader("token");
        return userInfoService.getUserRole(token);
    }

    /**
     * 根据角色查看用户信息
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/info/user/getByRole/{roleName}/{page}/{number}")
    public ResultBean<?> getUserByRole(@PathVariable("roleName") String roleName, @PathVariable("page") Integer page, @PathVariable("number") Integer number, @RequestParam(value = "username", required = false) String username) throws UnhandledException {
        return userInfoService.getUserByRole(roleName, page, number, username);
    }


}
