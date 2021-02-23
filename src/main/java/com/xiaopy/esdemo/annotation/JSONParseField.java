package com.xiaopy.esdemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义JSON解析
 *
 * @author xiaopeiyu
 * @since 2021/2/2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface JSONParseField {

    /**
     * 字段别称
     *
     * @return 字段别称
     */
    String aliasName() default "";

    /**
     * 字段JsonPath
     *
     * @return 字段JsonPath
     */
    String path() default "";
}
