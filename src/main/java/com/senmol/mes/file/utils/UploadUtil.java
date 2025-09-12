package com.senmol.mes.file.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * 通用工具类
 *
 * @author Administrator
 */
public class UploadUtil {
    /**
     * 文件上传
     *
     * @param file       文件
     * @param fileSuffix 文件后缀
     * @param filePath   文件上传路径
     * @throws IOException 写异常
     */
    @SuppressWarnings("all")
    public static String upload(MultipartFile file, String fileSuffix, String filePath) throws IOException {
        // 判断文件夹是否存在
        File path = new File(filePath);
        if (!path.exists()) {
            // 文件夹不存在则创建
            path.mkdirs();
        }

        // 替换文件名称
        String fileName = UUID.randomUUID() + fileSuffix;
        // 完成上传
        file.transferTo(new File(filePath, fileName));
        // 返回文件名
        return fileName;
    }

    /**
     * 文件预览、下载
     *
     * @param uploadPath 文件上传路径
     * @param path       文件路径
     * @param type       预览、下载
     * @param response   响应对象
     * @throws IOException 读写异常
     */
    public static void filePreviewOrDownload(String uploadPath, String path, Integer type,
                                             HttpServletResponse response) throws IOException {
        if (type == 1) {
            // 下载
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment;filename*=utf-8''" + path.substring(path.lastIndexOf("/") + 1));
        }
        // 创建文件输入流
        FileInputStream fis = new FileInputStream(uploadPath + path);
        // 获取输出流
        OutputStream os = response.getOutputStream();
        // 设置每次读取的数据量
        byte[] bytes = new byte[1024];
        int len;
        while ((len = fis.read(bytes)) != -1) {
            os.write(bytes, 0, len);
        }

        fis.close();
        os.close();
    }
}
