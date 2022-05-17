package com.stackfarm.esports.dao.authentication;

import com.stackfarm.esports.pojo.authorize.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author croton
 * @create 2021/9/5 8:46
 */
@Mapper
@Repository
public interface ApplicationDao {

    void insert(Application application);

    void deleteById(Long id);

    void update(Application application);

    Application selectById(Long id);

    Application selectByMemberIdAndType(@Param("memberId") Long memberId, @Param("applicationType") String applicationType);

    List<Application> selectByApplicationType(String applicationType);

    List<Application> selectByResult(Boolean result);

    List<Application> selectAll();
}
