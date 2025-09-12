package com.senmol.mes.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageUtil<T> extends Page<T> {
    private static final long serialVersionUID = 5893028719984150116L;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;
    /**
     * 是否导出
     */
    private Integer isExport = 0;

}
