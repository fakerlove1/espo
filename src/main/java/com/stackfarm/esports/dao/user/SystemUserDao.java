package com.stackfarm.esports.dao.user;

import com.stackfarm.esports.pojo.user.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author xiaohuang
 * @create 2021/3/31 19:48
 */
@Mapper
@Repository
public interface SystemUserDao {

    int insert(SystemUser user);

    int deleteById(Long id);

    int realDeleteByUserId(Long userId);

    int update(SystemUser user);

    SystemUser selectById(Long id);

    SystemUser selectByUsername(String username);

    List<SystemUser> selectByState(Boolean state);

    List<SystemUser> selectByStateAndUsername(Boolean state, String username);

    List<SystemUser> selectAll();

    List<SystemUser> selectByRoleAndPageAndNumber(String roleId, Integer page, Integer number);

    List<SystemUser> selectByRoleAndUsernameAndPageAndNumber(String roleId, String username,Integer page, Integer number);

    Integer selectByRole(String role);

}
