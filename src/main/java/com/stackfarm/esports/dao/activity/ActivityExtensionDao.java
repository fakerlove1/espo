package com.stackfarm.esports.dao.activity;

import com.stackfarm.esports.pojo.activity.ActivityExtension;
import com.stackfarm.esports.pojo.user.UserExtensionOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author xiaohuang
 * @create 2021/4/1 18:03
 */
@Mapper
@Component
public interface ActivityExtensionDao {

    int insert(ActivityExtension record);

    int deleteByActivityId(Long actId);

    int update(ActivityExtension record);

    ActivityExtension selectByActId(Long actId);

    ActivityExtension selectByPicture(String picture);

    ActivityExtension selectByPoster(String poster);


}
