import callback.ReaderHandler;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class SimpleExcelReader {

    public void read(InputStream is, ReaderHandler readerHandler) {

        try {
            XSSFWorkbook sheets = new XSSFWorkbook(is);

            XSSFSheet sheet = sheets.getSheetAt(0);

            for (Row row : sheet) {
                readerHandler.handler(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
