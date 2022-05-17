package com.stackfarm.esports.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.security.JwtToken;
import com.stackfarm.esports.system.SystemConstant;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author croton
 * @create 2021/3/31 20:47
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            return executeLogin(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 任何被拦截的请求，都需要进行身份验证；而这个验证的具体实现，就是通过调用此方法来完成的。
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader(SystemConstant.AUTHENTICATE_TOKEN_NAME);
        JwtToken jwtToken = new JwtToken(token);
        try {
            getSubject(request, response).login(jwtToken);
        } catch (AuthenticationException e) {
            ResultBean<Void> resultBean = new ResultBean<>();
            resultBean.setStatus(HttpStatus.BAD_REQUEST.value());
            resultBean.setMsg(e.getMessage());
            resultBean.setTimestamp(System.currentTimeMillis());
            resultBean.setData(null);
            ((HttpServletResponse) response).setHeader("Content-Type", "application/json");
            httpServletResponse.getOutputStream().write(objectMapper.writeValueAsString(resultBean).getBytes(StandardCharsets.UTF_8));
            return false;
        }
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
