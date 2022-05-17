package com.stackfarm.esports.service.file.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackfarm.esports.dao.activity.ActivityDao;
import com.stackfarm.esports.dao.activity.ActivityExtensionDao;
import com.stackfarm.esports.dao.authentication.TemplateDao;
import com.stackfarm.esports.dao.user.SystemUserDao;
import com.stackfarm.esports.dao.user.UserExtensionOrganizationDao;
import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.pojo.activity.ActivityExtension;
import com.stackfarm.esports.pojo.authorize.Template;
import com.stackfarm.esports.pojo.user.SystemUser;
import com.stackfarm.esports.pojo.user.UserExtensionOrganization;
import com.stackfarm.esports.result.ResultBean;
import com.stackfarm.esports.service.file.FileServie;
import com.stackfarm.esports.system.SystemConstant;
import com.stackfarm.esports.utils.BaseUtils;
import com.stackfarm.esports.utils.FileUtils;
import com.stackfarm.esports.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipOutputStream;

import static com.stackfarm.esports.utils.FileUtils.zipFile;

/**
 * @author croton
 * @create 2021/4/6 10:26
 */
@Service
@Transactional
public class FileServiceImpl implements FileServie {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActivityExtensionDao activityExtensionDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private SystemUserDao systemUserDao;
    @Autowired
    private TemplateDao templateDao;

    /**
     * 单文件直接下载
     * @param request
     * @param response
     * @param uuid
     * @return
     * @throws UnhandledException
     */
    @Override
    public ResultBean<Void> download(HttpServletRequest request, HttpServletResponse response, String uuid) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();

        Map<String, String> hashMap = parseUuid(uuid);
        String fileFullPath = hashMap.get("fileFullPath");

        Path file = Paths.get(fileFullPath);
        String fileName = file.getFileName().toString();
        if (!Files.exists(file)) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "所请求的文件不存在",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
        }
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream inputStream = Files.newInputStream(file);
            response.setContentLengthLong(Files.size(file));
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
                byte[] bytesName = fileName.getBytes("UTF-8");
                fileName = new String(bytesName, "ISO-8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            } else {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20"));
            }
            response.setStatus(HttpStatus.OK.value());
            byte[] bytes = new byte[1024 * 4];
            int readableBytes;
            while ((readableBytes = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, readableBytes);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "写入文件失败",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("下载成功！");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 多文件打包下载
     * @param request
     * @param response
     * @param uuid
     * @return
     * @throws Exception
     */
    @Override
    public ResultBean<Void> downloadZip(HttpServletRequest request, HttpServletResponse response, String uuid) throws Exception {
        ResultBean<Void> result = new ResultBean<>();
        List<String> uuids = BaseUtils.getFileListFromString(uuid);
        List<File> files = new ArrayList<>();
        String groupName = "";
        StringBuilder sb = new StringBuilder();
        //如果是“123_456”格式，说明只下载组图
        if ('_' != uuid.charAt(uuid.length()-1)) {
            for (String u : uuids) {
                if (u != null & !"".equals(u)) {
                    Map<String, String> hashMap = parseUuid(u);
                    String fileFullPath = hashMap.get("fileFullPath");
                    sb.append(fileFullPath + ";");
                    files.add(new File(fileFullPath));
                }
            }
            ActivityExtension activityExtension = activityExtensionDao.selectByPicture(sb.toString());
            if (activityExtension != null) {
                Long actId = activityExtension.getActId();
                SystemUser user = systemUserDao.selectById(activityDao.selectByActId(actId).getLauncherId());
                groupName = user.getUsername();
            } else {
                throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "活动信息不存在",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
            }
            if(groupName == null || "".equals(groupName)){
                groupName = "未知名称";
            }
            downLoadFiles(files, response, request, groupName + " 活动组图");
        } else if ('_' == uuid.charAt(0)) {
            //认证审核材料
            for (String u : uuids) {
                if (u != null & !"".equals(u)) {
                    Map<String, String> hashMap = parseUuid(u);
                    String fileFullPath = hashMap.get("fileFullPath");
                    sb.append(fileFullPath + ";");
                    files.add(new File(fileFullPath));
                }
            }
            downLoadFiles(files, response, request, "申请审核材料");
        } else {
            for (String u : uuids) {
                if (u != null & !"".equals(u)) {
                    Map<String, String> hashMap = parseUuid(u);
                    String fileFullPath = hashMap.get("fileFullPath");
                    files.add(new File(fileFullPath));
                    if (sb.length() == 0) {
                        sb.append(fileFullPath);
                    }
                }
            }
            ActivityExtension activityExtension = activityExtensionDao.selectByPoster(sb.toString());
            if (activityExtension != null) {
                Long actId = activityExtension.getActId();
                SystemUser user = systemUserDao.selectById(activityDao.selectByActId(actId).getLauncherId());
                groupName = user.getUsername();
            } else {
                throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "活动信息不存在",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
            }
            if(groupName == null || "".equals(groupName)){
                groupName = "未知名称";
            }
            downLoadFiles(files, response, request, groupName + " 审核材料");
        }

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("下载成功！");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 下载模板
     *
     * 1-江苏省电子竞技运动员注册申请表
     *
     * 2-江苏省电子竞技运动协会注册运动员代表资格协议
     *
     * 3-江苏省电子竞技运动员注册信息列表
     *
     * 4-江苏省电子竞技运动员注销申请表
     * @param flag
     * @return
     */
    @Override
    public ResultBean<?> downLoadTemplate(HttpServletRequest request, HttpServletResponse response, Integer flag) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();
        Template template = templateDao.selectById(flag);
        if (template == null) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "模板信息不存在",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }


        Path file = Paths.get(template.getFullPath());
        String fileName = file.getFileName().toString();
        if (!Files.exists(file)) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "所请求的文件不存在",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
        }
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream inputStream = Files.newInputStream(file);
            response.setContentLengthLong(Files.size(file));
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
                byte[] bytesName = fileName.getBytes("UTF-8");
                fileName = new String(bytesName, "ISO-8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            } else {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20"));
            }
            response.setStatus(HttpStatus.OK.value());
            byte[] bytes = new byte[1024 * 4];
            int readableBytes;
            while ((readableBytes = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, readableBytes);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "写入文件失败",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }


        result.setStatus(HttpStatus.OK.value());
        result.setMsg("下载成功！");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }


    /**
     * 上传模板
     *
     * 1-江苏省电子竞技运动员注册申请表
     *
     * 2-江苏省电子竞技运动协会注册运动员代表资格协议
     *
     * 3-江苏省电子竞技运动员注册信息列表
     *
     * 4-江苏省电子竞技运动员注销申请表
     *
     * @param flag
     * @param template
     * @return
     * @throws UnhandledException
     */
    public ResultBean<Void> upLoadTemplate(Integer flag, MultipartFile template) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();

        Template t = templateDao.selectById(flag);
        if (t == null) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "无此模板！",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }

        String templateName = template.getOriginalFilename();
        String templatePath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "模板"
                + File.separatorChar + t.getName() + File.separatorChar + templateName.substring(templateName.lastIndexOf("."));
        try {
            Path templatePathFile = FileUtils.forceCreateFile(templatePath);
            template.transferTo(templatePathFile);

        } catch (Exception e) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("文件存储失败");
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            throw unhandledException;
        }
        if (t.getFullPath() == null || "".equals(t.getFullPath())) {
            t.setCreateTime(System.currentTimeMillis());
        }
        t.setFullPath(templatePath);
        t.setUpdateTime(System.currentTimeMillis());
        templateDao.update(t);

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("上传成功！");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    private static HttpServletResponse downLoadFiles(List<File> files, HttpServletResponse response, HttpServletRequest request, String name) throws Exception {

        try {
            // 临时文件夹 最好是放在服务器上，方法最后有删除临时文件的步骤
            String zipFilename = SystemConstant.ZIP_FILE_TEMPORARY_PATH + File.separator + name + ".zip";
            File file = new File(zipFilename);
            file.createNewFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            response.reset();
            // response.getWriter()
            // 创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            zipFile(files, zipOut);
            zipOut.close();
            fous.close();
            return downloadZip(file, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private static HttpServletResponse downloadZip(File file, HttpServletResponse response, HttpServletRequest request) throws UnhandledException {
        if (file.exists() == false) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "待压缩的文件目录：" + file + "不存在.",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        } else {
            try {
                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();

                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                String fileName = file.getName();
                String userAgent = request.getHeader("User-Agent").toLowerCase();
                // 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
                if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
                    byte[] bytesName = fileName.getBytes("UTF-8");
                    fileName = new String(bytesName, "ISO-8859-1");
                    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                } else {
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20"));
                }

                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    File f = new File(file.getPath());
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }



    private HashMap<String, String> parseUuid(String uuid) throws UnhandledException {
        String attributes = redisTemplate.opsForValue().get(uuid) + "";

        //todo 文件属性为空

        if ("".equals(attributes) || "null".equals(attributes)) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "文件属性为空",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
        }
        HashMap<String, String> hashMap;
        try {
            hashMap = objectMapper.readValue(attributes, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "解析文件属性时发生错误",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }
        BaseUtils.getValueWithThrow(hashMap.get("fileFullPath"), BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
        return hashMap;
    }
}
