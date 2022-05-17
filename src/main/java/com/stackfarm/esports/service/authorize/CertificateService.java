package com.stackfarm.esports.service.authorize;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;

/**
 * @author croton
 * @create 2021/10/17 19:02
 */
public interface CertificateService {

    /**
     * 生成证书信息
     * @param memberId
     * @return
     */
    ResultBean<?> createCertificate(Long memberId) throws UnhandledException;

}
