package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Row;

public class SimpleExcelReaderException extends RuntimeException {

    private Row row;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public Row getRow() {
        return row;
    }

    public SimpleExcelReaderException(Throwable throwable, Row row) {
        super(String.format("Problem on row number : %s", row.getRowNum()), throwable);
        this.errorMessage = throwable.getMessage();
        this.row = row;
    }

}
