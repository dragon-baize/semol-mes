package com.senmol.mes.warehouse.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.warehouse.entity.MoveRecord;
import com.senmol.mes.warehouse.vo.MoveVo;

import java.util.List;

/**
 * 迁库记录(MoveRecord)表服务接口
 *
 * @author makejava
 * @since 2023-12-21 11:24:51
 */
public interface MoveRecordService extends IService<MoveRecord> {

    SaResult move(List<MoveVo> moveVos);
}

