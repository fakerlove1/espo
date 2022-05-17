package com.stackfarm.esports.dao.sign;

import com.stackfarm.esports.pojo.qrcode.QRCode;
import com.stackfarm.esports.pojo.sign.UserSign;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author croton
 * @create 2021/7/4 10:40
 */
@Mapper
@Repository
public interface UserSignDao {

    int insert(UserSign userSign);

    int deleteById(Long id);

    int update(UserSign userSign);

    UserSign selectByUserIdAndActId(@Param("userId") Long userId, @Param("actId") Long actId);

}
