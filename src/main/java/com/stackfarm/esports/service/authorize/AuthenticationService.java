package com.stackfarm.esports.service.authorize;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author xiaohuang
 * @create 2021/9/9 10:36
 */
@Service
public interface AuthenticationService {

    /**
     * 提交认证申请
     * @return
     */
    ResultBean<?> submitCertification(String name, String sex, String birth, String project,
                                      String type, String level, String club, String clubType,
                                      String origin, String idcardNumber,
                                      MultipartFile enrollApplication, MultipartFile qualificationProtocol,
                                      MultipartFile agreement, MultipartFile idcard, MultipartFile photo, MultipartFile extraEvidence, MultipartFile otherFileZip, String cardType) throws UnhandledException;
    
    /**
     * 查看申请列表
     * @return
     */
    ResultBean<?> getApplication(String club, Integer pageNum, Integer pageSize);


    /**
     * 查看认证列表
     * @return
     */
    ResultBean<?> getAuthenticationList(String club, String type/*, Integer pageNum, Integer pageSize*/);

    /**
     * 注销申请
     * @return
     */
    ResultBean<?> logoutApplication(Long memberId, String cause, MultipartFile[] material) throws IOException;

    /**
     * 查看团体内某一认证详情
     * @return
     */
    ResultBean<?> getOneAuthentication(Long memberId);

    /**
     * 修改团体内某一认证详情
     * @return
     */
    ResultBean<?> updateAuthentication(Long memberId, /*String name, String sex, String birth,*/ String project,
                                       String type, String level, String club, String clubType,
                                       String origin, String idcardNumber,
                                       MultipartFile enrollApplication, MultipartFile qualificationProtocol,
                                       MultipartFile agreement, MultipartFile idcard, MultipartFile photo,
                                       MultipartFile extraEvidence, MultipartFile otherFileZip) throws UnhandledException;


    /**
     * 提交纸质证书邮寄申请
     * @return
     */
    ResultBean<?> submitPostalApplication(Long memberId, String receiverName, String phoneNumber, String address, Integer cost,
                                          Boolean needReceipt, MultipartFile evidence, String cause) throws IOException;

    /**
     * 查看邮寄申请
     * @return
     */
    ResultBean<?> getPostalApplication(Long id);

    /**
     * 提交年审材料
     * @param club
     * @param file
     * @return
     */
    ResultBean<?> submitAnnualAuthentication(String club, MultipartFile file);

    /**
     * 查看年审结果
     * @param club
     * @return
     */
    ResultBean<?> getAnnualAuthenticationResult(String club, Integer pageNum, Integer pageSize) throws UnhandledException;

    /**
     * 年审信息更新
     * @param memberId
     * @param project
     * @param type
     * @param level
     * @return
     * @throws UnhandledException
     */
    ResultBean<?> updateAnnualAuthenticationApplication(Long memberId, String project, String type, String level) throws UnhandledException;
}
