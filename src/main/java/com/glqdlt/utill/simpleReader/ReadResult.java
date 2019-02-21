package com.glqdlt.utill.simpleReader;

/**
 * @author Jhun
 */
public class ReadResult<T> {
    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    private boolean passed;
    private T item;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    private int rowNum;


}
