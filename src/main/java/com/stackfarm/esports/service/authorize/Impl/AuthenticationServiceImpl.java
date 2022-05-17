package com.stackfarm.esports.service.authorize.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stackfarm.esports.dao.authentication.AnnualInfoDao;
import com.stackfarm.esports.dao.authentication.ApplicationDao;
import com.stackfarm.esports.dao.authentication.MemberAuthenticationDao;
import com.stackfarm.esports.dao.authentication.SendInformationDao;
import com.stackfarm.esports.dao.user.SystemUserDao;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.authorize.AnnualInfo;
import com.stackfarm.esports.pojo.authorize.Application;
import com.stackfarm.esports.pojo.authorize.MemberAuthentication;
import com.stackfarm.esports.pojo.authorize.SendInformation;
import com.stackfarm.esports.pojo.user.SystemUser;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.result.authorize.*;
import com.stackfarm.esports.service.authorize.AuthenticationService;
import com.stackfarm.esports.system.ApplicationTypeConstant;
import com.stackfarm.esports.system.AuthenticationStateConstant;
import com.stackfarm.esports.system.SystemConstant;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author xiaohuang
 * @create 2021/9/9 14:55
 */
@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private ApplicationDao applicationDao;
    @Autowired
    private MemberAuthenticationDao memberAuthenticationDao;
    @Autowired
    private SendInformationDao sendInformationDao;

    @Autowired
    private AnnualInfoDao annualInfoDao;

    @Autowired
    private SystemUserDao systemUserDao;

    @Override
    public ResultBean<?> submitCertification(String name, String sex, String birth, String project,
                                             String type, String level, String club, String clubType,
                                             String origin, String idcardNumber,
                                             MultipartFile enrollApplication, MultipartFile qualificationProtocol,
                                             MultipartFile agreement, MultipartFile idcard, MultipartFile photo, MultipartFile extraEvidence, MultipartFile otherFileZip, String cardType) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();

        //默认第一次认证
        boolean isFirst = true;
        MemberAuthentication member = memberAuthenticationDao.selectByClubAndIdNumber(club, idcardNumber);
        if (member != null && AuthenticationStateConstant.AUTHORIZING.equals(member.getState())) {
            isFirst = false;
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("认证中，请等待审核，勿重复提交!");
            result.setTimestamp(System.currentTimeMillis());
            return result;
        } else if (member != null && AuthenticationStateConstant.AUTHORIZED.equals(member.getState())) {
            isFirst = false;
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("已认证，请勿重复提交!");
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }
        //说明是认证未通过
        if (member != null) {
            isFirst = false;
        } else {
            isFirst = true;
            member = new MemberAuthentication();
        }


        member.setName(name);
        member.setSex(sex);
        member.setBirth(birth);
        member.setProject(project);
        member.setType(type); //类型，运动员/教练员/裁判员
        member.setLevel(level);
        member.setClub(club);//所在俱乐部名称
        member.setClubType(clubType);//所属俱乐部类型，省级/市级/...
        member.setOrigin(origin);
        member.setIdCardNumber(idcardNumber);

        String enrollApplicationFileName = enrollApplication.getOriginalFilename();
        String enrollApplicationPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "运动员注册申请表" + File.separatorChar + enrollApplicationFileName;

        String qualificationProtocolFileName = qualificationProtocol.getOriginalFilename();
        String qualificationProtocolPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "资格协议" + File.separatorChar + qualificationProtocolFileName;

        String agreementFileName = agreement.getOriginalFilename();
        String agreementPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "聘用合同" + File.separatorChar + agreementFileName;

        String idcardFileName = idcard.getOriginalFilename();
        String idcardPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "身份证" + File.separatorChar + idcardFileName;

        String photoFileName = photo.getOriginalFilename();
        String photoPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "证件照" + File.separatorChar + photoFileName;

        String otherFileZipName = otherFileZip.getOriginalFilename();
        String otherFileZipPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "其他材料" + File.separatorChar + otherFileZipName;

        String extraEvidencePath = "";
        if (extraEvidence != null) {
            String extraEvidenceFileName = extraEvidence.getOriginalFilename();
            extraEvidencePath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                    + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "证明材料" + File.separatorChar + extraEvidenceFileName;

            try {
                Path extraEvidencePathFile = FileUtils.forceCreateFile(extraEvidencePath);
                extraEvidence.transferTo(extraEvidencePathFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
            member.setExtraEvidence(extraEvidencePath);
        }


        try {
            Path enrollApplicationPathFile = FileUtils.forceCreateFile(enrollApplicationPath);
            enrollApplication.transferTo(enrollApplicationPathFile);
            Path qualificationProtocolPathFile = FileUtils.forceCreateFile(qualificationProtocolPath);
            qualificationProtocol.transferTo(qualificationProtocolPathFile);
            Path agreementPathFile = FileUtils.forceCreateFile(agreementPath);
            agreement.transferTo(agreementPathFile);
            Path idcardPathFile = FileUtils.forceCreateFile(idcardPath);
            idcard.transferTo(idcardPathFile);
            Path photoPathFile = FileUtils.forceCreateFile(photoPath);
            photo.transferTo(photoPathFile);
            Path otherFileZipFile = FileUtils.forceCreateFile(otherFileZipPath);
            otherFileZip.transferTo(otherFileZipFile);

            member.setEnrollApplication(enrollApplicationPath);
            member.setQualificationProtocol(qualificationProtocolPath);
            member.setAgreement(agreementPath);
            member.setIdcard(idcardPath);
            member.setCardType(cardType);
            member.setPhoto(photoPath);
            member.setOtherFileZip(otherFileZipPath);


        } catch (Exception e) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("文件存储失败");
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            throw unhandledException;
        }

        member.setCreateTime(System.currentTimeMillis());
        member.setUpdateTime(System.currentTimeMillis());
        member.setState(AuthenticationStateConstant.AUTHORIZING);

        if (isFirst) {
            memberAuthenticationDao.insert(member);
        } else {
            memberAuthenticationDao.update(member);
        }


        //对application进行操作
        MemberAuthentication newMember = memberAuthenticationDao.selectByNameAndSexAndBirth(name, sex, birth);
        Application application = new Application();
        application.setMemberId(newMember.getId());
        application.setApplicationType(ApplicationTypeConstant.AUTHENTICATION);
        application.setCreateTime(System.currentTimeMillis());
        String profile = "";
        if (!"".equals(extraEvidencePath)) {
            profile = enrollApplicationPath + ";" + qualificationProtocolPath + ";" + agreementPath + ";"
                    + idcardPath + ";" + photoPath + ";" + extraEvidencePath + ";" + otherFileZipPath + ";";
        } else {
            profile = enrollApplicationPath + ";" + qualificationProtocolPath + ";" + agreementPath + ";"
                    + idcardPath + ";" + photoPath + ";" + otherFileZipPath + ";";
        }

        application.setProfile(profile);

        applicationDao.insert(application);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("申请成功！");
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @Override
    public ResultBean<ApplicationBeans> getApplication(String club, Integer pageNum, Integer pageSize) {
        ResultBean<ApplicationBeans> result = new ResultBean<>();
        ApplicationBeans applicationBeans = new ApplicationBeans();
        List<ApplicationBean> applicationBeanList = new ArrayList<>();
        PageHelper.startPage(pageNum,pageSize);
        List<MemberAuthentication> memberAuthentications = memberAuthenticationDao.selectByClub(club);
        PageInfo<MemberAuthentication> pageInfo = new PageInfo<>(memberAuthentications);
        for (MemberAuthentication memberAuthentication : memberAuthentications) {
            Application a1 = applicationDao.selectByMemberIdAndType(memberAuthentication.getId(), ApplicationTypeConstant.AUTHENTICATION);
            Application a2 = applicationDao.selectByMemberIdAndType(memberAuthentication.getId(), ApplicationTypeConstant.LOGOUT);
            if (a1 != null) {
                ApplicationBean applicationBean1 = new ApplicationBean();
                applicationBean1.setApplicationId(a1.getId());
                applicationBean1.setApplicationType(a1.getApplicationType());
                applicationBean1.setCause(a1.getCause());
                applicationBean1.setCheckTime(a1.getCheckTime());
                applicationBean1.setCreateTime(a1.getCreateTime());
                applicationBean1.setMemberId(a1.getMemberId());
                applicationBean1.setProfile(a1.getProfile());
                applicationBean1.setResult(a1.getResult());
                applicationBean1.setZipUrl("_" + FileUtils.hidePaths(a1.getProfile()) + "_");
                applicationBeanList.add(applicationBean1);
            }
            if (a2 != null) {
                ApplicationBean applicationBean2 = new ApplicationBean();
                applicationBean2.setApplicationId(a2.getId());
                applicationBean2.setApplicationType(a2.getApplicationType());
                applicationBean2.setCause(a2.getCause());
                applicationBean2.setCheckTime(a2.getCheckTime());
                applicationBean2.setCreateTime(a2.getCreateTime());
                applicationBean2.setMemberId(a2.getMemberId());
                applicationBean2.setProfile(a2.getProfile());
                applicationBean2.setResult(a2.getResult());
                applicationBean2.setZipUrl("_" + FileUtils.hidePaths(a2.getProfile()) + "_");
                applicationBeanList.add(applicationBean2);
            }
        }

        applicationBeans.setApplications(applicationBeanList);
        applicationBeans.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        applicationBeans.setSize(pageInfo.getPageSize());
        applicationBeans.setPage(pageInfo.getPages());

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功");
        result.setData(applicationBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @Override
    public ResultBean<AuthorizedBeans> getAuthenticationList(String club, String type/*, Integer pageNum, Integer pageSize*/) {
        ResultBean<AuthorizedBeans> result = new ResultBean<>();
        AuthorizedBeans authorizedBeans = new AuthorizedBeans();
        List<MemberAuthentication> authentications = new ArrayList<>();

        List<AuthorizedBean> authorizedBeanList = new ArrayList<>();
        if (type == null) {
//            PageHelper.startPage(pageNum, pageSize);
            authentications = memberAuthenticationDao.selectAuthorizedByClub(club);
        } else {
            //            PageHelper.startPage(pageNum, pageSize);
            authentications = memberAuthenticationDao.selectAuthorizedByClubAndType(club, type);

        }
//        PageInfo<MemberAuthentication> pageInfo = new PageInfo<>(authentications);



        for (MemberAuthentication authentication : authentications) {
            AuthorizedBean authorizedBean = new AuthorizedBean();
            authorizedBean.setMember(authentication);
            authorizedBean.setEnrollApplicationUrl(FileUtils.hidePath(authentication.getEnrollApplication()));
            authorizedBean.setQualificationProtocolUrl(FileUtils.hidePath(authentication.getQualificationProtocol()));
            authorizedBean.setAgreementUrl(FileUtils.hidePath(authentication.getAgreement()));
            authorizedBean.setPhotoUrl(FileUtils.hidePath(authentication.getPhoto()));
            authorizedBean.setIdcardUrl(FileUtils.hidePath(authentication.getIdcard()));
            authorizedBean.setExtraEvidenceUrl(FileUtils.hidePath(authentication.getExtraEvidence()));

            authorizedBeanList.add(authorizedBean);
        }

        authorizedBeans.setAuthorizedBeans(authorizedBeanList);
//        authorizedBeans.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
//        authorizedBeans.setSize(pageInfo.getPageSize());
//        authorizedBeans.setPage(pageInfo.getPages());


        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功");
        result.setData(authorizedBeans);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @Override
    public ResultBean<?> logoutApplication(Long memberId, String cause, MultipartFile[] material) throws IOException {
        ResultBean<Void> result = new ResultBean<>();
        Application application = applicationDao.selectByMemberIdAndType(memberId, ApplicationTypeConstant.LOGOUT);
        int flag = 0;

        if (application == null) {
            application = new Application();
            flag = 1;
        }
        application.setMemberId(memberId);
        application.setApplicationType(ApplicationTypeConstant.LOGOUT);
        application.setCreateTime(System.currentTimeMillis());
        application.setCause(cause);

        MemberAuthentication memberAuthentication = memberAuthenticationDao.selectById(memberId);
        String profile = "";
        if(material.length != 0) {
            for (MultipartFile file: material) {
                String materialFileName = file.getOriginalFilename();
                String materialPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "注销材料" + File.separatorChar +
                        memberAuthentication.getClub() + File.separatorChar + memberAuthentication.getName() + File.separatorChar + materialFileName;
                profile = profile + materialPath + ";";
                Path materialPathFile = FileUtils.forceCreateFile(materialPath);
                file.transferTo(materialPathFile);
            }
        }
        application.setProfile(profile);
        application.setResult(null);
        if (flag == 1) {
            applicationDao.insert(application);
        } else {
            applicationDao.update(application);
        }

        memberAuthentication.setState(AuthenticationStateConstant.LOGOUTING);
        memberAuthentication.setUpdateTime(System.currentTimeMillis());
        memberAuthenticationDao.update(memberAuthentication);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("已申请注销");
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @Override
    public ResultBean<?> getOneAuthentication(Long memberId) {
        ResultBean<MemberAuthentication> result = new ResultBean<>();
        MemberAuthentication memberAuthentication = memberAuthenticationDao.selectById(memberId);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功");
        result.setData(memberAuthentication);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @Override
    public ResultBean<?> updateAuthentication(Long memberId, /*String name, String sex, String birth,*/ String project,
                                              String type, String level, String club, String clubType,
                                              String origin, String idcardNumber,
                                              MultipartFile enrollApplication, MultipartFile qualificationProtocol,
                                              MultipartFile agreement, MultipartFile idcard, MultipartFile photo,
                                              MultipartFile extraEvidence, MultipartFile otherFileZip) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH)+1;

        //todo 时间
        if (month != 1) {

            result.setData(null);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("未到年审时间，不可修改!");
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }

        MemberAuthentication memberAuthentication = memberAuthenticationDao.selectById(memberId);
        if (memberAuthentication == null) {
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("无此成员信息！");
            result.setTimestamp(System.currentTimeMillis());
            result.setData(null);
            return result;
        }
        Application application = applicationDao.selectByMemberIdAndType(memberId, ApplicationTypeConstant.UPDATE);

        if (application != null) {
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("修改的信息审核中，请勿重复提交！");
            result.setTimestamp(System.currentTimeMillis());
            result.setData(null);
            return result;
        } else {
            application = new Application();
            application.setMemberId(memberId);
            application.setApplicationType(ApplicationTypeConstant.UPDATE);
            application.setCreateTime(System.currentTimeMillis());

            applicationDao.insert(application);
        }
        /*if(name != null) {
            memberAuthentication.setName(name);
        }
        if(sex != null) {
            memberAuthentication.setSex(sex);
        }
        if(birth != null) {
            memberAuthentication.setBirth(birth);
        }*/
        if(origin != null) {
            memberAuthentication.setOrigin(origin);
        }
        if(idcardNumber != null) {
            memberAuthentication.setIdCardNumber(idcardNumber);
        }
        String name = memberAuthentication.getName();
        //其余全部更新
        //若修改了，member里的对应信息变成 “旧信息;新信息;” 的格式，审核通过后变回 “新信息” 的格式，反之变回 “旧信息”
        //故若可修改信息字段中有 “;” 出现时，说明该信息未通过审核
        if (project != null) {
            memberAuthentication.setProject(memberAuthentication.getProject()+";"+project+";");
        }
        if (type != null) {
            memberAuthentication.setType(memberAuthentication.getType()+";"+type+";");
        }
        if (level != null) {
            memberAuthentication.setLevel(memberAuthentication.getLevel()+";"+level+";");
        }
        if (club != null) {
            memberAuthentication.setClub(club);//所在俱乐部名称
        }
        if (clubType != null) {
            memberAuthentication.setClubType(clubType);//所属俱乐部类型，省级/市级/...

        }

        if (enrollApplication != null) {
            String enrollApplicationFileName = enrollApplication.getOriginalFilename();
            String enrollApplicationPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                    + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "运动员注册申请表" + File.separatorChar + enrollApplicationFileName;
            try {
                Path enrollApplicationPathFile = FileUtils.forceCreateFile(enrollApplicationPath);
                enrollApplication.transferTo(enrollApplicationPathFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            memberAuthentication.setEnrollApplication(enrollApplicationPath);
        }

        if (qualificationProtocol != null) {
            String qualificationProtocolFileName = qualificationProtocol.getOriginalFilename();
            String qualificationProtocolPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                    + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "资格协议" + File.separatorChar + qualificationProtocolFileName;
            try {
                Path qualificationProtocolPathFile = FileUtils.forceCreateFile(qualificationProtocolPath);
                qualificationProtocol.transferTo(qualificationProtocolPathFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            memberAuthentication.setQualificationProtocol(qualificationProtocolPath);
        }

        if (agreement != null) {
            String agreementFileName = agreement.getOriginalFilename();
            String agreementPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                    + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "聘用合同" + File.separatorChar + agreementFileName;
            try {
                Path agreementPathFile = FileUtils.forceCreateFile(agreementPath);
                agreement.transferTo(agreementPathFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            memberAuthentication.setAgreement(agreementPath);
        }

        if (idcard != null) {
            String idcardFileName = idcard.getOriginalFilename();
            String idcardPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                    + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "身份证" + File.separatorChar + idcardFileName;
            try {
                Path idcardPathFile = FileUtils.forceCreateFile(idcardPath);
                idcard.transferTo(idcardPathFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            memberAuthentication.setIdcard(idcardPath);

        }

        if (photo != null) {
            String photoFileName = photo.getOriginalFilename();
            String photoPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                    + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "证件照" + File.separatorChar + photoFileName;
            try {
                Path photoPathFile = FileUtils.forceCreateFile(photoPath);
                photo.transferTo(photoPathFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            memberAuthentication.setPhoto(photoPath);
        }

        if (extraEvidence != null) {
            String extraEvidenceFileName = extraEvidence.getOriginalFilename();
            String extraEvidencePath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                    + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "证明材料" + File.separatorChar + extraEvidenceFileName;

            try {
                Path extraEvidencePathFile = FileUtils.forceCreateFile(extraEvidencePath);
                extraEvidence.transferTo(extraEvidencePathFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            memberAuthentication.setExtraEvidence(extraEvidencePath);
        }

        if (otherFileZip != null) {
            String otherFileZipName = otherFileZip.getOriginalFilename();
            String otherFileZipPath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "认证材料"
                    + File.separatorChar + club + File.separatorChar + name + File.separatorChar + "其他材料" + File.separatorChar + otherFileZipName;
            try {
                Path otherFileZipFile = FileUtils.forceCreateFile(otherFileZipPath);
                otherFileZip.transferTo(otherFileZipFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            memberAuthentication.setOtherFileZip(otherFileZipPath);
        }


        memberAuthentication.setUpdateTime(System.currentTimeMillis());

        memberAuthenticationDao.update(memberAuthentication);


        result.setStatus(HttpStatus.OK.value());
        result.setMsg("修改成功！");
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @Override
    public ResultBean<?> submitPostalApplication(Long memberId, String receiverName, String phoneNumber, String address, Integer cost,
                                                 Boolean needReceipt, MultipartFile evidence, String cause) throws IOException {
        ResultBean<Void> result = new ResultBean<>();
        MemberAuthentication memberAuthentication = memberAuthenticationDao.selectById(memberId);
        if (memberAuthentication == null) {
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("此成员不存在");
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }

        //TODO 判断一下不要重复申请 未写
        // 注意有初次申请和补办的情况，cause不为空的是补办，为空的是初次申请
        // 审核时拒绝邮寄的sendInformation会被删除并予以邮箱告知
        // 所以只存在【多条补办已邮寄】和【单条初申】，【单条补办未邮寄】
        // 所以可以通过cause和state来判断是不是重复申请


        SendInformation sendInformation = new SendInformation();
        sendInformation.setMemberId(memberId);
        sendInformation.setReceiverName(receiverName);
        sendInformation.setPhoneNumber(phoneNumber);
        sendInformation.setAddress(address);
        sendInformation.setCost(cost);
        sendInformation.setNeedReceipt(needReceipt);
        sendInformation.setCause(cause);
        sendInformation.setState(false);

        String evidenceFileName = evidence.getOriginalFilename();
        String evidencePath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "邮寄申请"
                + File.separatorChar + memberAuthentication.getClub() + File.separatorChar + memberAuthentication.getName() + File.separatorChar + "邮寄支付凭证" + File.separatorChar + evidenceFileName;
        Path evidencePathFile = FileUtils.forceCreateFile(evidencePath);
        evidence.transferTo(evidencePathFile);

        sendInformation.setEvidence(evidencePath);
        sendInformationDao.insert(sendInformation);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("申请邮寄成功");
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @Override
    public ResultBean<?> getPostalApplication(Long userId) {
        ResultBean<List<SendInformation>> result = new ResultBean<>();
        SystemUser user = systemUserDao.selectById(userId);
        List<SendInformation> sendInformations = sendInformationDao.selectByClub(user.getUsername());
        for (SendInformation sendInformation : sendInformations) {
            sendInformation.setEvidence(FileUtils.hidePath(sendInformation.getEvidence()));
        }

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看邮寄成功");
        result.setData(sendInformations);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @Override
    public ResultBean<Void> submitAnnualAuthentication(String club, MultipartFile file) {
        ResultBean<Void> result = new ResultBean<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH) + 1;
        //TODO 时间
        if (month != 12) {
            result.setData(null);
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("未到年审时间，不可提交!");
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }
        AnnualInfo annualInfo = annualInfoDao.selectByClubAndYear(club, calendar.get(Calendar.YEAR));

        String fileName = file.getOriginalFilename();
        String filePath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "年审材料"
                + File.separatorChar + club + File.separatorChar + fileName;
        try {
            Path path = FileUtils.forceCreateFile(filePath);
            file.transferTo(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //一个俱乐部每年只有一个
        annualInfo.setClub(club);
        annualInfo.setState(-1);
        annualInfo.setFullPath(filePath);
        annualInfo.setUpdateTime(System.currentTimeMillis());
        annualInfoDao.update(annualInfo);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("提交成功！");
        result.setTimestamp(System.currentTimeMillis());
        result.setData(null);
        return result;
    }

    @Override
    public ResultBean<AnnualAuthenticationResultBeans> getAnnualAuthenticationResult(String club, Integer pageNum, Integer pageSize) throws UnhandledException {
        ResultBean<AnnualAuthenticationResultBeans> result = new ResultBean<>();
        AnnualAuthenticationResultBeans annualAuthenticationResultBeans = new AnnualAuthenticationResultBeans();
        List<AnnualAuthenticationResultBean> authenticationResultBeans = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<AnnualInfo> annualInfos = annualInfoDao.selectByClub(club);
        PageInfo<AnnualInfo> pageInfo = new PageInfo<>(annualInfos);
        if (annualInfos == null) {
            annualInfos = new ArrayList<>();
        }
        for (AnnualInfo annualInfo : annualInfos) {
            AnnualAuthenticationResultBean annualAuthenticationResultBean = new AnnualAuthenticationResultBean();
            annualAuthenticationResultBean.setClub(club);
            if (annualInfo.getState() == 1) {
                annualAuthenticationResultBean.setResult("通过");
            } else if (annualInfo.getState() == 0) {
                annualAuthenticationResultBean.setResult("未通过，审核结果已发至您的邮箱，请登录邮箱查看详情!");
            } else {
                annualAuthenticationResultBean.setResult("未审核");
            }
            annualAuthenticationResultBean.setYear(annualInfo.getYearNumber());
            authenticationResultBeans.add(annualAuthenticationResultBean);
        }

        annualAuthenticationResultBeans.setAnnualAuthenticationResultBeans(authenticationResultBeans);
        annualAuthenticationResultBeans.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        annualAuthenticationResultBeans.setSize(pageInfo.getPageSize());
        annualAuthenticationResultBeans.setPage(pageInfo.getPages());

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("查看成功！");
        result.setTimestamp(System.currentTimeMillis());
        result.setData(annualAuthenticationResultBeans);
        return result;
    }

    @Override
    public ResultBean<Void> updateAnnualAuthenticationApplication(Long memberId, String project, String type, String level) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();
        MemberAuthentication member = memberAuthenticationDao.selectById(memberId);
        if (member == null) {
            result.setStatus(HttpStatus.OK.value());
            result.setMsg("无此成员信息！");
            result.setTimestamp(System.currentTimeMillis());
            result.setData(null);
            return result;
        }
        //若修改了，member里的对应信息变成 “旧信息;新信息;” 的格式，审核通过后变回 “新信息” 的格式，反之变回 “旧信息”
        //故若可修改信息字段中有 “;” 出现时，说明该信息未通过审核
        if (project != null) {
            member.setProject(member.getProject()+";"+project+";");
        }
        if (type != null) {
            member.setType(member.getType()+";"+type+";");
        }
        if (level != null) {
            member.setLevel(member.getLevel()+";"+level+";");
        }
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("修改成功！");
        result.setTimestamp(System.currentTimeMillis());
        result.setData(null);

        return result;
    }


}
