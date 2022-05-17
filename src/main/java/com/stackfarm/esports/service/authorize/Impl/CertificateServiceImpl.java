package com.stackfarm.esports.service.authorize.Impl;

import com.stackfarm.esports.dao.authentication.MemberAuthenticationDao;
import com.stackfarm.esports.dao.authentication.ProjectDao;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.authorize.MemberAuthentication;
import com.stackfarm.esports.pojo.authorize.Project;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.result.authorize.CertificateBean;
import com.stackfarm.esports.service.authorize.CertificateService;
import com.stackfarm.esports.system.AuthenticationStateConstant;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.FileUtils;
import com.stackfarm.esports.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author croton
 * @create 2021/10/17 19:02
 */
@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private MemberAuthenticationDao memberAuthenticationDao;
    @Autowired
    private ProjectDao projectDao;

    /**
     * 生成证书信息
     * @param memberId
     * @return
     */
    @Override
    public ResultBean<CertificateBean> createCertificate(Long memberId) throws UnhandledException {

        ResultBean<CertificateBean> result = new ResultBean<>();
        CertificateBean certificateBean = new CertificateBean();
        MemberAuthentication  memberAuthentication = memberAuthenticationDao.selectById(memberId);
        if (!AuthenticationStateConstant.AUTHORIZED.equals(memberAuthentication.getState())) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("未认证不可生成证书！");
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            throw unhandledException;
        }


        Project project = projectDao.selectByName(memberAuthentication.getProject());

        if (project == null) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("无此项目！");
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            throw unhandledException;
        }
        certificateBean.setMemberName(memberAuthentication.getName());
        certificateBean.setMemberSex(memberAuthentication.getSex());
        certificateBean.setMemberBirth(memberAuthentication.getBirth());
        certificateBean.setMemberPlayId(memberAuthentication.getId());
        certificateBean.setProject(memberAuthentication.getProject());
        certificateBean.setMemberType(memberAuthentication.getType());
        certificateBean.setLevel(memberAuthentication.getLevel());
        certificateBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        certificateBean.setMemberOrigin(memberAuthentication.getOrigin());
        certificateBean.setMemberIdCard(memberAuthentication.getIdCardNumber());
        String level = memberAuthentication.getLevel();
        if (level.contains(";")) {
            level = BaseUtils.getListFromString(level).get(1);
        }
        certificateBean.setNumber(NumberUtils.getNumber(Integer.parseInt(project.getId().toString()), memberAuthentication.getType(), Integer.parseInt(level), new Date(memberAuthentication.getCheckTime()).getYear(),Integer.parseInt(memberAuthentication.getNumber())));
        certificateBean.setPhoto(FileUtils.hidePath(memberAuthentication.getPhoto()));

        result.setData(certificateBean);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("生成成功！");
        result.setTimestamp(System.currentTimeMillis());

        return result;
    }
}
