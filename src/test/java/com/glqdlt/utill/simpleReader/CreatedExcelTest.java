package com.glqdlt.utill.simpleReader;

import org.junit.Test;

import java.util.Date;

/**
 * @author Jhun
 * 2019-02-21
 */
public class CreatedExcelTest {

    public static class SomeObject {
        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }

        public Date pullDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @SimpleReaderOption(customColumnName = "스트링컬럼")
        private String string;
        @SimpleReaderOption(customColumnName = "인트컬럼")
        private Integer integer;
        @SimpleReaderOption(getMethodPrefix = "pull")
        private Date date;
    }

    @Test
    public void excelCreate() {



    }
}
