package com.stackfarm.esports.dao.authentication;

import com.stackfarm.esports.pojo.authorize.MemberAuthentication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author croton
 * @create 2021/9/5 8:47
 */
@Mapper
@Repository
public interface MemberAuthenticationDao {
    void insert(MemberAuthentication member);

    void logoutById(Long id);

    void update(MemberAuthentication member);

    MemberAuthentication selectById(Long id);

    MemberAuthentication selectByClubAndIdNumber(@Param("club")String club, @Param("idcardNumber")String idcardNumber);

    List<MemberAuthentication> selectByNameAndClub(@Param("name") String name, @Param("club") String club);

    List<MemberAuthentication> selectByClub(String club);

    List<MemberAuthentication> selectByStateAndType(String state, String type);

    List<MemberAuthentication> selectByStatePlus(@Param("state1") String state1, @Param("state2") String state2);

    List<MemberAuthentication> selectExpired(Long standardDate);

    List<MemberAuthentication> selectUnexpired(Long standardDate);

    List<MemberAuthentication> selectAll();

    List<MemberAuthentication> selectAuthorizedByClub(String club);

    List<MemberAuthentication> selectAuthorizedByClubAndType(@Param("club") String club, @Param("type") String type);

    MemberAuthentication selectByNameAndSexAndBirth(@Param("name") String name, @Param("sex") String sex, @Param("birth") String birth);

    List<MemberAuthentication> selectByClubAndState(@Param("club") String club, @Param("state") String state);

    List<MemberAuthentication> selectByClubAndStateAndType(@Param("club") String club, @Param("state") String state, @Param("type") String type);

    List<MemberAuthentication> selectAuthorizedByTypeAndProjectAndLevelAndYear(@Param("type") String type, @Param("project") String project, @Param("level") Integer level, @Param("year") Integer year);

    List<String> selectAllClubContainsPlayer();

}
