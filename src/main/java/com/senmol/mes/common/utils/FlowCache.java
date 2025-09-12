package com.senmol.mes.common.utils;

import java.lang.annotation.*;

/**
 * @author Administrator
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowCache {

    String entity() default "";

    String table();

    boolean isAdd() default true;

}
