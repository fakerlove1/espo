package com.stackfarm.esports.service.authorize.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stackfarm.esports.dao.authentication.AnnualInfoDao;
import com.stackfarm.esports.dao.authentication.ApplicationDao;
import com.stackfarm.esports.dao.authentication.MemberAuthenticationDao;
import com.stackfarm.esports.dao.authentication.SendInformationDao;
import com.stackfarm.esports.dao.user.SystemUserDao;
import com.stackfarm.esports.dao.user.UserExtensionOrganizationDao;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.authorize.AnnualInfo;
import com.stackfarm.esports.pojo.authorize.Application;
import com.stackfarm.esports.pojo.authorize.MemberAuthentication;
import com.stackfarm.esports.pojo.authorize.SendInformation;
import com.stackfarm.esports.pojo.user.SystemUser;
import com.stackfarm.esports.pojo.user.UserExtensionOrganization;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.result.authorize.*;
import com.stackfarm.esports.service.authorize.MemberManageService;
import com.stackfarm.esports.system.ApplicationTypeConstant;
import com.stackfarm.esports.system.AuthenticationStateConstant;
import com.stackfarm.esports.system.MemberTypeConstant;
import com.stackfarm.esports.system.SystemConstant;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.FileUtils;
import com.stackfarm.esports.utils.PropertiesReadUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipOutputStream;

import static com.stackfarm.esports.utils.FileUtils.zipFile;

/**
 * @author croton
 * @create 2021/9/5 13:09
 */
@Service
@Transactional
public class MemberManageServiceImpl implements MemberManageService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberAuthenticationDao memberAuthenticationDao;
    @Autowired
    private ApplicationDao applicationDao;
    @Autowired
    private SendInformationDao sendInformationDao;
    @Autowired
    private SystemUserDao systemUserDao;
    @Autowired
    private AnnualInfoDao annualInfoDao;
    @Autowired
    private UserExtensionOrganizationDao userExtensionOrganizationDao;

    /**
     * 查看申请列表
     * @return
     */
    @Override
    public ResultBean<ApplicationBeans> getApplications(Integer pageNum, Integer pageSize) {
        ResultBean<ApplicationBeans> result = new ResultBean<>();
        ApplicationBeans applicationsBean = new ApplicationBeans();
        List<ApplicationBean> applicationBeans = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Application> applications = applicationDao.selectAll();
        PageInfo<Application> pageInfo = new PageInfo<>(applications);
        if (applications == null) {
            applications = new ArrayList<>();
        }
        for (Application application : applications) {
            ApplicationBean applicationBean = new ApplicationBean();
            applicationBean.setApplicationId(application.getId());
            applicationBean.setApplicationType(application.getApplicationType());
            applicationBean.setCause(application.getCause());
            applicationBean.setCheckTime(application.getCheckTime());
            applicationBean.setCreateTime(application.getCreateTime());
            applicationBean.setProfile(application.getProfile());
            applicationBean.setResult(application.getResult());
            applicationBean.setZipUrl("_" + FileUtils.hidePaths(application.getProfile()) + "_");
            applicationBean.setMemberId(application.getMemberId());

            applicationBeans.add(applicationBean);
        }


        applicationsBean.setApplications(applicationBeans);
        applicationsBean.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        applicationsBean.setSize(pageInfo.getPageSize());
        applicationsBean.setPage(pageInfo.getPages());



        result.setData(applicationsBean);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功!");
        result.setTimestamp(System.currentTimeMillis());

        return result;
    }

    /**
     * 审核初次认证申请
     * @param applicationId
     * @param result
     * @param cause
     * @return
     */
    @Override
    public ResultBean<Void> checkAuthenticationApplication(Long applicationId, Boolean result, String cause) throws TencentCloudSDKException {


        ResultBean<Void> resultBean = new ResultBean<>();
        Application application = applicationDao.selectById(applicationId);
        if (application != null) {
            MemberAuthentication memberAuthentication = memberAuthenticationDao.selectById(application.getMemberId());
            if (memberAuthentication != null) {
                SystemUser club = systemUserDao.selectByUsername(memberAuthentication.getClub());
                if (club == null) {
                    resultBean.setData(null);
                    resultBean.setStatus(HttpStatus.OK.value());
                    resultBean.setMsg("俱乐部信息不存在!");
                    resultBean.setTimestamp(System.currentTimeMillis());
                    return resultBean;
                }
                String email = userExtensionOrganizationDao.selectByUserId(club.getId()).getEmail();
                if (result == true) {
                    application.setCause(cause);
                    application.setResult(result);
                    application.setCheckTime(System.currentTimeMillis());
                    //修改信息
                    if (ApplicationTypeConstant.UPDATE.equals(application.getApplicationType())) {
                        String type = memberAuthentication.getType();
                        String level = memberAuthentication.getLevel();
                        String project = memberAuthentication.getProject();
                        if (type.contains(";")) {
                            type = BaseUtils.getListFromString(type).get(1);
                            memberAuthentication.setType(type);
                        }
                        if (level.contains(";")) {
                            level = BaseUtils.getListFromString(level).get(1);
                            memberAuthentication.setLevel(level);
                        }
                        if (project.contains(";")) {
                            project = BaseUtils.getListFromString(project).get(1);
                            memberAuthentication.setProject(project);
                        }
                        //修改信息审核了之后删掉
                        applicationDao.deleteById(applicationId);

                    } /*初次认证*/else if (ApplicationTypeConstant.AUTHENTICATION.equals(application.getApplicationType())) {
                        memberAuthentication.setState(AuthenticationStateConstant.AUTHORIZED);
                        memberAuthentication.setEffectiveTime(System.currentTimeMillis() + PropertiesReadUtils.AUTHENTICATION_EFFECTIVE_TIME*366*24*60*60*1000);
                        memberAuthentication.setCheckTime(System.currentTimeMillis());
                        //编号
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        List<MemberAuthentication> ms = memberAuthenticationDao.selectAuthorizedByTypeAndProjectAndLevelAndYear(memberAuthentication.getType(), memberAuthentication.getProject(), Integer.parseInt(memberAuthentication.getLevel()), calendar.get(Calendar.YEAR));

                        memberAuthentication.setNumber(""+(ms.size()+1));
                        applicationDao.update(application);
                    }


                } else {
                    //修改信息
                    if (ApplicationTypeConstant.UPDATE.equals(application.getApplicationType())) {
                        String type = memberAuthentication.getType();
                        String level = memberAuthentication.getLevel();
                        String project = memberAuthentication.getProject();
                        if (type.contains(";")) {
                            type = BaseUtils.getListFromString(type).get(0);
                            memberAuthentication.setType(type);
                        }
                        if (level.contains(";")) {
                            level = BaseUtils.getListFromString(level).get(0);
                            memberAuthentication.setLevel(level);
                        }
                        if (project.contains(";")) {
                            project = BaseUtils.getListFromString(project).get(0);
                            memberAuthentication.setProject(project);
                        }
                    } /*初次认证*/else if (ApplicationTypeConstant.AUTHENTICATION.equals(application.getApplicationType())) {
                        memberAuthentication.setState(AuthenticationStateConstant.UNAUTHORIZED);
                    }
                    applicationDao.deleteById(applicationId);
                }

                memberAuthenticationDao.update(memberAuthentication);
                if (ApplicationTypeConstant.UPDATE.equals(application.getApplicationType())) {
                    BaseUtils.sendUpdateMsgByEmail(email, result, memberAuthentication.getName(), cause);
                } else if (ApplicationTypeConstant.AUTHENTICATION.equals(application.getApplicationType())) {
                    BaseUtils.sendAuthenticationMsgByEmail(email, result, memberAuthentication.getName(), cause);
                }

                resultBean.setData(null);
                resultBean.setStatus(HttpStatus.OK.value());
                resultBean.setMsg("审核成功!");
                resultBean.setTimestamp(System.currentTimeMillis());
            } else {
                resultBean.setData(null);
                resultBean.setStatus(HttpStatus.OK.value());
                resultBean.setMsg("审核失败，无此信息!");
                resultBean.setTimestamp(System.currentTimeMillis());
            }
        } else{
            resultBean.setData(null);
            resultBean.setStatus(HttpStatus.OK.value());
            resultBean.setMsg("审核失败，无此申请!");
            resultBean.setTimestamp(System.currentTimeMillis());
        }

        return resultBean;
    }

    /**
     * 审核注销申请
     * @param applicationId
     * @param result
     * @param cause
     * @return
     */
    @Override
    public ResultBean<Void> checkLogoutApplication(Long applicationId, Boolean result, String cause) {
        ResultBean<Void> resultBean = new ResultBean<>();
        Application application = applicationDao.selectById(applicationId);
        if (application != null) {
            MemberAuthentication memberAuthentication = memberAuthenticationDao.selectById(application.getMemberId());
            if (memberAuthentication != null) {
                if (!AuthenticationStateConstant.AUTHORIZED.equals(memberAuthentication.getState())) {
                    resultBean.setData(null);
                    resultBean.setStatus(HttpStatus.OK.value());
                    resultBean.setMsg("非已认证状态不可注销!");
                    resultBean.setTimestamp(System.currentTimeMillis());

                    return resultBean;
                }
                application.setCause(cause);
                application.setResult(result);
                application.setCheckTime(System.currentTimeMillis());
                if (result == true) {
                    //未删
                    memberAuthentication.setState(AuthenticationStateConstant.LOGOUT);
                    memberAuthentication.setEffectiveTime(0l);
                    memberAuthentication.setCheckTime(System.currentTimeMillis());
                    applicationDao.deleteById(applicationId);
                } else {
                    memberAuthentication.setState(AuthenticationStateConstant.AUTHORIZED);
                    applicationDao.deleteById(applicationId);
                }

                memberAuthenticationDao.update(memberAuthentication);

                resultBean.setData(null);
                resultBean.setStatus(HttpStatus.OK.value());
                resultBean.setMsg("审核成功!");
                resultBean.setTimestamp(System.currentTimeMillis());
            } else {
                resultBean.setData(null);
                resultBean.setStatus(HttpStatus.OK.value());
                resultBean.setMsg("审核失败，无此信息!");
                resultBean.setTimestamp(System.currentTimeMillis());
            }
        } else{
            resultBean.setData(null);
            resultBean.setStatus(HttpStatus.OK.value());
            resultBean.setMsg("审核失败，无此申请!");
            resultBean.setTimestamp(System.currentTimeMillis());
        }

        return resultBean;
    }

    /**
     * 查看已认证列表
     * @return
     */
    @Override
    public ResultBean<AuthorizedBeans> getAuthorised(Integer pageNum, Integer pageSize) {
        ResultBean<AuthorizedBeans> result = new ResultBean<>();

        AuthorizedBeans authorizedBeans = new AuthorizedBeans();
        List<AuthorizedBean> authorizedBeanList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<MemberAuthentication> memberAuthentications = memberAuthenticationDao.selectAll();
        PageInfo<MemberAuthentication> pageInfo = new PageInfo<>();
        if (memberAuthentications == null) {
            memberAuthentications = new ArrayList<>();
        }
        for (MemberAuthentication memberAuthentication : memberAuthentications) {
            AuthorizedBean authorizedBean = new AuthorizedBean();
            authorizedBean.setMember(memberAuthentication);
            authorizedBean.setEnrollApplicationUrl(FileUtils.hidePath(memberAuthentication.getEnrollApplication()));
            authorizedBean.setQualificationProtocolUrl(FileUtils.hidePath(memberAuthentication.getQualificationProtocol()));
            authorizedBean.setAgreementUrl(FileUtils.hidePath(memberAuthentication.getAgreement()));
            authorizedBean.setPhotoUrl(FileUtils.hidePath(memberAuthentication.getPhoto()));
            authorizedBean.setIdcardUrl(FileUtils.hidePath(memberAuthentication.getIdcard()));
            authorizedBean.setExtraEvidenceUrl(FileUtils.hidePath(memberAuthentication.getExtraEvidence()));

            authorizedBeanList.add(authorizedBean);
        }

        authorizedBeans.setAuthorizedBeans(authorizedBeanList);
        authorizedBeans.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        authorizedBeans.setSize(pageInfo.getPageSize());
        authorizedBeans.setPage(pageInfo.getPages());

        result.setData(authorizedBeans);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功!");
        result.setTimestamp(System.currentTimeMillis());

        return result;
    }

    /**
     * 查看年审列表
     * @return
     */
    @Override
    public ResultBean<AuthorizedBeans> getAnnual(Integer pageNum, Integer pageSize) {
        ResultBean<AuthorizedBeans> result = new ResultBean<>();

        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        //todo
        if (month != 12) {
            result.setData(null);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("未到年审时间!");
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }

        AuthorizedBeans authorizedBeans = new AuthorizedBeans();
        List<AuthorizedBean> authorizedBeanList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<MemberAuthentication> memberAuthentications = memberAuthenticationDao.selectByStatePlus(AuthenticationStateConstant.ANNUAL_AUTHORIZING, AuthenticationStateConstant.ANNUAL_UNAUTHORIZED);
        PageInfo<MemberAuthentication> pageInfo = new PageInfo<>(memberAuthentications);
        if (memberAuthentications == null) {
            memberAuthentications = new ArrayList<>();
        }
        for (MemberAuthentication memberAuthentication : memberAuthentications) {
            AuthorizedBean authorizedBean = new AuthorizedBean();
            authorizedBean.setMember(memberAuthentication);
            authorizedBean.setEnrollApplicationUrl(FileUtils.hidePath(memberAuthentication.getEnrollApplication()));
            authorizedBean.setQualificationProtocolUrl(FileUtils.hidePath(memberAuthentication.getQualificationProtocol()));
            authorizedBean.setAgreementUrl(FileUtils.hidePath(memberAuthentication.getAgreement()));
            authorizedBean.setPhotoUrl(FileUtils.hidePath(memberAuthentication.getPhoto()));
            authorizedBean.setIdcardUrl(FileUtils.hidePath(memberAuthentication.getIdcard()));
            authorizedBean.setExtraEvidenceUrl(FileUtils.hidePath(memberAuthentication.getExtraEvidence()));

            authorizedBeanList.add(authorizedBean);
        }

        authorizedBeans.setAuthorizedBeans(authorizedBeanList);
        authorizedBeans.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        authorizedBeans.setSize(pageInfo.getPageSize());
        authorizedBeans.setPage(pageInfo.getPages());

        result.setData(authorizedBeans);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功!");
        result.setTimestamp(System.currentTimeMillis());

        return result;
    }

    /**
     * 根据成员认证id查看认证信息
     * @param memberId
     * @return
     */
    @Override
    public ResultBean<AuthorizedBean> getAuthenticationDetail(Long memberId) {
        ResultBean<AuthorizedBean> result = new ResultBean<>();
        MemberAuthentication memberAuthentication = memberAuthenticationDao.selectById(memberId);
        if (memberAuthentication != null) {
            AuthorizedBean authorizedBean = new AuthorizedBean();
            authorizedBean.setMember(memberAuthentication);
            authorizedBean.setEnrollApplicationUrl(FileUtils.hidePath(memberAuthentication.getEnrollApplication()));
            authorizedBean.setQualificationProtocolUrl(FileUtils.hidePath(memberAuthentication.getQualificationProtocol()));
            authorizedBean.setAgreementUrl(FileUtils.hidePath(memberAuthentication.getAgreement()));
            authorizedBean.setPhotoUrl(FileUtils.hidePath(memberAuthentication.getPhoto()));
            authorizedBean.setIdcardUrl(FileUtils.hidePath(memberAuthentication.getIdcard()));
            authorizedBean.setExtraEvidenceUrl(FileUtils.hidePath(memberAuthentication.getExtraEvidence()));


            result.setData(authorizedBean);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("查看成功!");
            result.setTimestamp(System.currentTimeMillis());
        } else {
            result.setData(null);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("查看失败，无此认证成员!");
            result.setTimestamp(System.currentTimeMillis());
        }

        return result;
    }

    /**
     * 年审
     * @param club
     * @param result
     * @param cause
     * @return
     */
    @Override
    public ResultBean<Void> annual(String club, Boolean result, String cause) throws UnhandledException, TencentCloudSDKException {
        ResultBean<Void> resultBean = new ResultBean<>();

        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        //todo 月份改回来
        if (month != 12) {
            resultBean.setData(null);
            resultBean.setStatus(HttpStatus.OK.value());
            resultBean.setMsg("未到年审时间!");
            resultBean.setTimestamp(System.currentTimeMillis());
        } else {
            UnhandledException unhandledException = new UnhandledException();
            AnnualInfo annualInfo = annualInfoDao.selectByClubAndYear(club, calendar.get(Calendar.YEAR));

            if (annualInfo == null) {
                unhandledException.setMsg("无此年审信息！");
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                throw unhandledException;
            }

            SystemUser user = systemUserDao.selectByUsername(club);

            UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(user.getId());
            if (userExtensionOrganization == null) {
                unhandledException.setMsg("无此团体扩展信息！");
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                throw unhandledException;
            }
            if (user == null) {
                unhandledException.setMsg("无此团体信息！");
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                throw unhandledException;
            }
            List<MemberAuthentication> memberAuthentications = memberAuthenticationDao.selectByClubAndState(club, AuthenticationStateConstant.ANNUAL_AUTHORIZING);
            if (memberAuthentications == null) {
                memberAuthentications = new ArrayList<>();
            }
            //未通过
            if (!result) {
                annualInfo.setState(0);
                annualInfo.setFullPath(null);
                annualInfoDao.update(annualInfo);
                //该俱乐部所有运动员状态改为年审未通过
                for (MemberAuthentication memberAuthentication : memberAuthentications) {
                    if (MemberTypeConstant.AMATEUR_PLAYER.equals(memberAuthentication.getType()) || MemberTypeConstant.PROFESSIONAL_PLAYER.equals(memberAuthentication.getType())) {
                        memberAuthentication.setState(AuthenticationStateConstant.ANNUAL_UNAUTHORIZED);
                        memberAuthentication.setCheckTime(System.currentTimeMillis());
//                        String type = memberAuthentication.getType();
//                        String level = memberAuthentication.getLevel();
//                        String project = memberAuthentication.getProject();
//                        if (type.contains(";")) {
//                            type = BaseUtils.getListFromString(type).get(1);
//                            memberAuthentication.setType(type);
//                        }
//                        if (level.contains(";")) {
//                            level = BaseUtils.getListFromString(level).get(1);
//                            memberAuthentication.setLevel(level);
//                        }
//                        if (project.contains(";")) {
//                            project = BaseUtils.getListFromString(project).get(1);
//                            memberAuthentication.setProject(project);
//                        }
                        memberAuthenticationDao.update(memberAuthentication);
                    }
                }
                BaseUtils.sendAnnualAuthenticationMsgByEmail(userExtensionOrganization.getEmail(), result, cause);
            } else {
                //该俱乐部所有运动员状态改为已认证
                for (MemberAuthentication memberAuthentication : memberAuthentications) {
                    if (MemberTypeConstant.AMATEUR_PLAYER.equals(memberAuthentication.getType()) || MemberTypeConstant.PROFESSIONAL_PLAYER.equals(memberAuthentication.getType())) {
                        memberAuthentication.setState(AuthenticationStateConstant.AUTHORIZED);
                        memberAuthentication.setCheckTime(System.currentTimeMillis());
                        String type = memberAuthentication.getType();
                        String level = memberAuthentication.getLevel();
                        String project = memberAuthentication.getProject();
                        if (type.contains(";")) {
                            type = BaseUtils.getListFromString(type).get(0);
                            memberAuthentication.setType(type);
                        }
                        if (level.contains(";")) {
                            level = BaseUtils.getListFromString(level).get(0);
                            memberAuthentication.setLevel(level);
                        }
                        if (project.contains(";")) {
                            project = BaseUtils.getListFromString(project).get(0);
                            memberAuthentication.setProject(project);
                        }
                        memberAuthenticationDao.update(memberAuthentication);

                    }
                }
                annualInfo.setState(1);

                annualInfoDao.update(annualInfo);
                BaseUtils.sendAnnualAuthenticationMsgByEmail(userExtensionOrganization.getEmail(), result, cause);
            }


            resultBean.setData(null);
            resultBean.setStatus(HttpStatus.OK.value());
            resultBean.setMsg("年审完成!");
            resultBean.setTimestamp(System.currentTimeMillis());
        }
        return resultBean;
    }

    @Override
    public ResultBean<AnnualAuthenticationBeans> getAnnualList(Integer pageNum, Integer pageSize) throws UnhandledException {
        ResultBean<AnnualAuthenticationBeans> result = new ResultBean<>();
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        //todo 月份改回来
        if (month != 12) {
            result.setData(null);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("未到年审时间!");
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }

        AnnualAuthenticationBeans annualAuthenticationBeans = new AnnualAuthenticationBeans();
        StringBuilder sb = new StringBuilder();
        List<AnnualAuthenticationBean> annualAuthenticationBeanList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<String> clubList = memberAuthenticationDao.selectAllClubContainsPlayer();
        PageInfo<String> pageInfo = new PageInfo<>(clubList);
        for (String club : clubList) {
            AnnualAuthenticationBean annualAuthenticationBean = new AnnualAuthenticationBean();
            AnnualInfo annualInfo = annualInfoDao.selectByClubAndYear(club, calendar.get(Calendar.YEAR));
            if (annualInfo == null) {
                UnhandledException unhandledException = new UnhandledException();
                unhandledException.setMsg("有遗漏的年审信息！");
                unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
                unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
                unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                throw unhandledException;
            } else {
                String url = FileUtils.hidePath(annualInfo.getFullPath());
                annualAuthenticationBean.setClub(club);
                annualAuthenticationBean.setFileUrl(url);
                if (!"".equals(url)) {
                    sb.append(url).append("_");
                }


                if (annualInfo.getState() == 1) {//已通过
                    annualAuthenticationBean.setState("已通过");
                } else if (annualInfo.getState() == 0 && annualInfo.getFullPath() == null) {//已提交未通过
                    annualAuthenticationBean.setState("未通过");
                } else if (-1 == annualInfo.getState() && annualInfo.getFullPath() == null) {//未提交
                    annualAuthenticationBean.setState("已提交");
                } else if (-1 == annualInfo.getState() && annualInfo.getFullPath() != null) {//已提交未审核
                    annualAuthenticationBean.setState("未审核");
                }
            }
            annualAuthenticationBeanList.add(annualAuthenticationBean);
        }

        annualAuthenticationBeans.setAnnualAuthenticationBeanList(annualAuthenticationBeanList);
        annualAuthenticationBeans.setFilesUrl(sb.toString());
        annualAuthenticationBeans.setPage(pageInfo.getPages());
        annualAuthenticationBeans.setSize(pageInfo.getPageSize());
        annualAuthenticationBeans.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));

        result.setData(annualAuthenticationBeans);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功!");
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @Override
    public ResultBean<Void> downloadAnnualZip(HttpServletRequest request, HttpServletResponse response, String uuid) throws Exception {
        ResultBean<Void> result = new ResultBean<>();
        List<String> uuids = BaseUtils.getFileListFromString(uuid);
        List<File> files = new ArrayList<>();
        for (String u : uuids) {
            if (u != null & !"".equals(u)) {
                Map<String, String> hashMap = parseUuid(u);
                String fileFullPath = hashMap.get("fileFullPath");
                files.add(new File(fileFullPath));
            }
        }

        downLoadFiles(files, response, request, "年审材料");


        result.setStatus(HttpStatus.OK.value());
        result.setMsg("下载成功！");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 查看邮寄申请列表
     * @return
     */
    @Override
    public ResultBean<PostBean> getPostApplications(Integer pageNum, Integer pageSize) {
        ResultBean<PostBean> result = new ResultBean<>();
        PostBean postBean = new PostBean();
        PageHelper.startPage(pageNum, pageSize);
        List<SendInformation> sendInformations = sendInformationDao.selectAll();
        PageInfo<SendInformation> pageInfo = new PageInfo<>(sendInformations);

        if (sendInformations == null) {
            sendInformations = new ArrayList<>();
        }

        for (SendInformation sendInformation : sendInformations) {
            sendInformation.setEvidence(FileUtils.hidePath(sendInformation.getEvidence()));
        }

        postBean.setSendInformations(sendInformations);
        postBean.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        postBean.setSize(pageInfo.getPageSize());
        postBean.setPage(pageInfo.getPages());

        result.setData(postBean);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功!");
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 处理邮寄单
     * @param postId
     * @param state
     * @return
     */
    @Override
    public ResultBean<Void> post(Long postId, Boolean state, String cause, String trackingNumber) throws TencentCloudSDKException {
        ResultBean<Void> result = new ResultBean<>();
        SendInformation sendInformation = sendInformationDao.selectById(postId);
        String email = "";
        String name = "";
        if (sendInformation != null) {
            MemberAuthentication memberAuthentication = memberAuthenticationDao.selectById(sendInformation.getMemberId());

            name = memberAuthentication.getName();
            if (memberAuthentication != null) {
                SystemUser club = systemUserDao.selectByUsername(memberAuthentication.getClub());
                if (club != null) {
                    UserExtensionOrganization userExtensionOrganization = userExtensionOrganizationDao.selectByUserId(club.getId());
                    if (userExtensionOrganization != null) {
                        email = userExtensionOrganization.getEmail();
                    }
                }
            }
        }
        if (email == null || "".equals(email)) {
            result.setData(null);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("邮箱为空!");
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }



        if (sendInformation != null) {

            if (state == true) {
                sendInformation.setTrackingNumber(trackingNumber);
                sendInformation.setState(state);
                sendInformationDao.update(sendInformation);
                BaseUtils.sendPostMsgByEmail(email, true, name, cause, trackingNumber);
            } else {
                sendInformationDao.deleteById(postId);
                BaseUtils.sendPostMsgByEmail(email, false, name, cause, trackingNumber);
            }


            result.setData(null);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("处理完成!");
            result.setTimestamp(System.currentTimeMillis());
        } else {
            result.setData(null);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("信息不存在!");
            result.setTimestamp(System.currentTimeMillis());
        }
        return result;
    }

    /**
     * 添加裁判员
     * @param name
     * @param sex
     * @param birth
     * @param idcard
     * @param photo
     * @return
     */
    @Override
    public ResultBean<Void> addJudge(String name, String sex, String birth, String idcard, String photo) {
        ResultBean<Void> result = new ResultBean<>();
        MemberAuthentication memberAuthentication = new MemberAuthentication();
        memberAuthentication.setName(name);
        memberAuthentication.setSex(sex);
        memberAuthentication.setIdcard(idcard);
        memberAuthentication.setBirth(birth);
        memberAuthentication.setPhoto(photo);
        memberAuthentication.setType(MemberTypeConstant.JUDGE);
        memberAuthentication.setState(AuthenticationStateConstant.AUTHORIZED);
        memberAuthentication.setCreateTime(System.currentTimeMillis());
        memberAuthentication.setCheckTime(System.currentTimeMillis());
        memberAuthentication.setEffectiveTime(System.currentTimeMillis()+PropertiesReadUtils.AUTHENTICATION_EFFECTIVE_TIME*366*24*60*60*1000);

        memberAuthenticationDao.insert(memberAuthentication);

        result.setData(null);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("添加成功!");
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 注销裁判员
     * @param memberId
     * @return
     */
    @Override
    public ResultBean<Void> deleteJudge(Long memberId) {
        ResultBean<Void> result = new ResultBean<>();
        MemberAuthentication member = memberAuthenticationDao.selectById(memberId);
        if (member != null) {
            member.setEffectiveTime(0l);
            member.setState(AuthenticationStateConstant.LOGOUT);
            memberAuthenticationDao.update(member);

            result.setData(null);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("注销成功!");
            result.setTimestamp(System.currentTimeMillis());
        } else {
            result.setData(null);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("注销失败，该信息不存在!");
            result.setTimestamp(System.currentTimeMillis());
        }

        return result;
    }

    /**
     * 查看全部年审记录
     * @param clubName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResultBean<AllAnnualListBean> getAllAnnualList(String clubName, Integer pageNum, Integer pageSize) {
        ResultBean<AllAnnualListBean> result = new ResultBean<>();
        AllAnnualListBean allAnnualListBean = new AllAnnualListBean();
        PageHelper.startPage(pageNum, pageSize);
        List<AnnualInfo> annualInfos = annualInfoDao.selectByClub(clubName);
        PageInfo<AnnualInfo> pageInfo = new PageInfo<>(annualInfos);

        if (annualInfos == null) {
            annualInfos = new ArrayList<>();
        }


        allAnnualListBean.setAnnualInfos(annualInfos);
        allAnnualListBean.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        allAnnualListBean.setSize(pageInfo.getPageSize());
        allAnnualListBean.setPage(pageInfo.getPages());


        result.setData(allAnnualListBean);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功!");
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }


    private static HttpServletResponse downLoadFiles(List<File> files, HttpServletResponse response, HttpServletRequest request, String name) throws Exception {

        try {
            // 临时文件夹 最好是放在服务器上，方法最后有删除临时文件的步骤
            String zipFilename = SystemConstant.ZIP_FILE_TEMPORARY_PATH + File.separator + name + ".zip";
            File file = new File(zipFilename);
            file.createNewFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            response.reset();
            // response.getWriter()
            // 创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            zipFile(files, zipOut);
            zipOut.close();
            fous.close();
            return downloadZip(file, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    private static HttpServletResponse downloadZip(File file, HttpServletResponse response, HttpServletRequest request) throws UnhandledException {
        if (file.exists() == false) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "待压缩的文件目录：" + file + "不存在.",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        } else {
            try {
                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();

                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                String fileName = file.getName();
                String userAgent = request.getHeader("User-Agent").toLowerCase();
                // 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
                if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
                    byte[] bytesName = fileName.getBytes("UTF-8");
                    fileName = new String(bytesName, "ISO-8859-1");
                    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                } else {
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20"));
                }

                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    File f = new File(file.getPath());
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }



    private HashMap<String, String> parseUuid(String uuid) throws UnhandledException {
        String attributes = redisTemplate.opsForValue().get(uuid) + "";

        //todo 文件属性为空

        if ("".equals(attributes) || "null".equals(attributes)) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "文件属性为空",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
        }
        HashMap<String, String> hashMap;
        try {
            hashMap = objectMapper.readValue(attributes, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "解析文件属性时发生错误",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }
        BaseUtils.getValueWithThrow(hashMap.get("fileFullPath"), BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
        return hashMap;
    }
}
