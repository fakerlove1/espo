package com.stackfarm.esports.controller.authorize;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.authorize.MemberManageService;
import com.stackfarm.esports.system.UserRolesConstant;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author croton
 * @create 2021/9/5 13:08
 */
@RestController
public class MemberManageController {

    @Autowired
    private MemberManageService memberManageService;

    /**
     * 查看申请列表
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @GetMapping("/authorize/admin/getApplications/{pageNum}/{pageSize}")
    public ResultBean<?> getApplications(@PathVariable("pageNum")Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return memberManageService.getApplications(pageNum, pageSize);
    }

    /**
     * 审核初次认证申请
     * @param applicationId
     * @param result
     * @param cause
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/authorize/admin/checkAuthenticationApplication")
    public ResultBean<?> checkAuthenticationApplication(@RequestParam("applicationId") Long applicationId,
                                                        @RequestParam("result") Boolean result,
                                                        @RequestParam(value = "cause", required = false) String cause) throws TencentCloudSDKException {
        return memberManageService.checkAuthenticationApplication(applicationId, result, cause);
    }

    /**
     * 审核注销申请
     * @param applicationId
     * @param result
     * @param cause
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/authorize/admin/checkLogoutApplication")
    public ResultBean<?> checkLogoutApplication(@RequestParam("applicationId") Long applicationId,
                                                @RequestParam("result") Boolean result,
                                                @RequestParam(value = "cause", required = false) String cause) {
        return memberManageService.checkLogoutApplication(applicationId, result, cause);
    }

//    /**
//     * 查看年审列表
//     * @return
//     */
//    @RequiresRoles(UserRolesConstant.ADMIN)
//    @GetMapping("/authorize/admin/getAnnual/{pageNum}/{pageSize}")
//    public ResultBean<?> getAnnual(@PathVariable("pageNum")Integer pageNum,
//                                       @PathVariable("pageSize") Integer pageSize) {
//        return memberManageService.getAnnual(pageNum, pageSize);
//    }

    /**
     * 查看已认证列表
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @GetMapping("/authorize/admin/getAuthorised/{pageNum}/{pageSize}")
    public ResultBean<?> getAuthorised(@PathVariable("pageNum")Integer pageNum,
                                       @PathVariable("pageSize") Integer pageSize) {
        return memberManageService.getAuthorised(pageNum, pageSize);
    }

    /**
     * 根据成员认证id查看认证信息
     * @param memberId
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/authorize/admin/getAuthenticationDetail")
    public ResultBean<?> getAuthenticationDetail(@RequestParam("memberId") Long memberId) {
        return memberManageService.getAuthenticationDetail(memberId);
    }

    /**
     * 年审
     * @param club
     * @param result
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/authorize/admin/annual")
    public ResultBean<?> annual(@RequestParam("club") String club,
                                @RequestParam("result") Boolean result,
                                @RequestParam(value = "cause", required = false) String cause) throws TencentCloudSDKException, UnhandledException {
        return memberManageService.annual(club, result, cause);
    }

    /**
     * 查看年审列表
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @GetMapping("/authorize/admin/getAnnualList/{pageNum}/{pageSize}")
    public ResultBean<?> getAnnualList(@PathVariable("pageNum") Integer pageNum,
                                       @PathVariable("pageSize") Integer pageSize) throws UnhandledException {
        return memberManageService.getAnnualList(pageNum, pageSize);
    }

    /**
     * 下载年审材料
     * @return
     */
//    @RequiresRoles(UserRolesConstant.ADMIN)
    @GetMapping("/file/authorize/admin/downloadAnnualZip/{uuid}")
    public ResultBean<?> downloadAnnualZip(HttpServletRequest request, HttpServletResponse response, @PathVariable("uuid") String uuid) throws Exception {
        return memberManageService.downloadAnnualZip(request, response, uuid);
    }



    /**
     * 查看邮寄申请列表
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @GetMapping("/authorize/admin/getPostApplications/{pageNum}/{pageSize}")
    public ResultBean<?> getPostApplications(@PathVariable("pageNum")Integer pageNum,
                                             @PathVariable("pageSize") Integer pageSize) {
        return memberManageService.getPostApplications(pageNum, pageSize);
    }

    /**
     * 处理邮寄单
     * @param postId
     * @param state
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/authorize/admin/post")
    public ResultBean<?> post(@RequestParam("postId") Long postId,
                              @RequestParam("state") Boolean state,
                              @RequestParam(value = "cause", required = false) String cause,
                              @RequestParam(value = "trackingNumber", required = false) String trackingNumber) throws TencentCloudSDKException {
        return memberManageService.post(postId, state, cause, trackingNumber);
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
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/authorize/admin/addJudge")
    public ResultBean<?> addJudge(@RequestParam("name") String name,
                                  @RequestParam("sex") String sex,
                                  @RequestParam("birth") String birth,
                                  @RequestParam("idcard") String idcard,
                                  @RequestParam("photo") String photo) {
        return memberManageService.addJudge(name, sex, birth, idcard, photo);
    }

    /**
     * 注销裁判员
     * @param memberId
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @PostMapping("/authorize/admin/deleteJudge")
    public ResultBean<?> deleteJudge(@RequestParam("memberId") Long memberId) {
        return memberManageService.deleteJudge(memberId);
    }

    /**
     * 根据俱乐部名称查看全部年审记录
     * @param clubName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequiresRoles(UserRolesConstant.ADMIN)
    @GetMapping("/authorize/admin/getAllAnnualList/{clubName}/{pageNum}/{pageSize}")
    public ResultBean<?> getAllAnnualList(@PathVariable("clubName") String clubName,
                                          @PathVariable("pageNum") Integer pageNum,
                                          @PathVariable("pageSize") Integer pageSize) {
        return memberManageService.getAllAnnualList(clubName, pageNum, pageSize);
    }





}
