package com.stackfarm.esports.controller.sign;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.sign.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author croton
 * @create 2021/6/14 13:38
 */
@RestController
public class SignController {

    @Autowired
    private SignService signService;

    /**
     * 签到
     * @param uuid
     * @return
     */
    @GetMapping("/sign/{uuid}")
    public ResultBean<?> sign(@PathVariable("uuid") String uuid, HttpServletRequest request) throws UnhandledException {
        String token = request.getHeader("token");
        return signService.sign(uuid, token);
    }
}
