package com.senmol.mes.log.annotation;

import java.lang.annotation.*;

/**
 * @author Administrator
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logger {
    /**
     * 方法描述
     */
    String value();
}
