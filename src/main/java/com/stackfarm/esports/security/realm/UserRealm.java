package com.stackfarm.esports.security.realm;

import com.stackfarm.esports.dao.user.SystemRoleDao;
import com.stackfarm.esports.dao.user.SystemUserDao;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.user.SystemRole;
import com.stackfarm.esports.pojo.user.SystemUser;
import com.stackfarm.esports.security.JwtToken;
import com.stackfarm.esports.system.SystemConstant;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author croton
 * @create 2021/3/31 20:52
 */
@Component
public class UserRealm extends AuthorizingRealm {
    @Resource
    private SystemUserDao systemUserDao;
    @Autowired
    private SystemRoleDao systemRoleDao;


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 实际的授权操作
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SystemUser currentUser = (SystemUser) principals.getPrimaryPrincipal();
        List<String> rolesList = BaseUtils.getListFromString(currentUser.getRole());
        Set<String> roleSet = new HashSet<>();
        for (String s : rolesList) {
            roleSet.add(systemRoleDao.selectById(Long.parseLong(s)).getName());
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roleSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 实际的认证操作
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        return new SimpleAuthenticationInfo(verifyToken(token), token, this.getName());
    }

    private SystemUser verifyToken(String token) {
        String username;
        try {
            username = JwtUtils.getUserName(token);
        } catch (UnhandledException e) {
            throw new ExpiredCredentialsException(e.getMsg());
        }
        SystemUser currentUser = systemUserDao.selectByUsername(username);
        if (currentUser == null) {
            throw new UnknownAccountException("未注册用户");
        }
        // 更新令牌时间
        redisTemplate.expire(token, SystemConstant.AUTHENTICATE_TOKEN_EXPIRED_TIME, TimeUnit.MILLISECONDS);
        redisTemplate.expire(username, SystemConstant.AUTHENTICATE_TOKEN_EXPIRED_TIME, TimeUnit.MILLISECONDS);
        return currentUser;
    }
}
