package com.stackfarm.esports.dao.activity;

import com.stackfarm.esports.pojo.activity.ActivityUnexpect;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author xiaohuang
 * @create 2021/4/1 20:20
 */
@Mapper
@Component
public interface ActivityUnexpectDao {

    int insert(ActivityUnexpect record);

    int deleteByActId(Long actId);

    int update(ActivityUnexpect record);

    ActivityUnexpect selectByActId(Long actId);
}
