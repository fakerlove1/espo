package com.stackfarm.esports.service.user;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.system.UserRolesConstant;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author croton
 * @create 2021/4/5 12:37
 */
public interface UserInfoService {

    /**
     * 修改个人信息
     * @param userId
     * @param realName
     * @param cardId
     * @param preferPosition
     * @param experience
     * @param email
     * @param qq
     * @param introduction
     * @param profile
     * @return
     */
    ResultBean<?> updateUserInformation(Long userId, String realName, String cardId, Integer age,
                                        String sex, String nation, Long birthday, String home,
                                        String preferPosition, String experience, String email,
                                        String qq, String introduction, String education, MultipartFile profile) throws UnhandledException;

    /**
     * 查看个人信息
     * @return
     */
    ResultBean<?> getUserInformation(Long userId) throws UnhandledException;

    /**
     * 查看用户角色
     * @return
     */
    ResultBean<?> getUserRole(String token) throws UnhandledException;

    /**
     * 根据角色查看用户信息
     * @return
     */
    ResultBean<?> getUserByRole(String roleName, Integer page, Integer number, String name) throws UnhandledException;
}
