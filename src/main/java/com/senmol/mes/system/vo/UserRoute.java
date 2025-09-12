package com.senmol.mes.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 前端页面动态路由封装
 *
 * @author Administrator
 */
@Data
public class UserRoute implements Serializable {
    private static final long serialVersionUID = 5058652213237495139L;

    private Long id;

    private String path;

    private String name;

    private Boolean hidden;

    private String component;

    private Map<String, Object> meta;

    private List<UserRoute> children;
}
