package com.glqdlt.utill.simpleReader;

import com.glqdlt.utill.simpleReader.callback.SimpleReaderCallBack;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SimpleExcelReaderTest {

    @Test
    public void read() throws IOException {
        try (InputStream is = new FileInputStream(new File("src/test/resources/test-data.xlsx"))) {

            List<ExcelCellData> result = new SimpleExcelReader()

                    .read(
                            is,
                            3,
                                (SimpleReaderCallBack<ExcelCellData>) row -> ExcelCellData
                                .builder()

                                .seq((int) row.getCell(0).getNumericCellValue())
                                .regDate(row.getCell(1).getStringCellValue())
                                .rank((int) row.getCell(2).getNumericCellValue())
                                .author(row.getCell(3).getStringCellValue())
                                .title(row.getCell(4).getStringCellValue())

                                .build()
            );

            for (ExcelCellData e : result) {
                System.out.println(e.getTitle());
            }
        }
    }
}