package com.glqdlt.utill.simpleReader;

import com.glqdlt.utill.simpleReader.callback.SimpleReaderCallBack;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SimpleExcelReader  {

    /**
     * @param is
     * @param readRowStartCount
     * @param simpleReaderCallBack
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> List<T> read(InputStream is, Integer readRowStartCount, SimpleReaderCallBack simpleReaderCallBack) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        Sheet firstSheet = workbook.getSheetAt(0);
        List<T> result = new ArrayList<>();

        Integer i = 0;
        for (Row row : firstSheet) {
            i++;
            if (i >= readRowStartCount) {
                try {
                    result.add((T) simpleReaderCallBack.exec(row));
                }catch(NullPointerException e){
                    throw  new SimpleExcelReaderException(e,i);
                }
            }
        }
        return result;
    }

}
