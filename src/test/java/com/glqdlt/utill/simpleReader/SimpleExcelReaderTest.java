package com.glqdlt.utill.simpleReader;

import com.glqdlt.utill.simpleReader.callback.SimpleReaderCallBack;
import org.apache.poi.ss.usermodel.Row;
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

            List<ExcelCellData> result = new SimpleExcelReader().read(is, 3,
                    new SimpleReaderCallBack<ExcelCellData>() {
                        @Override
                        public ExcelCellData exec(Row row) {

                            ExcelCellData data = new ExcelCellData();
                            data.setSeq((int) row.getCell(0).getNumericCellValue());
                            data.setRegDate(row.getCell(1).getStringCellValue());
                            data.setRank((int) row.getCell(2).getNumericCellValue());
                            data.setAuthor(row.getCell(3).getStringCellValue());
                            data.setTitle(row.getCell(4).getStringCellValue());

                            return data;
                        }
                    }
            );

            for (ExcelCellData e : result) {
                System.out.println(e.getTitle());
            }
        }
    }
}