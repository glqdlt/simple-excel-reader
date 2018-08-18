import callback.ConsumeHandler;
import callback.ReadWithResultHandler;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SimpleExcelReader {

    private XSSFSheet generatedSheet(InputStream is, Integer sheetNum) throws IOException {
        XSSFWorkbook sheets = new XSSFWorkbook(is);
        XSSFSheet sheet = sheets.getSheetAt(sheetNum);
        return sheet;
    }

    public void consume(InputStream is, ConsumeHandler<?> consumeHandler, Integer sheetNum, Integer rowNum) {

        try {
            XSSFSheet sheet = generatedSheet(is, sheetNum);

            for (Row row : sheet) {
                if (row.getRowNum() > rowNum) {
                    try {
                        consumeHandler.read(row);
                    } catch (NullPointerException e) {
                        throw new ExcelReaderException(row.getRowNum() + " row's cells is Null !", e, row);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<?> readAndResultArray(InputStream is, ReadWithResultHandler<?> readWithResultHandler, Integer sheetNum, Integer rowNum) {

        List<Object> result = new ArrayList<>();
        try {
            XSSFSheet sheet = generatedSheet(is, sheetNum);

            for (Row row : sheet) {
                if (row.getRowNum() > rowNum) {
                    try {
                        Object obj = readWithResultHandler.read(row);
                        result.add(obj);
                    } catch (NullPointerException e) {
                        throw new ExcelReaderException(row.getRowNum() + " row's cells is Null !", e, row);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
}