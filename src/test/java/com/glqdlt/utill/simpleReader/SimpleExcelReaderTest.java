package com.glqdlt.utill.simpleReader;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SimpleExcelReaderTest {

    @Test
    public void consumee() throws IOException {


        try (FileInputStream inputStream = new FileInputStream(new File("src/test/resources/test-data.xlsx"))) {
            SimpleExcelReader simpleExcelReader = new SimpleExcelReader();
            List<TestPoiReadObject> testPoiReadObjects = new ArrayList<>();

            try {
                simpleExcelReader.consume(inputStream, (row) -> {
                    if (row.getRowNum() != 5) {

                        TestPoiReadObject t = new TestPoiReadObject();
                        t.setAuthor(row.getCell(3).getStringCellValue());
                        t.setTitle(row.getCell(4).getStringCellValue());
                        testPoiReadObjects.add(t);

                    }

                }, ExcelReaderOption.Builder.build(0, 0));
            } catch (ExcelReaderException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println(String.format("%s row 에서 의 문제가 발생", e.getRow().getRowNum()));
            }
            testPoiReadObjects.forEach(x -> System.out.println(x.getTitle()));
            Assert.assertEquals("title", testPoiReadObjects.get(0).getTitle());
        }
    }

    @Test(expected = RuntimeException.class)
    public void readWithResult() throws IOException {

        try (FileInputStream inputStream = new FileInputStream(new File("src/test/resources/bad-data.xlsx"))) {
            SimpleExcelReader simpleExcelReader = new SimpleExcelReader();
            try {
                List<TestPoiReadObject> testPoiReadObjects = simpleExcelReader.read(inputStream,
                        (row) -> {
                            String author = row.getCell(3).getStringCellValue();
                            String title = row.getCell(4).getStringCellValue();
                            TestPoiReadObject testPoiReadObject = new TestPoiReadObject();
                            testPoiReadObject.setTitle(title);
                            testPoiReadObject.setAuthor(author);
                            return testPoiReadObject;

                        },
                        ExcelReaderOption.Builder.build(2, 0));
                testPoiReadObjects.forEach(x -> System.out.println(x.getTitle()));
                Assert.assertEquals("title1", testPoiReadObjects.get(0).getTitle());
            } catch (ExcelReaderException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println(String.format("%s row에서 의 문제가 발생", e.getRow().getRowNum()));
            }
        }

    }
}