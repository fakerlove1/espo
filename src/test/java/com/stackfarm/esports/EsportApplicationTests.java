package com.stackfarm.esports;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stackfarm.esports.dao.activity.ActivityDao;
import com.stackfarm.esports.dao.authentication.AnnualInfoDao;
import com.stackfarm.esports.dao.authentication.ApplicationDao;
import com.stackfarm.esports.dao.authentication.MemberAuthenticationDao;
import com.stackfarm.esports.dao.qrcode.QRCodeDao;
import com.stackfarm.esports.dao.user.*;
import com.stackfarm.esports.pojo.authorize.Application;
import com.stackfarm.esports.pojo.authorize.MemberAuthentication;
import com.stackfarm.esports.pojo.qrcode.QRCode;
import com.stackfarm.esports.pojo.user.SystemUser;
import com.stackfarm.esports.pojo.user.UserActivitiesInfo;
import com.stackfarm.esports.pojo.user.UserActivityComplete;
import com.stackfarm.esports.pojo.user.UserExtensionOrganization;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.FileUtils;
import com.stackfarm.esports.utils.QRCodeUtils;
import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
class EsportApplicationTests {

    @Autowired
    SystemUserDao systemUserDao;
    @Autowired
    UserExtensionPersonDao userExtensionPersonDao;
    @Autowired
    UserExtensionOrganizationDao userExtensionOrganizationDao;
    @Autowired
    UserActivitiesInfoDao userActivitiesInfoDao;
    @Autowired
    UserInformationDao userInformationDao;
    @Autowired
    UserActivityDao userActivityDao;
    @Autowired
    UserActivityCompleteDao userActivityCompleteDao;
    @Autowired
    ActivityDao activityDao;
    @Autowired
    QRCodeDao qrCodeDao;
    @Autowired
    AnnualInfoDao annualInfoDao;
    @Autowired
    ApplicationDao applicationDao;
    @Autowired
    MemberAuthenticationDao memberAuthenticationDao;
    @Test
    void contextLoads() throws IOException {
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("qwe");
        list.add(null);
        list.add("789");
        System.out.println(list);
    }

    @Test
    void pathTest() {

        SystemUser user = systemUserDao.selectById(28l);
        String path = "D:/my_QRCode/";
        String content = user.toString();
        QRCodeUtils.generateQRCodeImg(path, content, "123");
        System.out.println(BaseUtils.getFileListFromString("13_24_"));
    }

    @Test
    void timesTest() {
        UserActivitiesInfo userActivitiesInfo = userActivitiesInfoDao.selectByUserId(60l);
        userActivitiesInfo.setTimes(userActivitiesInfo.getTimes() + 1);
        System.out.println(userActivitiesInfo);
    }

    @Test
    void daoTest() {
        System.out.println(userExtensionPersonDao.selectByUserId(1l));
        System.out.println(userExtensionOrganizationDao.selectByUserId(1l));
        System.out.println(systemUserDao.selectAll());
        System.out.println(userInformationDao.selectByUserId(1l));
    }

    @Test
    void dateTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        System.out.println(calendar.get(Calendar.YEAR));
    }

    @Test
    void selectDaoTest() {
        MemberAuthentication memberAuthentication = memberAuthenticationDao.selectById(39l);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());


        System.out.println(memberAuthentication.getNumber());
    }

}
