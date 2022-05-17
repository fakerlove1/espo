package com.stackfarm.esports.dao.user;

import com.stackfarm.esports.pojo.user.UserExtensionOrganization;
import com.stackfarm.esports.pojo.user.UserExtensionPerson;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author croton
 * @create 2021/4/3 14:02
 */
@Mapper
@Repository
public interface UserExtensionOrganizationDao {
    UserExtensionOrganization selectByUserId(Long userId);
    UserExtensionOrganization selectByEmail(String email);
    UserExtensionOrganization selectByTel(String tel);
    void deleteByUserId(Long userId);
    void insert(UserExtensionOrganization userExtensionPerson);
    void update(UserExtensionOrganization userExtensionPerson);
}
