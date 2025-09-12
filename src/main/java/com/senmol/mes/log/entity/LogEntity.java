package com.senmol.mes.log.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作日志记录表 sys_log
 *
 * @author Administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_log")
public class LogEntity extends Model<LogEntity> {
    private static final long serialVersionUID = -8376583786951202646L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 日志类型
     */
    @TableField("type")
    private String type;
    /**
     * 方法执行耗时
     */
    @TableField("time")
    private Long time;
    /**
     * 操作人ID
     */
    @TableField("op_user_id")
    private Long opUserId;
    /**
     * 操作时间
     */
    @TableField("op_time")
    private LocalDateTime opTime;
    /**
     * 描述
     */
    @TableField("description")
    private String description;
    /**
     * 方法
     */
    @TableField("method")
    private String method;
    /**
     * 参数
     */
    @TableField("params")
    private String params;
    /**
     * 异常信息
     */
    @TableField("exception_msg")
    private String exceptionMsg;
    /**
     * 访问IP
     */
    @TableField("ip")
    private String ip;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
