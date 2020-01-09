package com.glqdlt.utill.simpleReader.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jhun
 * 2020-01-22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelMakeOption {
    String customColumnName() default "";

    String customGetMethodPrefix() default "";

    boolean ignore() default false;
}
