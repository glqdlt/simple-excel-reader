package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Row;
/**
 * @author Jhun
 */
@FunctionalInterface
public interface ReadHandler<T> {
    T read(Row row);
}
