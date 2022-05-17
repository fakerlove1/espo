package com.stackfarm.esports.controller.msg;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.msg.MsgService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author croton
 * @create 2021/4/2 20:13
 */
@RestController
public class MsgController {

    @Autowired
    private MsgService msgService;
    /**
     * 发送验证码
     */
    @PostMapping("/msg/ext/ver/register")
    public ResultBean<?> verCodeSendRegister(@RequestParam("info") String info) throws UnhandledException, TencentCloudSDKException {
        return msgService.verCodeSendRegister(info);
    }

    /**
     * 发送验证码
     */
    @PostMapping("/msg/ext/ver/reset")
    public ResultBean<?> verCodeSendReset(@RequestParam("info") String info) throws UnhandledException, TencentCloudSDKException {
        return msgService.verCodeSendReset(info);
    }

    /**
     * 发送邀请码
     */
    @PostMapping("/msg/ext/inv")
    public ResultBean<?> inviteCodeSend(HttpServletRequest request, @RequestParam Map<String, String> params) throws UnhandledException {
        return null;
    }

}
