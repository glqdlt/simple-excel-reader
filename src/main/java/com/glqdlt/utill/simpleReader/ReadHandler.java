package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Row;

@FunctionalInterface
public interface ReadHandler<T> {
    T read(Row row);
}
