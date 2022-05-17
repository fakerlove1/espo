package com.stackfarm.esports.dao.user;

import com.stackfarm.esports.pojo.user.UserActivityComplete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author croton
 * @create 2021/4/7 10:55
 */
@Repository
@Mapper
public interface UserActivityCompleteDao {

    List<UserActivityComplete> selectByUserId(Long userId);
    List<UserActivityComplete> selectByActId(Long actId);
    UserActivityComplete selectByUserIdAndActId(Long userId, Long actId);
    void deleteByUserId(Long userId);
    void insert(UserActivityComplete userActivityComplete);
    void update(UserActivityComplete userActivityComplete);

}
