package com.glqdlt.utill.simpleReader;

public class SimpleExcelReaderException extends RuntimeException {

    private int rowNum;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getRowNum() {
        return rowNum;
    }


    public SimpleExcelReaderException(Throwable throwable, int rowNum) {
        super("Problem on row "+rowNum, throwable);
        this.errorMessage = throwable.getMessage();
        this.rowNum = rowNum;
    }

}
