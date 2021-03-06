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
     * ?????????????????????
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
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "???????????????????????????",
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
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "??????????????????",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("???????????????");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ?????????????????????
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
        //????????????123_456?????????????????????????????????
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
                throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "?????????????????????",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
            }
            if(groupName == null || "".equals(groupName)){
                groupName = "????????????";
            }
            downLoadFiles(files, response, request, groupName + " ????????????");
        } else if ('_' == uuid.charAt(0)) {
            //??????????????????
            for (String u : uuids) {
                if (u != null & !"".equals(u)) {
                    Map<String, String> hashMap = parseUuid(u);
                    String fileFullPath = hashMap.get("fileFullPath");
                    sb.append(fileFullPath + ";");
                    files.add(new File(fileFullPath));
                }
            }
            downLoadFiles(files, response, request, "??????????????????");
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
                throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "?????????????????????",
                        BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
            }
            if(groupName == null || "".equals(groupName)){
                groupName = "????????????";
            }
            downLoadFiles(files, response, request, groupName + " ????????????");
        }

        result.setStatus(HttpStatus.OK.value());
        result.setMsg("???????????????");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * ????????????
     *
     * 1-?????????????????????????????????????????????
     *
     * 2-??????????????????????????????????????????????????????????????????
     *
     * 3-????????????????????????????????????????????????
     *
     * 4-?????????????????????????????????????????????
     * @param flag
     * @return
     */
    @Override
    public ResultBean<?> downLoadTemplate(HttpServletRequest request, HttpServletResponse response, Integer flag) throws UnhandledException {
        ResultBean<Void> result = new ResultBean<>();
        Template template = templateDao.selectById(flag);
        if (template == null) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "?????????????????????",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }


        Path file = Paths.get(template.getFullPath());
        String fileName = file.getFileName().toString();
        if (!Files.exists(file)) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "???????????????????????????",
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
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "??????????????????",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }


        result.setStatus(HttpStatus.OK.value());
        result.setMsg("???????????????");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }


    /**
     * ????????????
     *
     * 1-?????????????????????????????????????????????
     *
     * 2-??????????????????????????????????????????????????????????????????
     *
     * 3-????????????????????????????????????????????????
     *
     * 4-?????????????????????????????????????????????
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
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "???????????????",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }

        String templateName = template.getOriginalFilename();
        String templatePath = SystemConstant.FILE_ROOT_PATH + File.separatorChar + "??????"
                + File.separatorChar + t.getName() + File.separatorChar + templateName.substring(templateName.lastIndexOf("."));
        try {
            Path templatePathFile = FileUtils.forceCreateFile(templatePath);
            template.transferTo(templatePathFile);

        } catch (Exception e) {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("??????????????????");
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
        result.setMsg("???????????????");
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    private static HttpServletResponse downLoadFiles(List<File> files, HttpServletResponse response, HttpServletRequest request, String name) throws Exception {

        try {
            // ??????????????? ????????????????????????????????????????????????????????????????????????
            String zipFilename = SystemConstant.ZIP_FILE_TEMPORARY_PATH + File.separator + name + ".zip";
            File file = new File(zipFilename);
            file.createNewFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            response.reset();
            // response.getWriter()
            // ?????????????????????
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
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "???????????????????????????" + file + "?????????.",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        } else {
            try {
                // ??????????????????????????????
                InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // ??????response
                response.reset();

                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                String fileName = file.getName();
                String userAgent = request.getHeader("User-Agent").toLowerCase();
                // ?????????????????????????????????????????????????????????URLEncoder.encode??????????????????
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

        //todo ??????????????????

        if ("".equals(attributes) || "null".equals(attributes)) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(), "??????????????????",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]), new Date());
        }
        HashMap<String, String> hashMap;
        try {
            hashMap = objectMapper.readValue(attributes, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "?????????????????????????????????",
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),new Date());
        }
        BaseUtils.getValueWithThrow(hashMap.get("fileFullPath"), BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
        return hashMap;
    }
}
