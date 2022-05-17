package com.stackfarm.esports.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.stackfarm.esports.a.SpringUtils;
import com.stackfarm.esports.dao.user.SystemUserDao;
import com.stackfarm.esports.dao.user.UserExtensionOrganizationDao;
import com.stackfarm.esports.dao.user.UserExtensionPersonDao;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.HttpStatusExternal;
import com.stackfarm.esports.system.SystemConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author croton
 * @create 2021/3/31 20:55
 */
public class JwtUtils {

    private static UserExtensionPersonDao userExtensionPersonDao = SpringUtils.getBean("userExtensionPersonDao");
    private static UserExtensionOrganizationDao userExtensionOrganizationDao = SpringUtils.getBean("userExtensionOrganizationDao");
    private static SystemUserDao systemUserDao = SpringUtils.getBean("systemUserDao");

//    static {
//        u
//    }
    /**
     * 验证身份
     * @param token 令牌
     * @param username 用户名
     * @param encryptPwd 加密过的密码
     */
    public static boolean verify(String token, String username, String encryptPwd) {
        String uname = username;
        if (BaseUtils.isMobile(username)) {
            Long userId = userExtensionPersonDao.selectByPhoneNumber(Long.parseLong(username)).getUserId();
            uname = systemUserDao.selectById(userId).getUsername();
        } else if (BaseUtils.isEmail(username)) {
            Long userId = userExtensionOrganizationDao.selectByEmail(username).getUserId();
            uname = systemUserDao.selectById(userId).getUsername();
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(encryptPwd);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withClaim(SystemConstant.USERNAME, uname).build();
            jwtVerifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 注册Token操作
     * @param username 用户登录名
     * @param secretKey 密钥
     * @return JWT生成的Token
     */
    public static String sign(String username, String secretKey) {
        String uname = username;
        if (BaseUtils.isMobile(username)) {
            Long userId = userExtensionPersonDao.selectByPhoneNumber(Long.parseLong(username)).getUserId();
            uname = systemUserDao.selectById(userId).getUsername();
        } else if (BaseUtils.isEmail(username)) {
            Long userId = userExtensionOrganizationDao.selectByEmail(username).getUserId();
            uname = systemUserDao.selectById(userId).getUsername();
        }
        RedisTemplate<String, Object> redisTemplate = SpringUtils.getBean("redisTemplate");
        assert redisTemplate != null;
        String oldToken = redisTemplate.opsForValue().get(uname) + "";
        if (!"".equals(oldToken) && !"null".equals(oldToken)) {
            return oldToken;
        }
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Date date = new Date();
        date.setTime(System.currentTimeMillis() + SystemConstant.AUTHENTICATE_TOKEN_EXPIRED_TIME);
        String token = JWT.create().withClaim(SystemConstant.USERNAME, uname).withExpiresAt(date).sign(algorithm);
        redisTemplate.opsForValue().set(token, uname, SystemConstant.AUTHENTICATE_TOKEN_EXPIRED_TIME, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(uname, token, SystemConstant.AUTHENTICATE_TOKEN_EXPIRED_TIME, TimeUnit.MILLISECONDS);
        return token;
    }

    /**
     * 从Token获取UserName
     */
    public static String getUserName(String token) throws UnhandledException {
        RedisTemplate<String, Object> redisTemplate = SpringUtils.getBean("redisTemplate");
        assert redisTemplate != null;
        if (token == null) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(),
                    "令牌不可为空",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                    new Date());
        }
        String username1 = (String) redisTemplate.opsForValue().get(token);
        String username2;
        try {
            username2 = JWT.decode(token).getClaim(SystemConstant.USERNAME).asString();
        } catch (JWTDecodeException e) {
            throw new UnhandledException(HttpStatusExternal.TOKEN_SIGNATURE_FAILED.value(),
                    "验证令牌有误 或令牌已过期",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                    new Date());
        }
        if (username1 == null || !username1.equals(username2)) {
            throw new UnhandledException(HttpStatusExternal.TOKEN_EXPIRED.value(),
                    "验证令牌有误 或令牌已过期",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                    new Date());
        }
        return username2;
    }

    /**
     * 从Token获取UserName
     */
    public static String getUserName(HttpServletRequest request) throws UnhandledException {
        String accessToken = request.getHeader(SystemConstant.AUTHENTICATE_TOKEN_NAME);
        return getUserName(accessToken);
    }

    /**
     * 从Token获取UserId
     */
    public static Long getUserId(String token) throws UnhandledException {
        String userName = getUserName(token);
        return systemUserDao.selectByUsername(userName).getId();
    }
}
