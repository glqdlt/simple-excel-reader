package com.glqdlt.utill.simpleReader;

public class ExcelMappingDataWithRowNumber<T> implements ExcelMappingResult<T> {
    private Integer rowNumber;
    private T data;

    public Integer getRowNumber() {
        return rowNumber;
    }

    public ExcelMappingDataWithRowNumber(Integer rowNumber, T data) {
        this.rowNumber = rowNumber;
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
