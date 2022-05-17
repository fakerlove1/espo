package com.stackfarm.esports.aop;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.utils.BaseUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author croton
 * @create 2021/3/31 19:30
 */
@Aspect
@Component
public class WebAop {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebAop.class);

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${server.error.path}")
    private String path;

    @Pointcut("execution(* com.stackfarm.esports..*.*(..))")
    public void cut() {
    }

    @Around("cut()")
    public Object handler(ProceedingJoinPoint joinPoint) throws IOException {
        synchronized (this) {
            String time = FORMAT.format(System.currentTimeMillis());
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
                assert servletRequestAttributes != null;
                HttpServletRequest request = servletRequestAttributes.getRequest();
                HttpServletResponse response = servletRequestAttributes.getResponse();
                assert response != null;
                if (throwable instanceof UnhandledException) {
                    UnhandledException unhandledException = (UnhandledException) throwable;
                    LOGGER.error(time + " 捕获异常，在: " + unhandledException.getLocation() + ". 捕获: " + unhandledException.getMsg());
                    String uuid = BaseUtils.getUUID();
                    redisTemplate.opsForValue().set(uuid, unhandledException.getMsg());
                    // 404
                    if (unhandledException.getHttpStatus().equals(HttpStatus.NOT_FOUND.value())) {
                        response.setStatus(HttpStatus.NOT_FOUND.value());
                    }
                    // 500
                    else if (unhandledException.getHttpStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR.value())) {
                        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    }
                    return new ResultBean<Void>(unhandledException.getHttpStatus(), unhandledException.getMsg(), null, System.currentTimeMillis());

                } else {
                    LOGGER.error(time + " 捕获未知异常: " + throwable.toString());
                    throwable.printStackTrace();
                    // 500
                    String uuid = BaseUtils.getUUID();
                    redisTemplate.opsForValue().set(uuid, throwable.toString());
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResultBean<Void>(HttpStatus.INTERNAL_SERVER_ERROR.value(), throwable.getMessage(), null, System.currentTimeMillis());
                }
            }
        }
    }
}
