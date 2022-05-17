package com.stackfarm.esports.service.activity;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @Author xiaohuang
 * @create 2021/4/1 20:51
 */
@Service
public interface OrganizationActivitiesService {

    /**
     * 发布活动（企业/俱乐部专有）
     */
    ResultBean<?> launchActivity(String token, String actName, String actType, Long enrollBeginTime,
                                 Long enrollEndTime, Long holdBeginTime, Long holdEndTime, String staffTypes,
                                 String staffCount, String jobRequirement, BigDecimal cost, BigDecimal reward,
                                 String actAddress, String detailAddress, String contactWay, String actScope,
                                 String actInformation, MultipartFile poster, MultipartFile[] pictures, MultipartFile actPlan) throws UnhandledException, IOException;

    /**
     * 撤销活动（企业/俱乐部专有）
     */
    ResultBean<?> cancelActivity(Long actId, String reason);


    /**
     * 查看活动情况（企业/俱乐部专有）
     */
    ResultBean<?> getActInformation(Long actId);

    /**
     * 参加活动
     */
    ResultBean<?> enrollActivity(Long actId, String teamName, String preferPosition, String preferPositionCount) throws UnhandledException;

    /**
     * 退出活动
     */
    ResultBean<?> exitActivity(Long userId, Long actId);

    /**
     * 查看自己可见范围内所有活动的信息
     */
    ResultBean<?> getAllActivities(Long userId, String name);

    /**
     * 用户查看自己全部参与信息情况，包括已审核/未审核
     */
    ResultBean<?> getUserActInformation(Long userId);

    /**
     * 企业查看所有自己发布的活动
     */
    ResultBean<?> getAllActInformation(Long userId);

    /**
     * 企业查看某一活动的报名信息
     */
    ResultBean<?> getActEnrollInformation(Long actId);
}
