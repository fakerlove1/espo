package com.stackfarm.esports.dao.authentication;

import com.stackfarm.esports.pojo.authorize.AnnualInfo;
import com.stackfarm.esports.pojo.authorize.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author croton
 * @create 2021/10/21 13:18
 */
@Mapper
@Repository
public interface AnnualInfoDao {
    int insert(AnnualInfo annualInfo);

    int deleteById(Integer id);

    int update(AnnualInfo annualInfo);

    List<AnnualInfo> selectByClub(String club);

    AnnualInfo selectByClubAndYear(@Param("club") String club, @Param("yearNumber") Integer yearNumber);
}
