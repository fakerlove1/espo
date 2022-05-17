package com.stackfarm.esports.controller.authorize;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.authorize.AuthenticationService;
import com.stackfarm.esports.system.UserRolesConstant;
import com.stackfarm.esports.utils.JwtUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author croton
 * @create 2021/9/13 19:10
 */
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * 提交认证申请
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PostMapping("/authorize/organization/submitCertification")
    public ResultBean<?> submitCertification(@RequestParam("name") String name,
                                             @RequestParam("sex") String sex,
                                             @RequestParam("birth") String birth,
                                             @RequestParam("project") String project,
                                             @RequestParam("type") String type,
                                             @RequestParam("level") Integer level,
                                             @RequestParam("club") String club,
                                             @RequestParam(value = "clubType", required = false) String clubType,
                                             @RequestParam("origin") String origin,
                                             @RequestParam("idcardNumber") String idcardNumber,
                                             @RequestParam("enrollApplication") MultipartFile enrollApplication,
                                             @RequestParam("qualificationProtocol") MultipartFile qualificationProtocol,
                                             @RequestParam("agreement") MultipartFile agreement,
                                             @RequestParam("idcard") MultipartFile idcard,
                                             @RequestParam("photo") MultipartFile photo,
                                             @RequestParam(value = "extraEvidence", required = false) MultipartFile extraEvidence,
                                             @RequestParam("otherFileZip") MultipartFile otherFileZip,
                                             @RequestParam("cardType") String cardType) throws UnhandledException {


        return authenticationService.submitCertification(name, sex, birth, project, type, level.toString(), club, clubType, origin, idcardNumber, enrollApplication, qualificationProtocol, agreement, idcard, photo, extraEvidence, otherFileZip, cardType);
    }

    /**
     * 查看申请列表
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @GetMapping("/authorize/organization/getApplication/{pageNum}/{pageSize}")
    public ResultBean<?> getApplication(HttpServletRequest request,
                                        @PathVariable("pageNum") Integer pageNum,
                                        @PathVariable("pageSize") Integer pageSize) throws UnhandledException {

        return authenticationService.getApplication(JwtUtils.getUserName(request.getHeader("token")), pageNum, pageSize);
    }


    /**
     * 查看认证列表
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PostMapping("/authorize/organization/getAuthenticationList")
    public ResultBean<?> getAuthenticationList(HttpServletRequest request, @RequestParam(value = "type", required = false) String type
                                               /*@PathVariable("pageNum") Integer pageNum,
                                               @PathVariable("pageSize") Integer pageSize*/) throws UnhandledException {
        return authenticationService.getAuthenticationList(JwtUtils.getUserName(request.getHeader("token")), type/*, pageNum, pageSize*/);
    }

    /**
     * 注销申请
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PostMapping("/authorize/organization/logoutApplication")
    public ResultBean<?> logoutApplication(@RequestParam("memberId") Long memberId,
                                           @RequestParam("cause") String cause,
                                           @RequestParam("material") MultipartFile[] material) throws IOException {
        return authenticationService.logoutApplication(memberId, cause, material);
    }

    /**
     * 查看团体内某一认证详情
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PostMapping("/authorize/organization/getOneAuthentication")
    public ResultBean<?> getOneAuthentication(@RequestParam("memberId") Long memberId) {
        return authenticationService.getOneAuthentication(memberId);
    }

    /**
     * 修改团体内某一认证详情
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PostMapping("/authorize/organization/updateAuthentication")
    public ResultBean<?> updateAuthentication(@RequestParam("memberId") Long memberId,
                                            /* String name, String sex, String birth,*/
                                              @RequestParam(value = "project", required = false) String project,
                                              @RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "level", required = false) Integer level,
                                              @RequestParam(value = "club", required = false) String club,
                                              @RequestParam(value = "clubType", required = false) String clubType,
                                              @RequestParam(value = "origin", required = false) String origin,
                                              @RequestParam(value = "idcardNumber", required = false) String idcardNumber,
                                              @RequestParam(value = "enrollApplication", required = false) MultipartFile enrollApplication,
                                              @RequestParam(value = "qualificationProtocol", required = false) MultipartFile qualificationProtocol,
                                              @RequestParam(value = "agreement", required = false) MultipartFile agreement,
                                              @RequestParam(value = "idcard", required = false) MultipartFile idcard,
                                              @RequestParam(value = "photo", required = false) MultipartFile photo,
                                              @RequestParam(value = "extraEvidence", required = false) MultipartFile extraEvidence,
                                              @RequestParam(value = "otherFileZip", required = false) MultipartFile otherFileZip) throws UnhandledException {
        return authenticationService.updateAuthentication(memberId, project, type, level.toString(), club, clubType, origin, idcardNumber, enrollApplication, qualificationProtocol, agreement, idcard, photo, extraEvidence, otherFileZip);

    }


    /**
     * 提交纸质证书邮寄申请
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PostMapping("/authorize/organization/submitPostalApplication")
    public ResultBean<?> submitPostalApplication(@RequestParam("memberId") Long memberId,
                                                 @RequestParam("receiverName") String receiverName,
                                                 @RequestParam("phoneNumber") String phoneNumber,
                                                 @RequestParam("address") String address,
                                                 @RequestParam("cost") Integer cost,
                                                 @RequestParam("needReceipt") Boolean needReceipt,
                                                 @RequestParam("evidence") MultipartFile evidence,
                                                 @RequestParam(value = "cause", required = false) String cause) throws IOException {
        return authenticationService.submitPostalApplication(memberId, receiverName, phoneNumber, address, cost, needReceipt, evidence, cause);
    }

    /**
     * 查看邮寄申请
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @GetMapping("/authorize/organization/getPostalApplication")
    public ResultBean<?> getPostalApplication(HttpServletRequest request) throws UnhandledException {
        return authenticationService.getPostalApplication(JwtUtils.getUserId(request.getHeader("token")));
    }

    /**
     * 提交年审材料
     * @param request
     * @param file
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PostMapping("/authorize/organization/submitAnnualAuthentication")
    public ResultBean<?> submitAnnualAuthentication(HttpServletRequest request,
                                                    @RequestParam("file") MultipartFile file) throws UnhandledException {
        return authenticationService.submitAnnualAuthentication(JwtUtils.getUserName(request.getHeader("token")), file);
    }

    /**
     * 查看年审结果
     * @param request
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @GetMapping("/authorize/organization/getAnnualAuthenticationResult/{pageNum}/{pageSize}")
    public ResultBean<?> getAnnualAuthenticationResult(HttpServletRequest request,
                                                       @PathVariable("pageNum") Integer pageNum,
                                                       @PathVariable("pageSize") Integer pageSize) throws UnhandledException {
        return authenticationService.getAnnualAuthenticationResult(JwtUtils.getUserName(request.getHeader("token")), pageNum, pageSize);
    }

    /**
     * 年审信息更新
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER}, logical = Logical.OR)
    @PostMapping("/authorize/organization/updateAnnualAuthenticationApplication")
    public ResultBean<?> updateAnnualAuthenticationApplication(@RequestParam("memberId") Long memberId,
                                              @RequestParam(value = "project", required = false) String project,
                                              @RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "level", required = false) Integer level) throws UnhandledException {
        return null;
    }
}
