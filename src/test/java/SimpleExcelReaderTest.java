import callback.ReadWithResultHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
                simpleExcelReader.consume(inputStream,(row) -> {
                    if(row.getRowNum() != 5){

                        TestPoiReadObject t = new TestPoiReadObject();
                        t.setAuthor(row.getCell(3).getStringCellValue());
                        t.setTitle(row.getCell(4).getStringCellValue());
                        testPoiReadObjects.add(t);

                    }

                },0,0);
            } catch (ExcelReaderException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println(e.getRow().getRowNum()+" row에서 의 문제가 발생");
            }
            testPoiReadObjects.forEach( x-> System.out.println(x.getTitle()));
            Assert.assertEquals("title",testPoiReadObjects.get(0).getTitle());
    }
    }

    @Test
    public void readWithResult() throws IOException {

        try (FileInputStream inputStream = new FileInputStream(new File("src/test/resources/test-data.xlsx"))) {
            SimpleExcelReader simpleExcelReader = new SimpleExcelReader();
            try {
            List<TestPoiReadObject> testPoiReadObjects = simpleExcelReader.readAndResultArray(inputStream,
                    (row) -> {
                    String author = row.getCell(3).getStringCellValue();
                    String title = row.getCell(4).getStringCellValue();
                    TestPoiReadObject testPoiReadObject = new TestPoiReadObject();
                    testPoiReadObject.setTitle(title);
                   testPoiReadObject.setAuthor(author);
                    return testPoiReadObject;

                }
                    , 0, 0);
            testPoiReadObjects.forEach(x -> System.out.println(x.getTitle()));
            Assert.assertEquals("title",testPoiReadObjects.get(0).getTitle());
            } catch (ExcelReaderException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println(e.getRow().getRowNum()+" row에서 의 문제가 발생");
            }
        }

    }
}