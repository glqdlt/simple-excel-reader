package com.glqdlt.utill.simpleReader;

public class ExcelReaderOption {

    private ExcelReaderOption(Integer rowNum, Integer sheetNum) {
        this.rowNum = rowNum;
        this.sheetNum = sheetNum;
    }


    public Integer getRowNum() {
        return rowNum;
    }

    public Integer getSheetNum() {
        return sheetNum;
    }

    private Integer rowNum;
    private Integer sheetNum;


    public static class Builder{

        public static ExcelReaderOption build(Integer rowNum, Integer sheetNum){
            return new ExcelReaderOption(rowNum,sheetNum);
        }


    }

}
