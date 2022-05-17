package com.stackfarm.esports.dao.user;

import com.stackfarm.esports.pojo.user.UserActivity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author xiaohuang
 * @create 2021/4/1 19:02
 */
@Mapper
@Component
public interface UserActivityDao {

    int insert(UserActivity record);

    int deleteById(Long id);

    int deleteByUserId(Long userId);

    int update(UserActivity record);

    UserActivity selectById(Long id);

    UserActivity selectByUserIdAndActId(Long userId, Long actId);

    List<UserActivity> selectAll();

    List<UserActivity> selectByUserId(Long userId);

    List<UserActivity> selectByActId(Long actId);

    List<UserActivity> selectByActIdAndState(Long actId, String state);
}
