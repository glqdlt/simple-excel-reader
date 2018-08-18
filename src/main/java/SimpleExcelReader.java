import callback.ReaderHandler;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class SimpleExcelReader {

    public void read(InputStream is, ReaderHandler readerHandler, Integer sheetNum, Integer rowNum) {

        try {
            XSSFWorkbook sheets = new XSSFWorkbook(is);

            XSSFSheet sheet = sheets.getSheetAt(sheetNum);

            for (Row row : sheet) {
                if (row.getRowNum() > rowNum) {
                    try {
                        Object object = readerHandler.handler(row);
                    } catch (NullPointerException e) {
                        throw new ExcelReaderException(row.getRowNum() + " Row's cell is Null", e, row);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}