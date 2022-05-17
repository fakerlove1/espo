package com.stackfarm.esports.service.base.Impl;

import com.stackfarm.esports.dao.user.SystemUserDao;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.user.SystemUser;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.base.BaseService;
import com.stackfarm.esports.utils.BaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author croton
 * @create 2021/4/4 17:27
 */
@Service
@Transactional
public class BaseServiceImpl implements BaseService {

    @Autowired
    private SystemUserDao systemUserDao;

    /**
     * 检查用户名是否存在
     * @param username
     * @return
     * @throws UnhandledException
     */
    @Override
    public ResultBean<Void> checkUsername(String username) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();
        SystemUser user = systemUserDao.selectByUsername(username);
        if (user != null) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "用户名已存在！",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());

        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
}
