package com.glqdlt.utill.simpleReader;

/**
 * @author Jhun
 */
public class ExcelReaderOption {
    private Boolean save;
    private String path;

    public String getPath() {
        return path;
    }

    private ExcelReaderOption(Integer rowNum, Integer sheetNum, Boolean save, String path) {
        this.rowNum = rowNum;
        this.sheetNum = sheetNum;
        this.save = save;
        this.path = path;
    }


    public Integer getRowNum() {
        return rowNum;
    }

    public Integer getSheetNum() {
        return sheetNum;
    }

    private Integer rowNum;
    private Integer sheetNum;

    public boolean isSave() {
        return this.save;
    }


    public static class Builder {

        public static ExcelReaderOption build(Integer rowNum, Integer sheetNum, Boolean save, String path) {
            return new ExcelReaderOption(rowNum, sheetNum, save, path);
        }


        public static ExcelReaderOption build(Integer rowNum, Integer sheetNum) {
            return new ExcelReaderOption(rowNum, sheetNum, false, "");
        }

    }

}
