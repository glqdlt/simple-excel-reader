package com.glqdlt.utill.simpleReader;

/**
 * @author Jhun
 * 2020-01-09
 */
public class ExcelException extends RuntimeException {
    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
