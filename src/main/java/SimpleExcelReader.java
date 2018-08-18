import callback.ReaderHandler;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class SimpleExcelReader {

    public void read(InputStream is, ReaderHandler readerHandler, Integer sheetNum) {

        try {
            XSSFWorkbook sheets = new XSSFWorkbook(is);

            XSSFSheet sheet = sheets.getSheetAt(sheetNum);

            for (Row row : sheet) {
                readerHandler.handler(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
