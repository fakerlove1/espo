package com.stackfarm.esports.system;

/**
 * @author croton
 * @create 2021/9/5 14:16
 */
public class AuthenticationStateConstant {

    /**
     * 认证未通过
     */
    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    /**
     * 已认证
     */
    public static final String AUTHORIZED = "AUTHORIZED";

    /**
     * 认证中
     */
    public static final String AUTHORIZING = "AUTHORIZING";

    /**
     * 年审中
     */
    public static final String ANNUAL_AUTHORIZING = "ANNUAL_AUTHORIZING";

    /**
     * 年审未通过
     */
    public static final String ANNUAL_UNAUTHORIZED = "ANNUAL_UNAUTHORIZED";

    /**
     * 注销中
     */
    public static final String LOGOUTING = "LOGOUTING";

    /**
     * 已注销
     */
    public static final String LOGOUT = "LOGOUT";

}
