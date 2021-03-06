package com.stackfarm.esports.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackfarm.esports.a.SpringUtils;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.system.SMSSenderConstant;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author croton
 * @create 2021/3/31 19:34
 */
public class BaseUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseUtils.class);

    private static final Date DATE = new Date();

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    private static boolean isOnTime;

    private static long timeGap = 0L;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Random random = new Random();

    private static final JavaMailSender javaMailSender = SpringUtils.getBean("mailSender");

//    static {
//        try {
//            String url = "http://www.ntsc.ac.cn";
//            URL temp = new URL(url);
//            URLConnection urlConnection = temp.openConnection();
//            urlConnection.connect();
//            timeGap = urlConnection.getDate() - System.currentTimeMillis();
//            isOnTime = Math.abs(timeGap) <= 1000;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * ????????????????????????2020?????????????????????????????????
     */
    private static final String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";

    /**
     * ?????????????????????
     */
    private static final String CHECK = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * ??????????????????
     * @return ????????????
     */
    public static String getRunLocation(StackTraceElement element) {
        return element.getClassName()+"."+element.getMethodName()+"."+element.getLineNumber();
    }

    /**
     * ??????UUID??????token
     * @return uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * ????????????
     */
    public static String getSalt() {

        int val = new Random().nextInt() % 999999;
        val = Math.abs(val);
        String salt = String.format("%06d", val);
        salt += UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        return salt;
    }

    /**
     * List -> String[]
     */
    public static String restoreList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        list.stream().filter(p -> !"".equals(p)).forEach(p -> {sb.append(p).append(";");});
        return sb.toString();
    }

    /**
     * List -> String[]
     */
    public static String filerestoreList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        list.stream().filter(p -> !"".equals(p)).forEach(p -> {sb.append(p).append("-");});
        return sb.toString();
    }

    /**
     * String[] -> List
     */
    public static List<String> getListFromString(String str) {
        String[] strings = str.trim().split(";");
        return Arrays.stream(strings).filter(p -> !"".equals(p)).collect(Collectors.toList());
    }

    /**
     * String[] -> List
     */
    public static List<String> getFileListFromString(String str) {
        String[] strings = str.trim().split("_");
        return Arrays.stream(strings).filter(p -> !"".equals(p)).collect(Collectors.toList());
    }

    /**
     * ???????????????
     *
     * @param phone ?????????
     * @return boolean true:??????false:???
     */
    public static boolean isMobile(String phone) {
        if (ObjectUtils.isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, phone);
    }

    /**
     * ?????????????????????
     * @param email ??????
     * @return ???????
     */
    public static boolean isEmail(String email) {
        Pattern regex = Pattern.compile(CHECK);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    /**
     * ??????????????????
     * @return ????????????
     */
    public static String newEmail(String identityType, String identifier) {
        return "SYS_GEN_" +
                UUID.randomUUID().toString().replace("-", "M").toUpperCase().trim() + "_" +
                identityType.toUpperCase() + "_" +
                identifier.toUpperCase() + "_" +
                "@esports.com";
    }

    /**
     * ??????????????????
     * @return ?????????1?????????UTC+8??????
     */
    public static Long UTCUp8Time() {
        long time;
        if (!isOnTime) {
            time = System.currentTimeMillis() + timeGap;
        } else {
            time = System.currentTimeMillis();
        }
        // TODO ????????????????????????
        return System.currentTimeMillis();
    }

    /**
     * ?????????????????? yyyy_MM_dd_hh_mm?????????
     */
    public static Long timeParser(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
        long t = 0;
        try {
            t = sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * ????????????
     */
    private static <T> T dealNullStr(T str) {
        if ("".equals(str)) {
            return null;
        } else {
            return str;
        }
    }

    /**
     * ????????????????????????
     */
    public static <T> T getValueWithThrow(T value, String errLocation) throws UnhandledException {
        value = dealNullStr(value);
        UnhandledException unhandledException = new UnhandledException(HttpStatus.BAD_REQUEST.value(), "??????????????????", errLocation, new Date());
        return Optional.ofNullable(value)
                .orElseThrow(() -> unhandledException);
    }

    public static <T> T getValueWithThrow(Map<String, T> params, String valName, String errLocation) throws UnhandledException {
        T value = params.get(valName);
        params.put(valName, getValueWithThrow(value, errLocation));
        return params.get(valName);
    }

    /**
     * ????????????????????????????????????
     */
    public static <T> T getValueWithLog(T value, String valName) {
        value = dealNullStr(value);
        return Optional.ofNullable(value)
                .orElse(log(valName));
    }

    public static <T> T getValueWithLog(Map<String, T> params, String valName) {
        T value = params.get(valName);
        params.put(valName, getValueWithLog(value, valName));
        return params.get(valName);
    }

    private static <T> T log(String valName) {
        long currentTime = System.currentTimeMillis();
        DATE.setTime(currentTime);
        String time = FORMAT.format(DATE);
        LOGGER.warn(time + " ???????????????" + valName + "??????");
        return null;
    }

//    public static void setDefaultValues(Object o) throws UnhandledException {
//        Field[] fields = o.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            if (Modifier.isFinal(field.getModifiers())) {
//                continue;
//            }
//            if (Modifier.isStatic(field.getModifiers())) {
//                continue;
//            }
//            boolean isAccess = field.canAccess(o);
//            if (!isAccess) {
//                field.setAccessible(true);
//            }
//            try {
//                if (field.get(o) != null) {
//                    continue;
//                }
//            } catch (IllegalAccessException e) {
//                ;
//            }
//            String fieldName = field.getName();
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(fieldName.charAt(0));
//            for (int i = 1, size = fieldName.length(); i < size; ++ i) {
//                char a = fieldName.charAt(i - 1);
//                char b = fieldName.charAt(i);
//                if (b >= 'A' && b <= 'Z') {
//                    stringBuilder.append("-")
//                            .append((char) (b - 'A' + 'a'));
//                } else {
//                    stringBuilder.append(b);
//                }
//            }
//            String newFieldName = stringBuilder.toString();
//            Class<?> clazz = field.getType();
//            try {
//                if (Long.class.getName().equals(clazz.getName())) {
//                    field.set(o, 0L);
//                } else if (String.class.getName().equals(clazz.getName())) {
//                    field.set(o, "no-" + newFieldName);
//                } else if (Double.class.getName().equals(clazz.getName())) {
//                    field.set(o, 0.0);
//                } else if (Integer.class.getName().equals(clazz.getName())) {
//                    field.set(o, 0);
//                } else if (Boolean.class.getName().equals(clazz.getName())) {
//                    field.set(o, false);
//                } else if (Short.class.getName().equals(clazz.getName())) {
//                    field.set(o, 0);
//                } else if (Float.class.getName().equals(clazz.getName())) {
//                    field.set(o, 0.0);
//                } else if (Date.class.getName().equals(clazz.getName())){
//                    field.set(o, new Date());
//                } else if (Gender.class.getName().equals(clazz.getName())) {
//                    field.set(o, Gender.X);
//                }
//            } catch (IllegalAccessException e) {
//                throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                        "???????????????" + fieldName + "??????????????????",
//                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
//                        new Date());
//            }
//            if (!isAccess) {
//                field.setAccessible(false);
//            }
//        }
//    }

    /**
     * ??????????????????????????????
     */
//    public static void updateObject(Object o, Map<String, String> params) throws UnhandledException {
//        Field[] fields = o.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            if (Modifier.isFinal(field.getModifiers())) {
//                continue;
//            }
//            if (Modifier.isStatic(field.getModifiers())) {
//                continue;
//            }
//            boolean isAccess = field.canAccess(o);
//            if (!isAccess) {
//                field.setAccessible(true);
//            }
//            String valName = field.getName();
//            String value = params.get(valName);
//            if (value != null && !"".equals(value)) {
//                Class<?> clazz = field.getType();
//                try {
//                    if (Long.class.getName().equals(clazz.getName())) {
//                        field.set(o, Long.parseLong(value));
//                    } else if (String.class.getName().equals(clazz.getName())) {
//                        field.set(o, value);
//                    } else if (Double.class.getName().equals(clazz.getName())) {
//                        field.set(o, Double.parseDouble(valName));
//                    } else if (Integer.class.getName().equals(clazz.getName())) {
//                        field.set(o, Integer.parseInt(value));
//                    } else if (Boolean.class.getName().equals(clazz.getName())) {
//                        field.set(o, Boolean.parseBoolean(value));
//                    } else if (Short.class.getName().equals(clazz.getName())) {
//                        field.set(o, Short.parseShort(value));
//                    } else if (Float.class.getName().equals(clazz.getName())) {
//                        field.set(o, Float.parseFloat(value));
//                    } else if (Date.class.getName().equals(clazz.getName())) {
//                        field.set(o, new Date(Long.parseLong(value)));
//                    } else {
//                        field.set(o, objectMapper.readValue(value, clazz));
//                    }
//                } catch (IllegalAccessException | JsonProcessingException e) {
//                    throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                            "???????????????" + valName + "??????",
//                            BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
//                            new Date());
//                }
//            }
//            if (!isAccess) {
//                field.setAccessible(false);
//            }
//        }
//    }

    /**
     * ?????????????????????????????????
     */
    public static HashMap<String, Object> allFieldsMap(Object o) throws UnhandledException {
        return allFieldsMap(o, null);
    }

    /**
     * ?????????????????????????????????
     * @param excludeFields ??????????????????
     */
    public static HashMap<String, Object> allFieldsMap(Object o, Set<String> excludeFields) throws UnhandledException {
        Field[] fields = o.getClass().getDeclaredFields();
        HashMap<String, Object> hashMap = new HashMap<>();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (excludeFields != null && excludeFields.contains(field.getName())) {
                continue;
            }
            boolean isAccess = field.canAccess(o);
            if (!isAccess) {
                field.setAccessible(true);
            }
            try {
                hashMap.put(field.getName(), field.get(o));
            } catch (IllegalAccessException e) {
                throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "???????????????" + field.getName() + "??????",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                        new Date());
            }
            if (!isAccess) {
                field.setAccessible(false);
            }
        }
        return hashMap;
    }

    /**
     * ???????????????
     * @return ?????????
     */
    public static String sendVerCode(String targetUsername, String template) throws TencentCloudSDKException {
        Random random = new Random();
        String verCode = (Math.abs(random.nextInt()) % 1000000) + "";
        if (isEmail(targetUsername)) {
            EmailUtils.sendVerCodeByEmail(targetUsername, verCode, javaMailSender);
        } else if (isMobile(targetUsername)) {
            if (SMSSenderConstant.SMS_TEMPLATE_REGISTER.equals(template)) {
                String[] params = {verCode, "10"};
                SMSSenderUtils.sendVerCodeBySms(targetUsername, params, template);
            } else if (SMSSenderConstant.SMS_TEMPLATE_RESET.equals(template)) {
                String[] params = {verCode};
                SMSSenderUtils.sendVerCodeBySms(targetUsername, params, template);
            }
        } else {
            return "target-id-error";
        }
        return verCode;
    }

    /**
     * ??????????????????
     * @return
     */
    public static void sendRefuseMail(String targetUsername, String cause) throws TencentCloudSDKException {
        EmailUtils.sendRefuseMsgByEmail(targetUsername, cause, javaMailSender);
    }

    /**
     * ????????????????????????
     * @return ?????????
     */
    public static void sendAccessMail(String targetUsername, String name) throws TencentCloudSDKException {
        EmailUtils.sendAccessMsgByEmail(targetUsername, name, javaMailSender);
    }

    /**
     * ??????????????????????????????
     * @return
     */
    public static void sendAccessMsg(String targetUsername, String name) throws TencentCloudSDKException {
        SMSSenderUtils.sendMsgBySms(targetUsername, name, SMSSenderConstant.SMS_TEMPLATE_ACCESS);
    }

    /**
     * ??????????????????????????????
     * @return
     */
    public static void sendSuccessMail(String targetUsername, String name) throws TencentCloudSDKException {
        EmailUtils.sendSuccessMsgByEmail(targetUsername, name, javaMailSender);
    }

    /**
     * ???????????????????????????????????????
     * @return
     */
    public static void sendAccessOMail(String targetUsername, String name) throws TencentCloudSDKException {
        EmailUtils.sendAccessOMsgByEmail(targetUsername, name, javaMailSender);
    }

    /**
     * ??????????????????????????????
     * @return
     */
    public static void sendCancelNotPassMsgByEmail(String targetUsername, String name, String cause) throws TencentCloudSDKException {
        EmailUtils.sendCancelNotPassMsgByEmail(targetUsername, name, cause, javaMailSender);
    }

    /**
     * ?????????????????????
     * @return
     */
    public static void sendDarkroomMsgByEmail(String targetUsername, String name, String time, String cause) throws TencentCloudSDKException {
        EmailUtils.sendDarkroomMsgByEmail(targetUsername, name, time, cause, javaMailSender);
    }

    /**
     * ??????????????????
     * @return
     */
    public static void sendAuthenticationMsgByEmail(String targetEmail, Boolean result, String name, String cause) throws TencentCloudSDKException {
        EmailUtils.sendAuthenticationMsgByEmail(targetEmail, result, name, cause, javaMailSender);
    }

    /**
     * ????????????????????????
     * @return
     */
    public static void sendUpdateMsgByEmail(String targetEmail, Boolean result, String name, String cause) throws TencentCloudSDKException {
        EmailUtils.sendUpdateMsgByEmail(targetEmail, result, name, cause, javaMailSender);
    }
    /**
     * ??????????????????
     * @return
     */
    public static void sendAnnualAuthenticationMsgByEmail(String targetEmail, Boolean result, String cause) throws TencentCloudSDKException {
        EmailUtils.sendAnnualAuthenticationMsgByEmail(targetEmail, result, cause, javaMailSender);
    }

    /**
     * ??????????????????
     * @return
     */
    public static void sendPostMsgByEmail(String targetEmail, Boolean result, String name, String cause, String trackingNumber) throws TencentCloudSDKException {
        EmailUtils.sendPostMsgByEmail(targetEmail, result, name, cause, trackingNumber, javaMailSender);
    }


    public static String toUpperCase(String str) {
        return str.replaceAll("-", "_").toUpperCase();
    }

    public static String parseTime(Long beginTime) {
        long currentTimeMillis = System.currentTimeMillis();
        //????????????
        Long mills = (beginTime - currentTimeMillis)/1000;

        if(mills <= 7*24*60*60) {
            return "?????????";
        } else if(mills > 7*24*60*60 && mills <= 30*24*60*60) {
            return "?????????";
        } else if(mills > 30*24*60*60 && mills <= 90*24*60*60) {
            return "?????????";
        } else if(mills > 90*24*60*60 && mills <= 365*24*60*60) {
            return "?????????";
        } else {
            return "????????????";
        }
    }

    /**
     * ???????????????????????????
     * @param time
     * @return
     */
    public static Boolean isDarkRoom(Long time, Long createTime) {
        Long currentTime = System.currentTimeMillis();
        Long darkTime = createTime + time;
        if(darkTime <= currentTime) {
            //??????????????????????????????????????????
            return false;
        } else {
            return true;
        }
    }

    /**
     * ????????????????????????
     * @param time
     * @return
     */
    public static String calculateDarkRoom(Long time, Long createTime) {
        Long currentTime = System.currentTimeMillis();
        Long darkTime = createTime + time;
        Long removeDarkTime = darkTime - currentTime;

        if(time < 0) {
            return "-1";
        }

        Long theTime = Long.valueOf(removeDarkTime/1000);// ????????????????????????
        int theTime1 = 0;// ???
        int theTime2 = 0;// ??????
        int theTime3 = 0;// ???
        if(theTime > 60) {
            theTime1 = (int) (theTime/60);
            theTime = (theTime%60);
            if(theTime1 > 60) {
                theTime2 = (int)(theTime1/60);
                theTime1 = (int)(theTime1%60);
                if(theTime2 > 24){
                    //??????24??????
                    theTime3 = (int)(theTime2/24);
                    theTime2 = (int)(theTime2%24);
                }
            }
        }
        String result = "";
        if(theTime > 0){
            result = ""+ theTime +"???";
        }
        if(theTime1 > 0) {
            result = ""+ theTime1 +"???"+result;
        }
        if(theTime2 > 0) {
            result = ""+ theTime2 +"??????"+result;
        }
        if(theTime3 > 0) {
            result = ""+ theTime3 +"???"+result;
        }
        return result;
    }


}
