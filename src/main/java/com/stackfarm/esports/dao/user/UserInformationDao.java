package com.stackfarm.esports.dao.user;

import com.stackfarm.esports.pojo.user.UserExtensionPerson;
import com.stackfarm.esports.pojo.user.UserInformation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author croton
 * @create 2021/4/3 15:32
 */
@Repository
@Mapper
public interface UserInformationDao {
    UserInformation selectByUserId(Long userId);
    UserInformation selectByCardId(String cardId);
    void deleteByUserId(Long userId);
    void insert(UserInformation userInformation);
    void update(UserInformation userInformation);
    UserInformation selectByName(String name);
}
