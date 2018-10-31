package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Row;

@FunctionalInterface
public interface ConsumeHandler<T> {
    void read(Row row);
}
