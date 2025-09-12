package com.senmol.mes.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限绑定
 *
 * @author Administrator
 */
@Data
public class PermRoleVo implements Serializable {
    private static final long serialVersionUID = 2938485467800439368L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String title;
    /**
     * 子级
     */
    private List<PermRoleVo> child = new ArrayList<>();

}
