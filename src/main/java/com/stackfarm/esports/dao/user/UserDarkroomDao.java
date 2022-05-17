package com.stackfarm.esports.dao.user;

import com.stackfarm.esports.pojo.user.UserDarkroom;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @Author xiaohuang
 * @create 2021/4/7 20:23
 */
@Mapper
@Repository
public interface UserDarkroomDao {

    int insert(UserDarkroom userDarkroom);

    int deleteByUserId(Long userId0);

    int update(UserDarkroom userDarkroom);

    UserDarkroom selectByUserId(Long userId);


}
