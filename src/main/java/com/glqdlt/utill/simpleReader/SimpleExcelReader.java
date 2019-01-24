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

    public void consume(InputStream is, ConsumeHandler handler, ExcelReaderOption options) {

        try {
            XSSFSheet sheet = generatedSheet(is, options.getSheetNum());

            for (Row row : sheet) {
                if (row.getRowNum() > options.getRowNum()) {
                    try {
                        handler.consume(row);
                    } catch (NullPointerException e) {
                        throw new SimpleExcelReaderException(e, row);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public <T> List<ReadResult> readMatch(InputStream is, ReadMatchHandler<T> handler, ExcelReaderOption options) {

        List<ReadResult> result = new ArrayList<>();
        try {
            XSSFSheet sheet = generatedSheet(is, options.getSheetNum());

            for (Row row : sheet) {
                if (row.getRowNum() >= options.getRowNum()) {
                    try {
                        ReadResult obj = handler.read(row);
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


    public <T> List<T> read(InputStream is, ReadHandler<T> handler, ExcelReaderOption options) {

        List<T> result = new ArrayList<>();
        try {
            XSSFSheet sheet = generatedSheet(is, options.getSheetNum());

            for (Row row : sheet) {
                if (row.getRowNum() >= options.getRowNum()) {
                        T obj = handler.read(row);
                        result.add(obj);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
}