package com.stackfarm.esports.dao.user;

import com.stackfarm.esports.pojo.user.SystemUserRoles;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author xiaohuang
 * @create 2021/4/1 10:27
 */
@Mapper
@Component
public interface SystemUserRolesDao {

    int insert(SystemUserRoles record);

    int deleteById(Long id);

    int deleteByUserId(Long userId);

    int realDeleteByUserId(Long userId);

    int update(SystemUserRoles record);

    SystemUserRoles selectById(Long id);

    List<SystemUserRoles> selectListByUserId(Long userId);

    List<SystemUserRoles> selectListByRoleId(Long roleId);
}
