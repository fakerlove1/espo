package com.stackfarm.esports.service.authorize;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author croton
 * @create 2021/9/5 13:08
 */
public interface MemberManageService {
    /**
     * 查看申请列表
     * @return
     */
    ResultBean<?> getApplications(Integer pageNum, Integer pageSize);

    /**
     * 审核初次认证申请
     * @param applicationId
     * @param result
     * @param cause
     * @return
     */
    ResultBean<?> checkAuthenticationApplication(Long applicationId, Boolean result, String cause) throws TencentCloudSDKException;

    /**
     * 审核注销申请
     * @param applicationId
     * @param result
     * @param cause
     * @return
     */
    ResultBean<?> checkLogoutApplication(Long applicationId, Boolean result, String cause);

    /**
     * 查看已认证列表
     * @return
     */
    ResultBean<?> getAuthorised(Integer pageNum, Integer pageSize);

    /**
     * 查看已认证列表
     * @return
     */
    ResultBean<?> getAnnual(Integer pageNum, Integer pageSize);

    /**
     * 根据成员认证id查看认证信息
     * @param memberId
     * @return
     */
    ResultBean<?> getAuthenticationDetail(Long memberId);

    /**
     * 年审
     * @param club
     * @param result
     * @param cause
     * @return
     */
    ResultBean<?> annual(String club, Boolean result, String cause) throws UnhandledException, TencentCloudSDKException;

    /**
     * 查看年审列表
     * @return
     */
    ResultBean<?> getAnnualList(Integer pageNum, Integer pageSize) throws UnhandledException;

    /**
     * 下载年审材料
     * @return
     */
    ResultBean<?> downloadAnnualZip(HttpServletRequest request, HttpServletResponse response, String uuid) throws Exception;


    /**
     * 查看邮寄申请列表
     * @return
     */
    ResultBean<?> getPostApplications(Integer pageNum, Integer pageSize);

    /**
     * 处理邮寄单
     * @param postId
     * @param state
     * @return
     */
    ResultBean<?> post(Long postId, Boolean state, String cause, String trackingNumber) throws TencentCloudSDKException;

    /**
     * 添加裁判员
     * @param name
     * @param sex
     * @param birth
     * @param idcard
     * @param photo
     * @return
     */
    ResultBean<?> addJudge(String name, String sex, String birth, String idcard, String photo);

    /**
     * 注销裁判员
     * @param memberId
     * @return
     */
    ResultBean<?> deleteJudge(Long memberId);


    /**
     * 查看全部年审记录
     * @param clubName
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResultBean<?> getAllAnnualList(String clubName, Integer pageNum, Integer pageSize);
}
