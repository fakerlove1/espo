package com.stackfarm.esports.controller.file;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.file.FileServie;
import com.stackfarm.esports.system.UserRolesConstant;
import com.stackfarm.esports.utils.BaseUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author croton
 * @create 2021/4/6 10:22
 */
@RestController
public class FileController {

    @Autowired
    private FileServie fileServie;


    /**
     * 单文件下载
     * @param request
     * @param response
     * @param uuid
     * @return
     * @throws UnhandledException
     */
    @GetMapping("/file/download/{uuid}")
    public ResultBean<?> download(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable("uuid") String uuid) throws Exception {


        if (uuid.contains("_")) {
            return fileServie.downloadZip(request, response, uuid);
        }
        return fileServie.download(request, response, uuid);

    }

    /**
     * 多文件打包下载
     * @param request
     * @param response
     * @param uuid
     * @return
     * @throws UnhandledException
     */
    @GetMapping("/file/download/zip/{uuid}")
    public ResultBean<?> downloadZip(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable("uuid") String uuid) throws Exception {
        return fileServie.downloadZip(request, response, uuid);
    }

    /**
     * 轮播
     * @param uuid
     * @return
     */
    @GetMapping("/file/download/round/{uuid}")
    public ResultBean<?> downloadZip(@PathVariable("uuid") String uuid) {
        ResultBean<List<String>> result = new ResultBean<>();
        List<String> uuidList = BaseUtils.getFileListFromString(uuid);
        result.setStatus(HttpStatus.OK.value());
        result.setMsg("success！");
        result.setData(uuidList);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 下载模板
     * @param flag
     * @return
     */
    //@RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.ADMIN}, logical = Logical.OR)
    @GetMapping("/authorize/template/download/{flag}")
    public ResultBean<?> downLoadTemplate(HttpServletRequest request, HttpServletResponse response, @PathVariable("flag") Integer flag) throws UnhandledException {
        return fileServie.downLoadTemplate(request, response, flag);
    }

    /**
     * 上传模板
     * @param flag
     * @return
     */
    @RequiresRoles(value = {UserRolesConstant.ENTERPRISE_USER, UserRolesConstant.CLUB_USER, UserRolesConstant.ADMIN}, logical = Logical.OR)
    @PostMapping("/authorize/template/upload")
    public ResultBean<?> upLoadTemplate(@RequestParam("flag") Integer flag, @RequestParam("template") MultipartFile template) throws UnhandledException {
        return fileServie.upLoadTemplate(flag, template);
    }
}
