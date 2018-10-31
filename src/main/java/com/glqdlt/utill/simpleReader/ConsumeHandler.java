package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Row;

@FunctionalInterface
public interface ConsumeHandler {
    void consume(Row row);
}
