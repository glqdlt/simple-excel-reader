package com.glqdlt.utill.simpleReader;

import com.glqdlt.utill.simpleReader.annotation.ExcelReadOption;
import com.glqdlt.utill.simpleReader.annotation.ExcelMakeOption;
import com.glqdlt.utill.simpleReader.config.ArchiveConfig;
import com.glqdlt.utill.simpleReader.config.ArchiveOption;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Jhun
 * 2020-01-09
 */
public class ObjectMappingTest {

    public static class SomeObject {
        private Integer intField;
        private LocalDateTime localDateTimeField;
        private Integer name;
        @ExcelMakeOption(customColumnName = "customName")
        private String name2;
        @ExcelReadOption(ignore = true)
        private String ignoreField;

        public Integer getIntField() {
            return intField;
        }

        public void setIntField(Integer intField) {
            this.intField = intField;
        }

        public LocalDateTime getLocalDateTimeField() {
            return localDateTimeField;
        }

        public void setLocalDateTimeField(LocalDateTime localDateTimeField) {
            this.localDateTimeField = localDateTimeField;
        }

        public Integer getName() {
            return name;
        }

        public void setName(Integer name) {
            this.name = name;
        }

        public String getName2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }

        public String getIgnoreField() {
            return ignoreField;
        }

        public void setIgnoreField(String ignoreField) {
            this.ignoreField = ignoreField;
        }

    }


    @Test
    public void ignoreField() {
        SimpleArchiveExcelReader simpleArchiveExcelReader = new SimpleArchiveExcelReader();
        List<Field> aaa = simpleArchiveExcelReader.checkFields(SomeObject.class);
        Assert.assertEquals(4, aaa.size());
    }

    @Test
    public void name() throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException {

        try (FileInputStream inputStream = new FileInputStream(new File("src/test/resources/test-data2.xlsx"))) {
            ArchiveConfig archiveConfig = new ArchiveConfig();
            archiveConfig.setMakeOption(new ArchiveOption(false, null));
            archiveConfig.setReadOption(new ArchiveOption(false, null));
            archiveConfig.addDateTimeFormat(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            SimpleArchiveExcelReader simpleExcelReader = new SimpleArchiveExcelReader(archiveConfig);
            XSSFSheet sheet = simpleExcelReader.getSheet(inputStream, 0);

            List<ExcelMappingResult<SomeObject>> asd = simpleExcelReader.mappingObject(sheet, SomeObject.class);

            Assert.assertEquals(5, asd.size());
            for (ExcelMappingResult<SomeObject> ss : asd) {
                Assert.assertNull(ss.getData().getIgnoreField());
            }
        }

    }
}
