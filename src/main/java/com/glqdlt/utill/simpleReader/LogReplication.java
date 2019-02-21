package com.glqdlt.utill.simpleReader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Jhun
 * 2019-02-19
 */
public class LogReplication {
    public LogReplication(Field field, Method method, String headName) {
        this.field = field;
        this.method = method;
        this.headName = headName;
    }

    public Field getField() {

        return field;
    }

    public Method getMethod() {
        return method;
    }

    public String getHeadName() {
        return headName;
    }

    Field field;
    Method method;
    String headName;
}
