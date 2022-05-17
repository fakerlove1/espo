package com.stackfarm.esports.service.sign;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;

import java.util.Map;

/**
 * @author croton
 * @create 2021/6/14 13:39
 */
public interface SignService {

    ResultBean<?> sign(String uuid, String token) throws UnhandledException;
}
