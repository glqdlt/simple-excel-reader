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

        ReaderHandler readerHandler = (row) -> {
            System.out.println(row.getRowNum());
            return null;
        };


        try (FileInputStream inputStream = new FileInputStream(new File("src/test/resources/test-data.xlsx"))) {
            SimpleExcelReader simpleExcelReader = new SimpleExcelReader();
            simpleExcelReader.read(inputStream, readerHandler, 0);

        }

    }
}