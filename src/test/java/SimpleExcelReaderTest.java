import callback.ReadWithResultHandler;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class SimpleExcelReaderTest {

    @Test
    public void readWithResult() throws IOException {

        ReadWithResultHandler<TestPoiReadObject> readWithResultHandler = (row) -> {
            String author = row.getCell(3).getStringCellValue();
            String title = row.getCell(4).getStringCellValue();
            TestPoiReadObject testPoiReadObject = new TestPoiReadObject();
            testPoiReadObject.setTitle(title);
            testPoiReadObject.setAuthor(author);
            return testPoiReadObject;

        };


        try (FileInputStream inputStream = new FileInputStream(new File("src/test/resources/test-data.xlsx"))) {
            SimpleExcelReader simpleExcelReader = new SimpleExcelReader();
            try {
            List<TestPoiReadObject> ss = (List<TestPoiReadObject>) simpleExcelReader.readAndResultArray(inputStream, readWithResultHandler, 0, 1);
            ss.forEach(x -> System.out.println(x.getAuthor()));
            } catch (ExcelReaderException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println(e.getRow().getRowNum()+" row에서 의 문제가 발생");
            }
        }

    }
}