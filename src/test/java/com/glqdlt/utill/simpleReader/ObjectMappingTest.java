package com.glqdlt.utill.simpleReader;

import com.glqdlt.utill.simpleReader.annotation.ExcelColumnIgnore;
import com.glqdlt.utill.simpleReader.annotation.ExcelColumnOption;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Jhun
 * 2020-01-09
 */
public class ObjectMappingTest {

    public static class SomeObject {
        private Date dateField;
        private LocalDateTime localDateTimeField;
        private String name;
        @ExcelColumnOption(columnName = "customName")
        private String name2;
        @ExcelColumnIgnore
        private String ignoreField;
        private Integer numb;
    }


    @Test
    public void name() {

        SimpleArchiveExcelReader simpleArchiveExcelReader = new SimpleArchiveExcelReader();
        List<Field> aaa = simpleArchiveExcelReader.checkFields(SomeObject.class);
        Assert.assertEquals(5, aaa.size());


    }
}
