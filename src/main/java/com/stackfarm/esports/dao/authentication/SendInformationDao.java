package com.stackfarm.esports.dao.authentication;

import com.stackfarm.esports.pojo.authorize.SendInformation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author croton
 * @create 2021/9/5 9:13
 */
@Mapper
@Repository
public interface SendInformationDao {

    void insert(SendInformation sendInformation);

    void deleteById(Long id);

    void update(SendInformation sendInformation);

    List<SendInformation> selectFirstByMemberId(Long memberId);

    List<SendInformation> selectRedoByMemberId(Long memberId);

    List<SendInformation> selectAll();

    List<SendInformation> selectByClub(String club);

    SendInformation selectById(Long id);
}
