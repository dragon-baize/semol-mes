package com.senmol.mes.project.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class NapeFileVo implements Serializable {
    private static final long serialVersionUID = -5247865043410342814L;
    /**
     * 项目阶段
     */
    private Integer napeBelong;
    /**
     * 选项名称
     */
    private String napeTitle;
    /**
     * 主键
     */
    private Long id;
    /**
     * 添加项ID
     */
    private Long napeId;
    /**
     * 项目ID
     */
    private Long manageId;
    /**
     * 文件原名
     */
    private String originalTitle;
    /**
     * 文件实际名
     */
    private String realityTitle;
    /**
     * 文件上传路径
     */
    private String filePath;
    /**
     * 类型 0-文件 1-数据
     */
    private Integer type;
    /**
     * 是否审核 0-待审 1-通过 2-不通过
     */
    private Integer approveResult;
    /**
     * 自定义版本号
     */
    private String revision;
    /**
     * 文件说明
     */
    private String remarks;
    /**
     * 推送时间
     */
    private LocalDateTime createTime;
    /**
     * 推送人ID
     */
    private Long createUser;
    /**
     * 推送人
     */
    private String createUserName;
}
