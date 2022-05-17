package com.stackfarm.esports.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackfarm.esports.a.SpringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author croton
 * @create 2021/4/2 18:52
 */
public class FileUtils {
    /**
     * 强制创建文件
     * @param filePath 文件路径
     * @return 文件
     */
    public static Path forceCreateFile(String filePath) throws IOException {

        Path path = Paths.get(filePath.substring(0, filePath.lastIndexOf(File.separator)));
        Path file = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
        return file;
    }

    /**
     * 强制创建文件,删除已有
     * @param filePath 文件路径
     * @return 文件
     */
    public static Path forceCreateFilePlus(String filePath) throws IOException {

        Path file = Paths.get(filePath);
        if (!Files.exists(file)) {
            Files.createFile(file);
        } else {
            Files.delete(file);
            Files.createFile(file);
        }
        return file;
    }

    /**
     * 上传
     */
    public static String upload(MultipartFile multipartFile, String path) {

        try {
            multipartFile.transferTo(forceCreateFile(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 通过文件路径直接修改文件名,支持文件，目录
     * TODO
     * @param filePath    需要修改的文件的完整路径
     * @param newFileName 需要修改的文件的名称
     */
    public static String rename(String filePath, String newFileName) {
        File f = new File(filePath);
        // 判断原文件是否存在（防止文件名冲突）
        if (!f.exists()) {
            return null;
        }
        newFileName = newFileName.trim();
        // 文件名不能为空
        if ("".equals(newFileName)) {
            return null;
        }
        String newFilePath = null;
        // 判断是否为文件夹
        if (f.isDirectory()) {
            newFilePath = filePath.substring(0, filePath.lastIndexOf(File.separator)) + File.separator + newFileName;
        } else {
            newFilePath = filePath.substring(0, filePath.lastIndexOf(File.separator)) + File.separator + newFileName
                    + filePath.substring(filePath.lastIndexOf("."));
        }
        File nf = new File(newFilePath);
        try {
            // 修改文件名
            f.renameTo(nf);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newFilePath;
    }

    /**
     * 递归删除目录及其子文件夹
     */
    public static void deleteFile(Path path) {

        if (path == null || !Files.exists(path) || "".equals(path.toString())) {
        } else {
            try {
                if (Files.isDirectory(path)) {
                    Files.list(path).forEach(FileUtils::deleteFile);
                    Files.delete(path);
                } else {
                    Files.delete(path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String hidePath(String fileFullPath) {
        if (fileFullPath == null || "".equals(fileFullPath)) {
            return "";
        }
        String uuid = BaseUtils.getUUID();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fileFullPath", fileFullPath);
        try {
            RedisTemplate<String, Object> redisTemplate = SpringUtils.getBean("redisTemplate");
            ObjectMapper objectMapper = new ObjectMapper();
            redisTemplate.opsForValue().set(uuid, objectMapper.writeValueAsString(hashMap), 12, TimeUnit.HOURS);
        } catch (JsonProcessingException ignored) {
            ;
        }
        return uuid;
    }

    public static String hidePaths(String fileFullPath) {
        List<String> uuids = new ArrayList<>();
        String result = "";
        if (fileFullPath == null || "".equals(fileFullPath)) {
            return "";
        }
        String[] paths = fileFullPath.split(";");
        for (String path : paths) {
            if (!"".equals(path) && path != null) {
                String uuid = BaseUtils.getUUID();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("fileFullPath", path);
                try {
                    RedisTemplate<String, Object> redisTemplate = SpringUtils.getBean("redisTemplate");
                    ObjectMapper objectMapper = new ObjectMapper();
                    redisTemplate.opsForValue().set(uuid, objectMapper.writeValueAsString(hashMap), 12, TimeUnit.HOURS);
                    uuids.add(uuid);
                } catch (JsonProcessingException ignored) {
                    ;
                }
            }
        }
        int i = 0;
        StringBuilder sb = new StringBuilder();
        if (uuids.size() > 0) {
            for (String uuid : uuids) {
                if (i > 0) {
                    sb.append("_");
                }
                sb.append(uuid);
                i++;
            }
        }
        result = sb.toString();
        return result;
    }

    /**
     * 把接受的全部文件打成压缩包
     * @param files;
     * @param outputStream
     */
    public static void zipFile(List<File> files, ZipOutputStream outputStream) {
        int size = files.size();
        for (int i = 0; i < size; i++) {
            File file = files.get(i);
            zipFile(file, outputStream);
        }
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     *
     * @param inputFile
     * @param outputStream
     */
    public static void zipFile(File inputFile, ZipOutputStream outputStream) {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    outputStream.putNextEntry(new ZipEntry(inputFile.getName()));
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], outputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
