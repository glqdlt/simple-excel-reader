package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Row;

public class ExcelReaderException extends RuntimeException {

    private Row row;

    public ExcelReaderException(String message, Throwable cause, Row row) {
        super(message, cause);
        this.row = row;
    }


    public Row getRow() {
        return row;
    }
}
