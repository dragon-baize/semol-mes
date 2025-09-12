package com.senmol.mes.common.utils;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 单个参数校验工具
 *
 * @author Administrator
 */
public class CheckToolUtil {

    private static final String CODE = "code";
    private static final String ID = "id";
    public static final String LIMIT = "limit 1";
    private static final List<String> FILE_FORMAT = new ArrayList<>(Arrays.asList(".xlsx", ".xls"));

    /**
     * 编号重复校验
     */
    @SuppressWarnings("all")
    public static long checkCodeExist(IService service, Long id, String code) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(CODE, code);

        if (ObjUtil.isNotNull(id)) {
            wrapper.ne(ID, id);
        }

        wrapper.last(LIMIT);
        return service.count(wrapper);
    }

    /**
     * 文件格式校验
     */
    public static String checkFileFormat(MultipartFile file) {
        if (file == null) {
            return "文件不能为空！";
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            return "文件名称不能为空！";
        }

        int lastIndexOf = filename.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "文件缺少后缀！";
        }

        String suffix = filename.substring(lastIndexOf);
        if (!FILE_FORMAT.contains(suffix)) {
            return "仅支持 `xls` 和 `xlsx` 格式！";
        }

        return null;
    }

}
