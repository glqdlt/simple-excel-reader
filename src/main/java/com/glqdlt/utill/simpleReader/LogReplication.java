package com.glqdlt.utill.simpleReader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 리플렉션으로 엑셀 Object 매핑할 때 사용하는 내부에서 참조만하는 Value Object.
 *
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
