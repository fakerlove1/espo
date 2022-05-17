package com.stackfarm.esports.dao.authentication;

import com.stackfarm.esports.pojo.authorize.Project;
import com.stackfarm.esports.pojo.qrcode.QRCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author croton
 * @create 2021/10/17 17:05
 */
@Repository
@Mapper
public interface ProjectDao {

    int insert(Project project);

    int deleteById(Long id);

    int update(Project project);

    Project selectById(Long id);

    Project selectByName(String name);
}
