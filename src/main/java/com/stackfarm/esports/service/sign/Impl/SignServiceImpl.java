package com.stackfarm.esports.service.sign.Impl;

import com.stackfarm.esports.dao.activity.ActivityDao;
import com.stackfarm.esports.dao.activity.ActivityExtensionDao;
import com.stackfarm.esports.dao.sign.UserSignDao;
import com.stackfarm.esports.dao.user.SystemUserDao;
import com.stackfarm.esports.dao.user.UserActivitiesInfoDao;
import com.stackfarm.esports.dao.user.UserActivityCompleteDao;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.activity.Activity;
import com.stackfarm.esports.pojo.activity.ActivityExtension;
import com.stackfarm.esports.pojo.sign.UserSign;
import com.stackfarm.esports.pojo.user.SystemUser;
import com.stackfarm.esports.pojo.user.UserActivitiesInfo;
import com.stackfarm.esports.pojo.user.UserActivityComplete;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.sign.SignService;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author croton
 * @create 2021/6/14 13:39
 */
@Service
@Transactional
public class SignServiceImpl implements SignService {

    @Autowired
    private SystemUserDao systemUserDao;
    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private UserSignDao userSignDao;

    /**
     * 签到
     * @param uuid
     * @return
     */
    @Override
    public ResultBean<Void> sign(String uuid, String token) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();
        String username = JwtUtils.getUserName(token);
        SystemUser admin = systemUserDao.selectByUsername(username);
        String[] strings = uuid.split("_");
        List<Long> infos = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            infos.add(Long.parseLong(strings[i]));
        }
        Long userId = infos.get(0);
        Long actId = infos.get(1);
        Activity activity = activityDao.selectByActId(actId);
        if (activity.getLauncherId() != admin.getId()) {
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setMsg("您无权此操作！");
            result.setData(null);
            result.setTimestamp(System.currentTimeMillis());
        } else {
            if (activity.getParticipateIds() == null) {
                result.setStatus(HttpStatus.BAD_REQUEST.value());
                result.setMsg("该活动尚无参与人员！");
                result.setData(null);
                result.setTimestamp(System.currentTimeMillis());
            } else {
                List<String> participates = BaseUtils.getListFromString(activity.getParticipateIds());
                Boolean is_participate = false;
                for (String participate : participates) {
                    if (participate.equals(userId.toString())) {
                        is_participate = true;
                    }
                }

                if (is_participate == true) {
                    UserSign userSign = userSignDao.selectByUserIdAndActId(userId, actId);
                    if (userSign.getState() == true) {
                        result.setStatus(HttpStatus.BAD_REQUEST.value());
                        result.setMsg("此用戶已簽到！");
                        result.setData(null);
                        result.setTimestamp(System.currentTimeMillis());
                    } else {
                        userSign.setState(true);
                        userSignDao.update(userSign);
                    }
                } else {
                    result.setStatus(HttpStatus.BAD_REQUEST.value());
                    result.setMsg("此用戶非参与人员！");
                    result.setData(null);
                    result.setTimestamp(System.currentTimeMillis());
                }
            }

        }
        return result;
    }
}
