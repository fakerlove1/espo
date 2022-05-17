package com.stackfarm.esports.controller.check;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.check.AdminCheckService;
import com.stackfarm.esports.system.SystemConstant;
import com.stackfarm.esports.system.UserRolesConstant;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author croton
 * @create 2021/4/2 16:46
 */
@RestController
public class AdminCheckController {

    @Autowired
    private AdminCheckService adminCheckService;
    /**
     * 管理员审核用户
     * @param userId
     * @param isPassed
     * @param cause
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PutMapping("/check/user")
    public ResultBean<?> checkUser(@RequestParam("userId") Long userId, @RequestParam("isPassed") Boolean isPassed,
                                   @RequestParam(value = "cause", required = false) String cause) throws TencentCloudSDKException {
        return adminCheckService.checkUser(userId, isPassed, cause);
    }


    /**
     * 获取全部用户审核信息
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/check/user/get")
    public ResultBean<?> getUserApplications(@RequestParam(value = "username", required = false) String username) {
        return adminCheckService.getUserApplications(username);
    }

    /**
     * 管理员审核活动
     * @param actId
     * @param isPassed
     * @param cause
     * @param level
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PutMapping("/check/act")
    public ResultBean<?> checkActivities(@RequestParam("actId") Long actId, @RequestParam("isPassed") Boolean isPassed,
                                         @RequestParam(value = "cause", required = false) String cause, @RequestParam(value = "level", required = false) Integer level) throws TencentCloudSDKException {
        return adminCheckService.checkActivities(actId, isPassed, cause, level);
    }

    /**
     * 获取全部活动审核信息
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @GetMapping("/check/act/get")
    public ResultBean<?> getActivityApplications() throws UnhandledException {
        return adminCheckService.getActivityApplications();
    }

    /**
     * 管理员审核活动撤销申请
     * @param actId
     * @param isPassed
     * @param cause
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PutMapping("/check/act/cancel")
    public ResultBean<?> checkCancelActivity(@RequestParam("actId") Long actId,
                                             @RequestParam("isPassed") Boolean isPassed,
                                             @RequestParam(value = "cause", required = false) String cause) throws TencentCloudSDKException {
        return adminCheckService.checkCancelActivity(actId, isPassed, cause);
    }

    /**
     * 管理员查看全部待撤销活动申请
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @GetMapping("/check/act/cancel/get")
    public ResultBean<?> getAllCancelActivity() {
        return adminCheckService.getAllCancelActivity();
    }

    /**
     * 管理员查看全部活动情况
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/admin/getAll")
    public ResultBean<?> getAllActivities(@RequestParam("page") Integer page,
                                          @RequestParam("number") Integer number,
                                          @RequestParam(value = "name", required = false) String name) {
        return adminCheckService.getAllActivities(page, number, name);
    }
    /**
     * 管理员拉黑/解禁用户
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/admin/blacklist/user")
    public ResultBean<?> blackListUser(@RequestParam("userId") Long userId,@RequestParam("isBlacklist") Boolean isBlacklist
            ,@RequestParam(value = "time", required = false) Long time,@RequestParam("cause")String cause) throws TencentCloudSDKException {
        return adminCheckService.blackListUser(userId, isBlacklist, time, cause);
    }
}
