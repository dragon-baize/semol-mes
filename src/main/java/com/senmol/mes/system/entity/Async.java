package com.senmol.mes.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 异步异常(Async)表实体类
 *
 * @author makejava
 * @since 2024-04-15 09:48:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_async")
public class Async extends Model<Async> {
    private static final long serialVersionUID = 1027104673336291163L;
    /**
     * 异常位置
     */
    @TableField("method")
    private String method;
    /**
     * 参数
     */
    @TableField("args")
    private String args;
    /**
     * 异常原因
     */
    @TableField("e_msg")
    private String eMsg;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

}

