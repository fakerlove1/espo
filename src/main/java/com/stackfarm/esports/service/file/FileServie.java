package com.stackfarm.esports.service.file;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author croton
 * @create 2021/4/6 10:26
 */
public interface FileServie {

    ResultBean<?> download(HttpServletRequest request, HttpServletResponse response, String uuid) throws UnhandledException;
    ResultBean<?> downloadZip(HttpServletRequest request, HttpServletResponse response, String uuid) throws Exception;
    ResultBean<?> downLoadTemplate(HttpServletRequest request, HttpServletResponse response, Integer flag) throws UnhandledException;
    ResultBean<?> upLoadTemplate(Integer flag, MultipartFile template) throws UnhandledException;
}
