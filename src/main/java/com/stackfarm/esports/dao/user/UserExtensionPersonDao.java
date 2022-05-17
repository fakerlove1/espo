package com.stackfarm.esports.dao.user;

import com.stackfarm.esports.pojo.user.UserExtensionPerson;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author croton
 * @create 2021/4/3 14:02
 */
@Mapper
@Repository
public interface UserExtensionPersonDao {
    UserExtensionPerson selectByUserId(Long userId);
    UserExtensionPerson selectByEmail(String email);
    UserExtensionPerson selectByPhoneNumber(Long phoneNumber);
    void deleteByUserId(Long userId);
    void insert(UserExtensionPerson userExtensionPerson);
    void update(UserExtensionPerson userExtensionPerson);
}
