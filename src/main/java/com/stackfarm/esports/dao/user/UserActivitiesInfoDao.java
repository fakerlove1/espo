package com.stackfarm.esports.dao.user;

import com.stackfarm.esports.pojo.user.UserActivitiesInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author croton
 * @create 2021/4/2 14:55
 */
@Repository
@Mapper
public interface UserActivitiesInfoDao {

    void insert(UserActivitiesInfo userActivitiesInfo);

    void deleteByUserId(Long userId);

    void update(UserActivitiesInfo userActivitiesInfo);

    UserActivitiesInfo selectByUserId(Long userId);

    List<UserActivitiesInfo> selectAll();

}
