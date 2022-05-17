package com.stackfarm.esports.service.activity.Impl;

import com.stackfarm.esports.dao.activity.ActivityDao;
import com.stackfarm.esports.pojo.activity.Activity;
import com.stackfarm.esports.service.activity.ActivityService;
import com.stackfarm.esports.system.ActivityStateConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author xiaohuang
 * @create 2021/4/4 15:01
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final ActivityDao activityDao;

    public ActivityServiceImpl(@Autowired ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    @Override
    public Activity updateActState(Activity activity) {

        //如果活动处于通过，报名中，报名结束，活动进行中才会刷新状态
        if(activity.getState().equals(ActivityStateConstant.ACCESS) || activity.getState().equals(ActivityStateConstant.ENROLLING)
        || activity.getState().equals(ActivityStateConstant.ENROLL_CLOSED) || activity.getState().equals(ActivityStateConstant.HOLDING)) {
            if(activity.getEnrollBeginTime() <= System.currentTimeMillis()
                    && activity.getEnrollEndTime() > System.currentTimeMillis()) {
                //////System.out.println("活动报名中");
                activity.setState(ActivityStateConstant.ENROLLING);
            } else if(activity.getEnrollEndTime() <= System.currentTimeMillis()
                    && activity.getBeginTime() > System.currentTimeMillis()) {
                //报名结束
                //System.out.println("报名已结束");
                activity.setState(ActivityStateConstant.ENROLL_CLOSED);
            } else if(activity.getBeginTime() <= System.currentTimeMillis()
                    && activity.getEndTime() > System.currentTimeMillis()) {
                //活动进行中
                //System.out.println("活动进行中");
                activity.setState(ActivityStateConstant.HOLDING);
            } else if(activity.getEndTime() <= System.currentTimeMillis()) {
                //活动结束
                //System.out.println("活动已经结束");
                activity.setState(ActivityStateConstant.ENDED);
            }
            activityDao.update(activity);
        }
        return activity;
    }
}
