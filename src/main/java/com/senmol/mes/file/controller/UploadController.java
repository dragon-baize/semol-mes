package com.senmol.mes.file.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.file.utils.UploadUtil;
import com.senmol.mes.log.annotation.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("file")
public class UploadController {

    /**
     * 图片大小（单位MB）
     */
    @Value("${file.upload.image.max-size}")
    private Integer imageMaxSize;
    /**
     * 文件大小（单位MB）
     */
    @Value("${file.upload.file.max-size}")
    private Integer fileMaxSize;
    /**
     * 图片格式
     */
    @Value("#{'${file.upload.image.format:}'.split(',')}")
    private List<String> imageFormat;
    /**
     * 文件格式
     */
    @Value("#{'${file.upload.file.format}'.split(',')}")
    private List<String> fileFormat;
    /**
     * 上传文件存放位置
     */
    @Value("${file.upload.path}")
    private String uploadPath;
    /**
     * 文件大小由MB转成B乘数
     */
    private final Long MULTIPLY = 1048576L;

    /**
     * 上传图片
     *
     * @param image 图片
     * @return 图片保存地址
     */
    @Logger("图片上传")
    @PostMapping("uploadPic")
    public SaResult uploadPic(@RequestParam("file") MultipartFile image) {
        // 获取头像图片
        String filename = image.getOriginalFilename();
        if (image.getSize() == 0 || !StringUtils.hasText(filename)) {
            return SaResult.error("未发现图片或图片名为空");
        }

        // 限制头像大小
        if (image.getSize() > imageMaxSize * MULTIPLY) {
            return SaResult.error("上传图片大小不能超过 " + imageMaxSize + "MB");
        }

        // 校验头像名称
        String fileSuffix = filename.substring(filename.lastIndexOf("."));
        // 仅支持JPG、PNG格式
        if (imageFormat.size() > 0 && !"".equals(imageFormat.get(0)) && !imageFormat.contains(fileSuffix.toLowerCase())) {
            return SaResult.error("图片格式有误，支持的格式为：" + imageFormat);
        }

        // 获取当前日期作为头像存储位置
        String localDate = LocalDate.now().toString();
        String filePath = uploadPath + "/image/" + localDate;

        String newFileName;
        try {
            newFileName = UploadUtil.upload(image, fileSuffix, filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return SaResult.error("图片上传失败：" + e.getMessage());
        }

        return SaResult.ok("/image/" + localDate + "/" + newFileName);
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件保存地址
     */
    @Logger("文件上传")
    @PostMapping("uploadFile")
    public SaResult uploadFile(@RequestParam("file") MultipartFile file) {
        // 获取文件
        String filename = file.getOriginalFilename();
        if (file.getSize() == 0 || !StringUtils.hasText(filename)) {
            return SaResult.error("未发现文件或文件名为空");
        }

        // 限制文件大小
        if (file.getSize() > fileMaxSize * MULTIPLY) {
            return SaResult.error("上传文件大小不能超过 " + fileMaxSize + "MB");
        }

        // 校验文件名称
        String fileSuffix = filename.substring(filename.lastIndexOf("."));
        // 仅支持的格式
        if (fileFormat.size() > 0 && !"".equals(fileFormat.get(0)) && !fileFormat.contains(fileSuffix.toLowerCase())) {
            return SaResult.error("文件格式有误，支持的格式为：" + fileFormat);
        }


        // 获取当前日期作为文件存储位置
        String localDate = LocalDate.now().toString();
        String filePath = uploadPath + "/file/" + localDate;

        String newFileName;
        try {
            newFileName = UploadUtil.upload(file, fileSuffix, filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return SaResult.error("文件上传失败：" + e.getMessage());
        }

        return SaResult.ok("/file/" + localDate + "/" + newFileName);
    }

    /**
     * 预览、下载
     *
     * @param filePath 文件路径
     * @param type     0-预览、1-下载
     * @param response 响应对象
     * @return 预览、下载结果
     */
    @CrossOrigin
    @GetMapping("preOrDown")
    public SaResult preOrDown(@RequestParam("filePath") String filePath, @RequestParam("type") Integer type,
                              HttpServletResponse response) {
        try {
            UploadUtil.filePreviewOrDownload(uploadPath, filePath, type, response);
        } catch (IOException e) {
            e.printStackTrace();
            return SaResult.error("文件获取失败：" + e.getMessage());
        }

        return SaResult.ok("文件获取成功");
    }
}
