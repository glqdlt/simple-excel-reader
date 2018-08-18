import callback.ReaderHandler;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;


public class SimpleExcelReaderTest {

    @Test
    public void constructed() throws IOException {

        ReaderHandler<TestPoiReadObject> readerHandler = (row) -> {
            String author = row.getCell(3).getStringCellValue();
            String title = row.getCell(6).getStringCellValue();


            TestPoiReadObject testPoiReadObject = new TestPoiReadObject();
            testPoiReadObject.setTitle(title);
            testPoiReadObject.setAuthor(author);
            return testPoiReadObject;

        };


        try (FileInputStream inputStream = new FileInputStream(new File("src/test/resources/test-data.xlsx"))) {
            SimpleExcelReader simpleExcelReader = new SimpleExcelReader();
            try {
                simpleExcelReader.read(inputStream, readerHandler, 0, 1);
            } catch (ExcelReaderException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println(e.getRow().getRowNum()+" row에서 의 문제가 발생");
            }
        }

    }
}