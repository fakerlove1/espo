package com.stackfarm.esports.system;

import com.stackfarm.esports.utils.PropertiesReadUtils;

/**
 * @author croton
 * @create 2021/3/31 21:04
 */
public class SystemConstant {
    public static final String AUTHENTICATE_TOKEN_NAME = "token";

    // TODO
    public static final Long AUTHENTICATE_TOKEN_EXPIRED_TIME = PropertiesReadUtils.TOKEN_EXPIRE_TIME;

    public static final Integer INV_CODE_REM_NUM = PropertiesReadUtils.INV_CODE_REM_NUM;

    public static final String USERNAME = "username";

    public static final Long SQL_CACHE_EXPIRE_TIME = 3 * 24 * 60 * 60 * 1000L;

    public static final Long DATA_CACHE_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

    public static final String FILE_ROOT_PATH = PropertiesReadUtils.FILE_ROOT_PATH;

    public static final String ZIP_FILE_TEMPORARY_PATH = PropertiesReadUtils.FILE_ROOT_PATH;

    public static final String DEFAULT_ALL = "ALL";

    public static final String ONLY_MY_ATY = "ONLY_MY_ATY";
}
