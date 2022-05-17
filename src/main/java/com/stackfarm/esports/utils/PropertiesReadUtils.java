package com.stackfarm.esports.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author croton
 * @create 2021/3/31 21:04
 */
@Component
public class PropertiesReadUtils implements InitializingBean {
    public static Long TOKEN_EXPIRE_TIME;

    public static Integer INV_CODE_REM_NUM;

    public static String FILE_ROOT_PATH;

    public static String ZIP_FILE_TEMPORARY_PATH;

    public static String QRCODE_PATH;

    public static String SECRET_ID;

    public static String SECRET_KEY;

    public static String APP_ID;

    public static String SENDER;

    public static String SMS_TEMPLATE_REGISTER;

    public static String SMS_TEMPLATE_RESET;

    public static String SMS_TEMPLATE_ACCESS;


    public static String EMAIL_USERNAME;

    public static Integer AUTHENTICATION_EFFECTIVE_TIME;




    @Value("${tokenExpireTime}")
    private String tokenExpireTime;

    @Value("${fileRootPath}")
    private String fileRootPath;

    @Value("${invCodeRemNum}")
    private Integer invCodeRemNum;

    @Value("${zipFileTemporaryPath}")
    private String zipFileTemporaryPath;

    @Value("${qrCodePath}")
    private String qrCodePath;

    @Value("${secretId}")
    public String secretId;
    @Value("${secretKey}")
    public String secretKey;
    @Value("${appId}")
    public String appId;
    @Value("${sender}")
    public String sender;
    @Value("${smsTemplateRegister}")
    public String smsTemplateRegister;
    @Value("${smsTemplateReset}")
    public String smsTemplateReset;
    @Value("${smsTemplateAccess}")
    public String smsTemplateAccess;
    @Value("${emailUsername}")
    public String emailUsername;
    @Value("${authenticationEffectiveTime}")
    public Integer authenticationEffectiveTime;

    @Override
    public void afterPropertiesSet() {

        TOKEN_EXPIRE_TIME = Long.parseLong(tokenExpireTime) * 60 * 60 * 1000;

        INV_CODE_REM_NUM = invCodeRemNum;

        FILE_ROOT_PATH = fileRootPath;

        ZIP_FILE_TEMPORARY_PATH = zipFileTemporaryPath;

        QRCODE_PATH = qrCodePath;

        SECRET_ID = secretId;

        SECRET_KEY = secretKey;

        APP_ID =appId;

        SENDER = sender;

        SMS_TEMPLATE_REGISTER =smsTemplateRegister;

        SMS_TEMPLATE_RESET = smsTemplateReset;

        SMS_TEMPLATE_ACCESS =smsTemplateAccess;


        EMAIL_USERNAME = emailUsername;

        AUTHENTICATION_EFFECTIVE_TIME = authenticationEffectiveTime;
    }
}
