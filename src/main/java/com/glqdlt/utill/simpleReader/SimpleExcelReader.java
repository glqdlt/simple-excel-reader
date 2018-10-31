package com.glqdlt.utill.simpleReader;

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

    public void consume(InputStream is, ConsumeHandler<?> consumeHandler, ExcelReaderOption options) {

        try {
            XSSFSheet sheet = generatedSheet(is, options.getSheetNum());

            for (Row row : sheet) {
                if (row.getRowNum() > options.getRowNum()) {
                    try {
                        consumeHandler.read(row);
                    } catch (NullPointerException e) {
                        throw new SimpleExcelReaderException(e, row);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public <T> List<T> readAndResultArray(InputStream is, ReadWithResultHandler<T> readWithResultHandler, ExcelReaderOption options) {

        List<T> result = new ArrayList<>();
        try {
            XSSFSheet sheet = generatedSheet(is, options.getSheetNum());

            for (Row row : sheet) {
                if (row.getRowNum() >= options.getRowNum()) {
                    try {
                        T obj = readWithResultHandler.read(row);
                        result.add(obj);
                    } catch (NullPointerException e) {
                        throw new SimpleExcelReaderException(e, row);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
}