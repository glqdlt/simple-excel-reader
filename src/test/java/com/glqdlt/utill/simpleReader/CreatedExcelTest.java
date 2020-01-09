package com.glqdlt.utill.simpleReader;

import com.glqdlt.utill.simpleReader.annotation.ExcelMakeOption;
import com.glqdlt.utill.simpleReader.config.ArchiveConfig;
import com.glqdlt.utill.simpleReader.config.ArchiveOption;
import com.glqdlt.utill.simpleReader.config.ExcelArchiveFileStrategys;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
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

        @ExcelMakeOption(customColumnName = "스트링컬럼")
        private String string;
        @ExcelMakeOption(customColumnName = "인트컬럼")
        private Integer integer;
    }

    @Test
    public void excelCreate() throws IOException {

        SomeObject someObject = new SomeObject();
        someObject.setInteger(99);
        someObject.setString("헬로우");

        List<SomeObject> data = Collections.singletonList(someObject);

        File excel = new File(System.getProperty("user.home") + File.separator + "testExcel.xlsx");
        ArchiveConfig archiveConfig = new ArchiveConfig();
        archiveConfig.setMakeOption(new ArchiveOption(false, ExcelArchiveFileStrategys.SIMPLE_ARCHIVE_STRAEGY));
        archiveConfig.setReadOption(new ArchiveOption(false, ExcelArchiveFileStrategys.SIMPLE_ARCHIVE_STRAEGY));
        SimpleArchiveExcelReader simpleArchiveExcelReader = new SimpleArchiveExcelReader(archiveConfig);
        File aaa = simpleArchiveExcelReader.make(excel, data, SomeObject.class);
        Assert.assertTrue(aaa.exists());
        Assert.assertEquals(aaa.getAbsolutePath(), excel.getAbsolutePath());
        aaa.deleteOnExit();
    }
}
