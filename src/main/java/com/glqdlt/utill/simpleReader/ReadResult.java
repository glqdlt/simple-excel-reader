package com.glqdlt.utill.simpleReader;


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


}
