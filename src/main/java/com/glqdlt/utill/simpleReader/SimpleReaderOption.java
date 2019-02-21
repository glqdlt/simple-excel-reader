package com.glqdlt.utill.simpleReader;

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
public @interface SimpleReaderOption {
    String getMethodPrefix() default "get";
    boolean skipField() default false;
    String customColumnName() default "";
}
