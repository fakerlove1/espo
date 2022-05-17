package com.stackfarm.esports.dao.authentication;

import com.stackfarm.esports.pojo.authorize.Project;
import com.stackfarm.esports.pojo.authorize.Template;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author croton
 * @create 2021/10/18 19:37
 */
@Repository
@Mapper
public interface TemplateDao {

    int insert(Template template);

    int deleteById(Integer id);

    int update(Template template);

    Template selectById(Integer id);
}
