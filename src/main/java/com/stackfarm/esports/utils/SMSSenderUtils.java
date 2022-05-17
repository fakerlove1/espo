package com.stackfarm.esports.utils;

import com.stackfarm.esports.system.SMSSenderConstant;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;

/**
 * @author croton
 * @create 2021/4/5 16:44
 */
public class SMSSenderUtils {

    private static final String SECRET_ID = SMSSenderConstant.SECRET_ID;

    private static final String SECRET_KEY = SMSSenderConstant.SECRET_KEY;

    private static final String APP_ID = SMSSenderConstant.APP_ID;

    private static final String SENDER = SMSSenderConstant.SENDER;




    public static SendSmsResponse sendVerCodeBySms(String targetPhone, String[] params, String template) throws TencentCloudSDKException {
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(60);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        SmsClient client = new SmsClient(cred, "",clientProfile);
        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppid(APP_ID);
        req.setSign(SENDER);
        req.setSenderId(null);
        String session = "手机号: " + targetPhone;
        req.setSessionContext(session);
        req.setTemplateID(template);
        String[] phoneNumbers = {"+86" + targetPhone};
        req.setPhoneNumberSet(phoneNumbers);
        req.setTemplateParamSet(params);
        return client.SendSms(req);
    }


    public static SendSmsResponse sendMsgBySms(String targetPhone, String actName, String template) throws TencentCloudSDKException {
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(60);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        SmsClient client = new SmsClient(cred, "",clientProfile);
        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppid(APP_ID);
        req.setSign(SENDER);
        req.setSenderId(null);
        String session = "手机号: " + targetPhone;
        req.setSessionContext(session);
        req.setTemplateID(template);
        String[] phoneNumbers = {"+86" + targetPhone};
        req.setPhoneNumberSet(phoneNumbers);
        String[] params = {actName};
        req.setTemplateParamSet(params);
        return client.SendSms(req);
    }

}
