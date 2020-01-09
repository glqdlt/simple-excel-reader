package com.glqdlt.utill.simpleReader;

import com.glqdlt.utill.simpleReader.annotation.ExcelColumnOption;
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

        @ExcelColumnOption(columnName = "스트링컬럼")
        private String string;
        @ExcelColumnOption(columnName = "인트컬럼")
        private Integer integer;
    }

    @Test
    public void excelCreate() throws IOException {

        SomeObject someObject = new SomeObject();
        someObject.setInteger(99);
        someObject.setString("헬로우");

        List<SomeObject> data = Collections.singletonList(someObject);

        File excel = new File(System.getProperty("user.home") + File.separator + "testExcel.xlsx");
        SimpleExcelReader simpleExcelReader = new SimpleExcelReader();
        simpleExcelReader.create(excel, data);

    }
}
