package com.stackfarm.esports.system;

import com.stackfarm.esports.utils.PropertiesReadUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author croton
 * @create 2021/4/5 16:59
 */
public class SMSSenderConstant {

    public static final String SECRET_ID = PropertiesReadUtils.SECRET_ID;

    public static final String SECRET_KEY = PropertiesReadUtils.SECRET_KEY;

    public static final String APP_ID = PropertiesReadUtils.APP_ID;

    public static final String SENDER = PropertiesReadUtils.SENDER;

    public static final String SMS_TEMPLATE_REGISTER = PropertiesReadUtils.SMS_TEMPLATE_REGISTER;

    public static final String SMS_TEMPLATE_RESET = PropertiesReadUtils.SMS_TEMPLATE_RESET;

    public static final String SMS_TEMPLATE_ACCESS = PropertiesReadUtils.SMS_TEMPLATE_ACCESS;






}
