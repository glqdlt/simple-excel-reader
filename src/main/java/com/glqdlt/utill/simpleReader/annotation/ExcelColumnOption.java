package com.glqdlt.utill.simpleReader.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jhun
 * 2019-02-19
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumnOption {
//    String[] customGetMethodPrefix() default {};

    String columnName() default "";

    int columnOrder() default 0;

}
