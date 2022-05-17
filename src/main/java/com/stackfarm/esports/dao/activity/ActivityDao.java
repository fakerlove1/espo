package com.stackfarm.esports.dao.activity;

import com.stackfarm.esports.pojo.activity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author xiaohuang
 * @create 2021/4/1 15:27
 */
@Mapper
@Component
public interface ActivityDao {

    int insert(Activity activity);

    int deleteById(Long id);

    int update(Activity activity);

    Activity selectById(Long id);

    Activity selectByActName(String name);

    Activity selectByActNameAndUserIdAndState(String name, Long launcherId, String state);

    List<Long> selectByScope(String scope, String name);


    List<Activity> selectListByLauncherId(Long launcherId);

    List<Activity> selectByState(String state);

    Integer selectCount();

    List<Activity> selectSuccessByUserId(Long userId);

    Activity selectByActId(Long actId);

    List<Activity> selectAll(Integer page, Integer number,String name);


}
