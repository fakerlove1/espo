# 后台系统

## 1. 使用技术

1. 前端框架
   + springboot
   + mybatis

2. 数据库
   + redis
   + mysql

3. 安全框架

   shiro

4. 短信服务

   腾讯云的

5. 邮箱服务

   qq的stmp

## 2. 个人建议

* 个人开发推荐mybatis-plus

* 使用lombok ，简化实体类

* 前端和后台建议分开放置

  前端静态页面，放置在七牛云，阿里云，腾讯云

* 可以研究一下运维，jenkins,docker

* 安全框架建议使用springsecurity

* 日志记录，直接使用全局异常捕获器

* 推荐使用阿里云的oss,数据的本地会直接丢失

* 升级了一下你的 jar包





system_user 用户表

user_infomation 用户的信息表

system_role 用户的所有的权限

system_user_roles 用户的权限表