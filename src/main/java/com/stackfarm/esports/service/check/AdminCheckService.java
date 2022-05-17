package com.stackfarm.esports.service.check;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.system.UserRolesConstant;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author croton
 * @create 2021/4/3 20:37
 */
@Service
public interface AdminCheckService {

    /**
     * 管理员审核用户
     * @param userId
     * @param isPassed
     * @param cause
     * @return
     */
    ResultBean<?> checkUser(Long userId, Boolean isPassed, String cause) throws TencentCloudSDKException;


    /**
     * 获取全部用户审核信息
     * @return
     */
    ResultBean<?> getUserApplications(String username);

    /**
     * 管理员审核活动
     * @param actId
     * @param isPassed
     * @param cause
     * @param level
     * @return
     */
    ResultBean<?> checkActivities(Long actId, Boolean isPassed, String cause, Integer level) throws TencentCloudSDKException;

    /**
     * 获取全部待审核活动
     * @return
     */
    ResultBean<?> getActivityApplications() throws UnhandledException;

    /**
     * 管理员审核活动撤销申请
     * @param actId
     * @param isPassed
     * @param cause
     * @return
     */
    ResultBean<?> checkCancelActivity(Long actId, Boolean isPassed, String cause) throws TencentCloudSDKException;

    /**
     * 管理员获得所有活动信息
     * @return
     */
    ResultBean<?> getAllActivities(Integer page, Integer number, String name);

    /**
     * 管理员获取全部待撤销活动
     * @return
     */
    ResultBean<?> getAllCancelActivity();

    /**
     * 管理员拉黑/解禁用户
     * @return
     */
    ResultBean<?> blackListUser(Long userId, Boolean isBlacklist, Long time, String cause) throws TencentCloudSDKException;
}
