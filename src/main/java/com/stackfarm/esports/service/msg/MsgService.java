package com.stackfarm.esports.service.msg;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import java.util.Map;

/**
 * @author croton
 * @create 2021/4/2 20:31
 */
public interface MsgService {

    ResultBean<?> verCodeSendRegister(String info) throws UnhandledException, TencentCloudSDKException;

    ResultBean<?> verCodeSendReset(String info) throws UnhandledException, TencentCloudSDKException;

    ResultBean<?> inviteCodeSend(Long userId, Map<String, String> params);
}
