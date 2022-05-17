package com.stackfarm.esports.dao.user;

import com.stackfarm.esports.pojo.user.SystemRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author xiaohuang
 * @create 2021/3/31 20:58
 */
@Mapper
@Component
public interface SystemRoleDao {

    int insert(SystemRole role);

    int deleteById(Long id);

    int update(SystemRole role);

    SystemRole selectById(Long id);

    SystemRole selectByName(String name);

}
