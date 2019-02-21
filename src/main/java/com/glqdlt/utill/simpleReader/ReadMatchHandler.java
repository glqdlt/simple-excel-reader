package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Row;
/**
 * @author Jhun
 */
@FunctionalInterface
public interface ReadMatchHandler<T> {
    ReadResult<T> read(Row row);
}
