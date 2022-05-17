package com.stackfarm.esports.service.activity;

import com.stackfarm.esports.pojo.activity.Activity;
import org.springframework.stereotype.Service;

/**
 * @Author xiaohuang
 * @create 2021/4/4 15:00
 */
@Service
public interface ActivityService {

    Activity updateActState(Activity activity);

}
