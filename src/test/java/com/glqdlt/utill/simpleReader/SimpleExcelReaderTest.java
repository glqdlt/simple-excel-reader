package com.glqdlt.utill.simpleReader;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SimpleExcelReaderTest {

    private static class SomeObject {
        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public Date getSecond() {
            return second;
        }

        public void setSecond(Date second) {
            this.second = second;
        }

        private String first;
        private Date second;
    }


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

    @Test
    public void 읽고저장까지() {
        final String path = System.getProperty("user.dir") + File.separator + "temp.xlsx";

        try (FileInputStream inputStream = new FileInputStream(new File("src/test/resources/test-data.xlsx"))) {
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
                        ExcelReaderOption.Builder.build(2, 0, true, path));
                testPoiReadObjects.forEach(x -> System.out.println(x.getTitle()));
            } catch (ExcelReaderException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println(String.format("%s row에서 의 문제가 발생", e.getRow().getRowNum()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = new File(path);
        f.delete();
    }
}