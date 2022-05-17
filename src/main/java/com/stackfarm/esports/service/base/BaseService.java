package com.stackfarm.esports.service.base;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author croton
 * @create 2021/4/4 17:26
 */
public interface BaseService {
    ResultBean<?> checkUsername(String username) throws UnhandledException;
}
