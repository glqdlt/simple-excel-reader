package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
    public void create_() {
        SimpleExcelReader simpleExcelReader = new SimpleExcelReader();
        Path path = Paths.get("tmp.xls");
        List<SomeObject> dummy = IntStream.rangeClosed(0, 100)
                .boxed()
                .map(x -> {
                    SomeObject someObject = new SomeObject();
                    someObject.setFirst(x + "____");
                    someObject.setSecond(new Date());
                    return someObject;
                })
                .collect(Collectors.toList());
        try (OutputStream dd = Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.DELETE_ON_CLOSE)) {
            simpleExcelReader.create(dd, (x) -> {
                Sheet qq = x.createSheet();
                Row header = qq.createRow(0);

                Cell h_f = header.createCell(0);
                h_f.setCellValue("첫번째");

                Cell h_f_2 = header.createCell(1);
                h_f_2.setCellValue("두번째");

                int i = 1;
                for (SomeObject o : dummy) {
                    Row s = qq.createRow(i);

                    Cell s_f = s.createCell(0);
                    s_f.setCellValue(o.getFirst());

                    Cell s_f_2 = s.createCell(1);
                    s_f_2.setCellValue(o.getSecond());
                    i++;
                }
                return x;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}