package com.stackfarm.esports.controller.authorize;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.authorize.CertificateService;
import com.stackfarm.esports.system.UserRolesConstant;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author croton
 * @create 2021/10/17 19:49
 */
@RestController
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("/authorize/certificate/create")
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.ADMIN}, logical = Logical.OR)
    public ResultBean<?> createCertificate(@RequestParam("memberId") Long memberId) throws UnhandledException {
        return certificateService.createCertificate(memberId);
    }
}
