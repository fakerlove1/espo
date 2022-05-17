package com.stackfarm.esports.security;

import org.apache.shiro.authc.AuthenticationToken;


/**
 * @author croton
 * @create 2021/3/31 20:50
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    private static final long serialVersionUID = 1L;

    /**
     * 注意，principal和credential都是同一个
     */

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public JwtToken(String token) {
        this.token = token;
    }

    public JwtToken() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
