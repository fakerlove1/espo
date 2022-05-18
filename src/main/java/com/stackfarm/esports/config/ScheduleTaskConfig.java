package com.stackfarm.esports.config;

import com.stackfarm.esports.dao.authentication.AnnualInfoDao;
import com.stackfarm.esports.dao.authentication.MemberAuthenticationDao;
import com.stackfarm.esports.pojo.authorize.AnnualInfo;
import com.stackfarm.esports.pojo.authorize.MemberAuthentication;
import com.stackfarm.esports.system.AuthenticationStateConstant;
import com.stackfarm.esports.a.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author croton
 * @create 2021/9/5 16:06
 */
@Configuration
@Lazy(false)
@EnableScheduling
@Slf4j
public class ScheduleTaskConfig {

    private MemberAuthenticationDao memberAuthenticationDao = SpringUtils.getBean("memberAuthenticationDao");
    private AnnualInfoDao annualInfoDao = SpringUtils.getBean("annualInfoDao");

    /**
     * 每到12.1更新一次，开始年审
     */
    @Scheduled(cron = "0 55 12 31 12 *")
    public void startAnnual() {
        log.info("开始年审");
        //所有运动员的状态变成年审中
        List<MemberAuthentication> all = memberAuthenticationDao.selectByStateAndType(AuthenticationStateConstant.AUTHORIZED, "PLAYER");
        List<MemberAuthentication> all1 = memberAuthenticationDao.selectByStateAndType(AuthenticationStateConstant.LOGOUTING, "PLAYER");
        all.addAll(all1);
        for (MemberAuthentication memberAuthentication : all) {
            memberAuthentication.setState(AuthenticationStateConstant.ANNUAL_AUTHORIZING);
            memberAuthenticationDao.update(memberAuthentication);
        }
        //所有涉及认证的俱乐部新增annual_info
        List<String> clubList = memberAuthenticationDao.selectAllClubContainsPlayer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        if (clubList == null) {
            clubList = new ArrayList<>();
        }
        for (String club : clubList) {
            AnnualInfo annualInfo = new AnnualInfo();
            annualInfo.setClub(club);
            annualInfo.setState(-1);//-1为未审核，0为未通过，1为已通过
            annualInfo.setCreateTime(System.currentTimeMillis());
            annualInfo.setYearNumber(year);
            annualInfoDao.insert(annualInfo);
        }
    }

    /**
     * 每到1.1更新一次，年审结束
     */
    @Scheduled(cron = "59 59 23 31 1 *")
//    @Scheduled(cron = "0 0 0 1 1 *")
    public void endAnnual() {
        log.info("年审结束");
        //年审未通过
        List<MemberAuthentication> all1 = memberAuthenticationDao.selectByStateAndType(AuthenticationStateConstant.ANNUAL_UNAUTHORIZED, "PLAYER");
        for (MemberAuthentication memberAuthentication : all1) {
            memberAuthentication.setState(AuthenticationStateConstant.LOGOUT);
            memberAuthenticationDao.update(memberAuthentication);
        }
        //年审中
        List<MemberAuthentication> all2 = memberAuthenticationDao.selectByStateAndType(AuthenticationStateConstant.ANNUAL_AUTHORIZING, "PLAYER");
        for (MemberAuthentication memberAuthentication : all2) {
            memberAuthentication.setState(AuthenticationStateConstant.LOGOUT);
            memberAuthenticationDao.update(memberAuthentication);
        }
    }

    /**
     * 每5分钟更新一次，检查是否过期
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void checkEffective() {
        System.out.println("检查是否过期");
        List<MemberAuthentication> memberAuthentications = memberAuthenticationDao.selectExpired(System.currentTimeMillis());
        for (MemberAuthentication memberAuthentication : memberAuthentications) {
            memberAuthentication.setState(AuthenticationStateConstant.LOGOUT);
        }
    }



}
