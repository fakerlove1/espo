package com.stackfarm.esports.dao.qrcode;

import com.stackfarm.esports.pojo.qrcode.QRCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author croton
 * @create 2021/6/16 19:39
 */
@Repository
@Mapper
public interface QRCodeDao {

    int insert(QRCode qrCode);

    int deleteById(Long id);

    int update(QRCode qrCode);

    QRCode selectByUserIdAndActivityId(@Param("userId") Long userId, @Param("activityId") Long activityId);

}
