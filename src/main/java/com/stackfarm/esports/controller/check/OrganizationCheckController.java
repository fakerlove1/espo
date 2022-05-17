package com.stackfarm.esports.controller.check;

import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.check.OrganizationCheckService;
import com.stackfarm.esports.system.UserRolesConstant;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author croton
 * @create 2021/4/2 17:18
 */
@RestController
public class OrganizationCheckController {

    @Autowired
    private OrganizationCheckService organizationCheckService;

    /**
     * 企业/俱乐部审核报名活动的用户
     * @param userId
     * @param isPassed
     * @param cause
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PutMapping("/check/user/act")
    public ResultBean<?> checkUserActivity(@RequestParam("userId") Long userId,
                                           @RequestParam("actId") Long actId,
                                           @RequestParam("isPassed") Boolean isPassed,
                                           @RequestParam(value = "cause", required = false) String cause) throws TencentCloudSDKException {
        return organizationCheckService.checkUserActivity(userId, actId, isPassed, cause);
    }

    /**
     * 企业/俱乐部获取某一活动全部报名申请信息
     * @param actId
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @GetMapping("/check/user/get/{actId}")
    public ResultBean<?> getUserActivity(@PathVariable("actId") Long actId) {
        return organizationCheckService.getUserActivity(actId);
    }

    /**
     * 企业/俱乐部审核用户活动情况，是否完成和完成情况
     * @param actId
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PutMapping("/check/user/act/state")
    public ResultBean<?> checkUserActivityState(@RequestParam("userId") Long userId,
                                                @RequestParam("actId") Long actId,
                                                @RequestParam("state") Boolean state,
                                                @RequestParam(value = "comment", required = false) String comment) {
        return organizationCheckService.checkUserActivityState(userId, actId, state, comment);
    }


    /**
     * 企业/俱乐部审核用户退出申请
     * @param actId
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PutMapping("/check/user/act/cancel")
    public ResultBean<?> checkUserCancelActivity(@RequestParam("userId") Long userId,
                                                 @RequestParam("actId") Long actId,
                                                 @RequestParam("isPassed") Boolean isPassed,
                                                 @RequestParam(value = "cause", required = false) String cause) {
        return null;
    }
}
