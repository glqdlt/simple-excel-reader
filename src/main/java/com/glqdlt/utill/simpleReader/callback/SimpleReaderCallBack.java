package com.glqdlt.utill.simpleReader.callback;

import org.apache.poi.ss.usermodel.Row;

/**
 * @param <T>
 */
public interface SimpleReaderCallBack<T> {
    T exec(Row row);
}
