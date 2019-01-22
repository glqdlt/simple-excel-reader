package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Row;

@FunctionalInterface
public interface ReadMatchHandler<T> {
    ReadResult<T> read(Row row);
}
