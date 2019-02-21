package com.glqdlt.utill.simpleReader;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Jhun
 * 2019-02-21
 */
@Ignore
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
    public void excelCreate() throws IOException {

        SomeObject someObject = new SomeObject();
        someObject.setDate(DateUtils.asDate(LocalDate.of(2018, 1, 1)));
        someObject.setInteger(99);
        someObject.setString("헬로우");

        List<SomeObject> data = Collections.singletonList(someObject);

        File excel = new File(System.getProperty("user.home") + File.separator + "testExcel.xlsx");
        SimpleExcelReader simpleExcelReader = new SimpleExcelReader();
        simpleExcelReader.create(excel, data);

    }
}
