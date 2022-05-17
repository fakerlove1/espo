package com.stackfarm.esports.controller.base;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author croton
 * @create 2021/4/2 17:49
 */
@RestController
public class BaseController {

    @Autowired
    private BaseService baseService;
    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    @GetMapping("/base/username/{username}")
    public ResultBean<?> checkUsername(@PathVariable("username") String username) throws UnhandledException {
        return baseService.checkUsername(username);
    }
}
