package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jhun
 */
public class SimpleExcelReader {
    private <T> Workbook mappingToObjectInnerWorkbook(List<T> list, Workbook workbook) {
        Sheet sheet = workbook.createSheet();

        Class targetClass = list.get(0).getClass();

        Field[] fields = targetClass.getDeclaredFields();
        if (fields.length < 1) {
            throw new RuntimeException("Field is not definition! length 0");
        }
        Method[] methods = targetClass.getDeclaredMethods();

        List<LogReplication> jobs = Stream.of(fields)
                .filter(field -> {
                    Annotation[] annos = field.getDeclaredAnnotations();
                    for (Annotation a : annos) {
                        if (a instanceof SimpleReaderOption) {
                            if (((SimpleReaderOption) a).skipField()) {
                                return false;
                            }
                        }
                    }
                    return true;
                }).map(field -> {
                    String _f = field.getName();
                    Annotation[] annotations = field.getDeclaredAnnotations();
                    Method mm = Stream.of(annotations).map(x -> {
                        Method method;
                        if (x instanceof SimpleReaderOption) {
                            SimpleReaderOption aa = ((SimpleReaderOption) x);
                            String prefix = aa.getMethodPrefix();
                            if (!prefix.equals("get")) {
                                String _customMethodName = (prefix + _f).toLowerCase();
                                method = Stream.of(methods)
                                        .filter(_m -> {
                                            String _methodName = _m.getName().toLowerCase();
                                            return _methodName.equals(_customMethodName);
                                        }).findAny().orElseThrow(() -> new RuntimeException(String.format("NotFounded Method Name %s", _customMethodName)));
                            } else {
                                String _getMethodName = ("get" + _f).toLowerCase();
                                String _isMethodName = ("is" + _f).toLowerCase();
                                method = Stream.of(methods)
                                        .filter(_m -> {
                                            String _methodName = _m.getName().toLowerCase();
                                            return _methodName.equals(_getMethodName) || _methodName.equals(_isMethodName);
                                        })
                                        .findAny().orElseThrow(() -> new RuntimeException("NotFounded Method get() or is()"));
                            }
                        } else {
                            String _getMethodName = ("get" + _f).toLowerCase();
                            String _isMethodName = ("is" + _f).toLowerCase();
                            method = Stream.of(methods)
                                    .filter(_m -> {
                                        String _methodName = _m.getName().toLowerCase();
                                        return _methodName.equals(_getMethodName) || _methodName.equals(_isMethodName);
                                    })
                                    .findAny().orElseThrow(() -> new RuntimeException("NotFounded Method get() or is()"));
                        }
                        return method;
                    }).findAny().orElseThrow(() -> new RuntimeException("NotFounded Method!"));

                    String customHeadName = Stream.of(annotations)
                            .filter(x -> x instanceof SimpleReaderOption)
                            .map(x -> {
                                SimpleReaderOption aa = ((SimpleReaderOption) x);
                                return aa.customColumnName();
                            })
                            .filter(x -> !x.equals(""))
                            .findAny().orElse(_f);

                    return new LogReplication(field, mm, customHeadName);
                })
                .collect(Collectors.toList());

        for (int cellPosition = 0; cellPosition < jobs.size(); cellPosition++) {
            LogReplication job = jobs.get(cellPosition);
            String key = job.getHeadName();
            Row header = sheet.getRow(0);
            if (header == null) {
                header = sheet.createRow(0);
            }
            Cell header_cell = header.createCell(cellPosition);
            header_cell.setCellValue(key);
            for (int rowPosition = 0; rowPosition < list.size(); rowPosition++) {
                int index = rowPosition + 1;
                Row body = sheet.getRow(index);
                if (body == null) {
                    body = sheet.createRow(index);
                }
                Cell body_cell = body.createCell(cellPosition);
                try {
                    Field f = job.field;
                    Class<?> type = f.getType();
                    boolean isDate = type.isAssignableFrom(Date.class);
                    boolean isInt = type.isAssignableFrom(int.class);
                    boolean isInteger = type.isAssignableFrom(Integer.class);
                    boolean isLongObject = type.isAssignableFrom(Long.class);
                    boolean isLong = type.isAssignableFrom(long.class);

                    Object _rawData = job.method.invoke(list.get(rowPosition));

                    if (job.getMethod().getName().contains("parsingGet")) {
                        if (_rawData == null) {
                            body_cell.setCellValue("");
                        } else {
                            body_cell.setCellValue(String.valueOf(_rawData));
                        }
                    } else {
                        if (isDate) {
                            String _dString;
                            if (_rawData == null) {
                                _dString = "";
                            } else {
                                Date _d = (Date) _rawData;
                                _dString = DateUtils.asLocalDateTime(_d).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            }
                            body_cell.setCellValue(_dString);
                        } else if (isLong || isLongObject) {
                            if (_rawData == null || _rawData.equals(0L)) {
                                body_cell.setCellValue(0);
                            } else {
                                body_cell.setCellValue(Long.parseLong(String.valueOf(_rawData)));
                            }
                            body_cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        } else if (isInteger || isInt) {
                            if (_rawData == null || _rawData.equals(0)) {
                                body_cell.setCellValue(0);
                            } else {
                                body_cell.setCellValue(Integer.parseInt(String.valueOf(_rawData)));
                            }
                            body_cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        } else {
                            if (_rawData == null) {
                                body_cell.setCellValue("");
                            } else {
                                body_cell.setCellValue(String.valueOf(_rawData));
                            }
                        }
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return workbook;
    }


    public XSSFSheet getSheet(InputStream is, Integer sheetNum) throws IOException {
        XSSFWorkbook sheets = new XSSFWorkbook(is);
        return sheets.getSheetAt(sheetNum);
    }

    public <T> void create(File file, List<T> list) throws IOException {
        if (list.size() < 1) {
            throw new RuntimeException("list size is 0!");
        }
        try (OutputStream os = Files.newOutputStream(
                file.toPath(),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            Workbook rr = mappingToObjectInnerWorkbook(list, new XSSFWorkbook());
            rr.write(os);
        }
    }

    public void consume(InputStream is, ConsumeHandler handler, ExcelReaderOption options) {
        try {
            XSSFSheet sheet = getSheet(is, options.getSheetNum());

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
            XSSFSheet sheet = getSheet(is, options.getSheetNum());

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

            if (options.isSave()) {
                create(new File(options.getPath()), result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }


    public <T> List<T> read(InputStream is, ReadHandler<T> handler, ExcelReaderOption options) {

        List<T> result = new ArrayList<>();
        try {
            XSSFSheet sheet = getSheet(is, options.getSheetNum());

            for (Row row : sheet) {
                if (row.getRowNum() >= options.getRowNum()) {
                    T obj = handler.read(row);
                    result.add(obj);
                }
            }

            if (options.isSave()) {
                create(new File(options.getPath()), result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }
}