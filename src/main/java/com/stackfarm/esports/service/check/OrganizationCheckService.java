package com.stackfarm.esports.service.check;

import com.stackfarm.esports.result.ResultBean;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author xiaohuang
 * @create 2021/4/3 17:26
 */
@Service
public interface OrganizationCheckService {

    ResultBean<?> checkUserActivity(Long userId, Long actId, Boolean isPassed, String cause) throws TencentCloudSDKException;

    ResultBean<?> getUserActivity(Long actId);

    ResultBean<?> checkUserActivityState(@RequestParam("userId") Long userId,
                                         @RequestParam("actId") Long actId,
                                         @RequestParam("state") Boolean state,
                                         @RequestParam("comment") String comment);

}
