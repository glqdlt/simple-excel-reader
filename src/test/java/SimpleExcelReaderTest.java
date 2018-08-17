import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleExcelReaderTest {

    @Test
    public void constructed() {

        SimpleExcelReader simpleExcelReader = new SimpleExcelReader();
        simpleExcelReader.read();

    }
}