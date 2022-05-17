package com.stackfarm.esports.controller.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.activity.OrganizationActivitiesService;
import com.stackfarm.esports.system.UserRolesConstant;
import com.stackfarm.esports.utils.JwtUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author croton
 * @create 2021/4/2 13:12
 */
@RestController
public class OrganizationActivityController {
    @Autowired
    private OrganizationActivitiesService organizationActivitiesService;
    /**
     * 发布活动, 企业/俱乐部专有
     * @param actName
     * @param actType
     * @param enrollBeginTime
     * @param enrollEndTime
     * @param holdBeginTime
     * @param holdEndTime
     * @param staffTypes
     * @param staffCount
     * @param jobRequirement
     * @param cost
     * @param reward
     * @param actAddress
     * @param detailAddress
     * @param contactWay
     * @param actScope
     * @param actInformation
     * @param poster
     * @param pictures
     * @param actPlan
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PostMapping("/act/launch")
    public ResultBean<?> launchActivity(HttpServletRequest request, @RequestParam("actName") String actName,
                                        @RequestParam("actType") String actType, @RequestParam("enrollBeginTime") Long enrollBeginTime,
                                        @RequestParam("enrollEndTime") Long enrollEndTime, @RequestParam("holdBeginTime") Long holdBeginTime,
                                        @RequestParam("holdEndTime") Long holdEndTime, @RequestParam("staffTypes") String staffTypes,
                                        @RequestParam("staffCount") String staffCount,
                                        @RequestParam(value = "jobRequirement", required = false) String jobRequirement, @RequestParam("cost") BigDecimal cost,
                                        @RequestParam("reward") BigDecimal reward, @RequestParam("actAddress") String actAddress,
                                        @RequestParam("detailAddress") String detailAddress, @RequestParam("contactWay") String contactWay,
                                        @RequestParam("actScope") String actScope, @RequestParam(value = "actInformation", required = false) String actInformation,
                                        @RequestParam("poster") MultipartFile poster, @RequestParam(value = "picture", required = false) MultipartFile[] pictures,
                                        @RequestParam(value = "actPlan", required = false) MultipartFile actPlan) throws UnhandledException, IOException {
        String token = request.getHeader("token");
        return organizationActivitiesService.launchActivity(token, actName, actType, enrollBeginTime, enrollEndTime,holdBeginTime,holdEndTime,
                staffTypes,staffCount,jobRequirement,cost,reward,actAddress,detailAddress,contactWay,actScope,actInformation,poster,
                pictures,actPlan);
    }

    /**
     * 撤销活动，企业/俱乐部专有
     * @param actId
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @DeleteMapping("/act/delete/{actId}")
    public ResultBean<?> cancelActivity(@PathVariable("actId") Long actId, @RequestParam("reason") String reason) {
        return organizationActivitiesService.cancelActivity(actId, reason);
    }

    /**
     * 查看某一活动详情，企业/俱乐部专有
     * @param actId
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @GetMapping("/act/get/{actId}")
    public ResultBean<?> getActInformation(@PathVariable("actId") Long actId) {
        return organizationActivitiesService.getActInformation(actId);
    }

    /**
     * 查看自己发布的某一活动的全部报名信息，企业/俱乐部专有
     * @param actId
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @GetMapping("/act/get/enroll/{actId}")
    public ResultBean<?> getActEnrollInformation(@PathVariable("actId") Long actId) {
        return organizationActivitiesService.getActEnrollInformation(actId);
    }

    /**
     * 查看自己发布的全部活动详情，企业/俱乐部专有
     * @param userId
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.ADMIN}, logical = Logical.OR)
    @GetMapping("/act/get/release/{userId}")
    public ResultBean<?> getAllActInformation(@PathVariable("userId") Long userId) {
        return organizationActivitiesService.getAllActInformation(userId);
    }

    /**
     * 报名活动
     * @param actId
     * @param teamName
     * @param preferPosition
     * @param preferPositionCount
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.PERSON_USER}, logical = Logical.OR)
    @PostMapping("/act/user/enroll")
    public ResultBean<?> enrollActivity(@RequestParam("actId") Long actId, @RequestParam(value = "teamName", required = false) String teamName,
                                        @RequestParam(value = "preferPosition", required = false) String preferPosition,
                                        @RequestParam(value = "preferPositionCount", required = false) String preferPositionCount,
                                        HttpServletRequest request) throws UnhandledException {
        String token = request.getHeader("token");
        String tn = "";
        String pp = "";
        String ppc = "";
        if (teamName != null) {
            tn = teamName;
        } else {
            tn = JwtUtils.getUserName(token);
        }
        if (preferPosition != null) {
            pp = preferPosition;
        } else {
            pp = "无";
        }
        if (preferPositionCount != null) {
            ppc = preferPositionCount;
        } else {
            ppc = "1";
        }
        return organizationActivitiesService.enrollActivity(actId, tn, pp, ppc);
    }


    /**
     * 退出活动
     * @param userId
     * @param actId
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.PERSON_USER}, logical = Logical.OR)
    @DeleteMapping("/act/user/exit")
    public ResultBean<?> exitActivity(@RequestParam("userId") Long userId, @RequestParam("actId") Long actId) {
        return organizationActivitiesService.exitActivity(userId, actId);
    }


    /**
     * 查看自己可见范围内所有活动的信息
     * @param userId
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.PERSON_USER}, logical = Logical.OR)
    @PostMapping("/act/user/get")
    public ResultBean<?> getAllActivities(@RequestParam("userId") Long userId, @RequestParam(value = "name", required = false) String name) {
        return organizationActivitiesService.getAllActivities(userId, name);
    }


    /**
     * 用户查看自己全部参与信息情况，包括已审核/未审核/报名中等
     * @param userId
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.PERSON_USER}, logical = Logical.OR)
    @GetMapping("/act/get/join/{userId}")
    public ResultBean<?> getUserActInformation(@PathVariable("userId") Long userId) {
        return organizationActivitiesService.getUserActInformation(userId);
    }



}
