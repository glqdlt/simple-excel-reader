package com.glqdlt.utill.simpleReader;

import com.glqdlt.utill.simpleReader.annotation.ExcelMakeOption;
import com.glqdlt.utill.simpleReader.annotation.ExcelReadOption;
import com.glqdlt.utill.simpleReader.config.ArchiveConfig;
import com.glqdlt.utill.simpleReader.config.ArchiveOption;
import com.glqdlt.utill.simpleReader.config.ExcelArchiveFileStrategys;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jhun
 * 2020-01-09
 */
public class SimpleArchiveExcelReader implements ExcelReader {

    private static class FieldAndMethod {
        public FieldAndMethod(Field f, Method m) {
            this.f = f;
            this.m = m;
        }

        private Field f;
        private Method m;

        public Field getF() {
            return f;
        }

        public Method getM() {
            return m;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(SimpleArchiveExcelReader.class);

    private final ArchiveOption readOption;
    private List<DateTimeFormatter> dateTimeParser;

    private ArchiveOption getReadOption() {
        return readOption;
    }


    public SimpleArchiveExcelReader(ArchiveConfig config) {
        this.readOption = config.getReadOption();
        this.dateTimeParser = config.getDateTimeParser();
    }

    public SimpleArchiveExcelReader() {
        this.readOption = new ArchiveOption(false, null);
        this.dateTimeParser = new LinkedList<>();
    }

    @Override
    public <T> File make(List<T> data, Class<T> type) {
        return make(ExcelArchiveFileStrategys.SIMPLE_ARCHIVE_STRAEGY.getArchiveFile(), data, type);
    }

    @Override
    public <T> File make(File file, List<T> data, Class<T> type) {
        if (data.size() < 1) {
            log.debug("maybe data is empty");
        }
        if (file.isDirectory()) {
            throw new ExcelException(String.format("%s is not file", file.getAbsolutePath()));
        }
        try (OutputStream outputStream = Files.newOutputStream(
                file.toPath(),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            Workbook wb = new XSSFWorkbook();
            mappingToObjectInnerWorkbook(type, data, wb);
            wb.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private <T> Workbook mappingToObjectInnerWorkbook(Class<T> tt, List<T> list, Workbook workbook) {
        Sheet sheet = workbook.createSheet();

        Field[] fields = tt.getDeclaredFields();
        if (fields.length < 1) {
            throw new RuntimeException("Field is not definition! length 0");
        }
        Method[] methods = tt.getDeclaredMethods();

        List<LogReplication> jobs = Stream.of(fields)
                .filter(field -> {
                    Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
                    for (Annotation a : declaredAnnotations) {
                        if (a instanceof ExcelMakeOption) {
                            if (((ExcelMakeOption) a).ignore()) {
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
                        if (x instanceof ExcelMakeOption && !((ExcelMakeOption) x).customGetMethodPrefix().equals("")) {
                            ExcelMakeOption anno = ((ExcelMakeOption) x);
                            String prefix = anno.customGetMethodPrefix();
                            String _customMethodName = (prefix + _f).toLowerCase();
                            method = Stream.of(methods).filter(yy -> yy.getName().toLowerCase().equals(_customMethodName)).findAny()
                                    .orElseThrow(() -> new RuntimeException("Not Found Method"));

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
                            .filter(x -> x instanceof ExcelMakeOption)
                            .map(x -> {
                                ExcelMakeOption aa = ((ExcelMakeOption) x);
                                return aa.customColumnName();
                            })
                            .filter(x -> !x.equals(""))
                            .findAny().orElse(_f);

                    return new LogReplication(field, mm, customHeadName);
                }).collect(Collectors.toList());

        for (
                int cellPosition = 0; cellPosition < jobs.size(); cellPosition++) {
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
                    log.error(e.getMessage(), e);
                }
            }
        }
        return workbook;
    }


    public XSSFSheet getSheet(InputStream is, Integer sheetNum) throws IOException {
        final XSSFWorkbook workbook = new XSSFWorkbook(is);
        return workbook.getSheetAt(sheetNum);
    }


    public String makeMethodName(String prefix, String fileName) {
        final String first = fileName.substring(0, 1).toUpperCase();
        final String last = fileName.substring(1);
        return prefix + first + last;
    }

    @FunctionalInterface
    interface ColumnSupportChecker {
        default boolean resolve(Integer cellType) {
            Integer[] supportType = getSupportType();
            for (Integer type : supportType) {
                if (type.equals(cellType)) {
                    return true;
                }
            }
            return false;
        }

        Integer[] getSupportType();

    }

    private static ColumnSupportChecker STRING_COLUMN_CHECKER = () -> new Integer[]{Cell.CELL_TYPE_STRING};
    private static ColumnSupportChecker DATE_COLUMN_CHECKER = () -> new Integer[]{Cell.CELL_TYPE_STRING};
    private static ColumnSupportChecker NUMBER_COLUMN_CHECKER = () -> new Integer[]{Cell.CELL_TYPE_NUMERIC};


    public <T> List<ExcelMappingResult<T>> mappingObject(Sheet sheet, Class<T> type) {
        try {
            final Iterator<Row> rows = sheet.rowIterator();
            final List<Field> fields = checkFields(type);
            final List<FieldAndMethod> fieldAndMethods = extractSetMethods(type.getMethods(), fields);
            final List<ExcelMappingResult<T>> result = new ArrayList<>();
            while (rows.hasNext()) {
                Row row = rows.next();
                T obj = type.newInstance();
                int i = 0;
                Integer rowNumb = row.getRowNum() + 1;
                for (FieldAndMethod fm : fieldAndMethods) {
                    int columnNumb = i + 1;

                    Cell cell = row.getCell(i);
                    if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                        continue;
                    }
                    Class<?> fType = fm.getF().getType();
                    final Object data;
                    try {
                        final Integer cellType = cell.getCellType();
                        if (fType.isAssignableFrom(String.class) && STRING_COLUMN_CHECKER.resolve(cellType)) {
                            data = cell.getStringCellValue();
                        } else if (fType.isAssignableFrom(Integer.class) && NUMBER_COLUMN_CHECKER.resolve(cellType)) {
                            data = (int) cell.getNumericCellValue();
                        } else if (fType.isAssignableFrom(Long.class) && NUMBER_COLUMN_CHECKER.resolve(cellType)) {
                            data = (long) cell.getNumericCellValue();
                        } else if (fType.isAssignableFrom(Double.class) && NUMBER_COLUMN_CHECKER.resolve(cellType)) {
                            data = cell.getNumericCellValue();
                        } else if (fType.isAssignableFrom(Date.class) && DATE_COLUMN_CHECKER.resolve(cellType)) {
                            data = parsingStringToDate(cell.getStringCellValue());
                        } else if (fType.isAssignableFrom(LocalDateTime.class) && DATE_COLUMN_CHECKER.resolve(cellType)) {
                            data = parsingStringToLocalDateTime(cell.getStringCellValue());
                        } else {
                            throw new ExcelException(String.format("Not Supported Field Type '%s'", fType.getTypeName()));
                        }
                        fm.getM().invoke(obj, data);
                    } catch (IllegalStateException | IllegalArgumentException | InvocationTargetException e) {
                        throw new ExcelException(
                                String.format("%s row, %s cell mapping to field name '%s' fail ... cell data is '%s'. expected data type '%s', but is it not.",
                                        rowNumb,
                                        columnNumb,
                                        fm.getF().getName(),
                                        cell,
                                        fType.getTypeName()
                                ), e);
                    }
                    i++;
                }
                result.add(new ExcelMappingDataWithRowNumber<>(rowNumb, obj));
            }
            return result;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public Date parsingStringToDate(String s) {
        LocalDateTime dateTime = parsingStringToLocalDateTime(s);
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime parsingStringToLocalDateTime(String s) {
        for (DateTimeFormatter f : this.dateTimeParser) {
            try {
                return LocalDateTime.parse(s, f);
            } catch (DateTimeParseException e) {
                log.warn(e.getMessage());
            }
        }
        log.warn("'{}' datetime parsing fail, not matched formatter.. skip this cell data.", s);
        return null;
    }

    public List<FieldAndMethod> extractSetMethods(Method[] methods, List<Field> fields) {
        final List<FieldAndMethod> findMethod = new ArrayList<>();
        final List<Method> methodRaws = Stream.of(methods).collect(Collectors.toList());
        for (Field field : fields) {
            final String expected = this.makeMethodName("set", field.getName());
            Method setMethod = methodRaws.stream()
                    .filter(x -> x.getName().equals(expected)).findAny().orElseThrow(() -> new ExcelException(String.format("not found public method '%s'", expected)));
            findMethod.add(new FieldAndMethod(field, setMethod));
        }
        return findMethod;
    }

    public List<Field> checkFields(Class type) {
        final Field[] fields = type.getDeclaredFields();
        final List<Field> result = new ArrayList<>();
        for (Field f : fields) {
            Annotation[] annotations = f.getAnnotations();
            boolean isSkip = Stream.of(annotations).anyMatch(x -> {
                if (x instanceof ExcelReadOption) {
                    ExcelReadOption aaaa = ((ExcelReadOption) x);
                    return aaaa.ignore();
                }
                return false;
            });

            if (!isSkip) {
                result.add(f);
            }
        }
        return result;
    }


    @Override
    public <T> List<ExcelMappingResult<T>> read(Integer sheetNumber, InputStream stream, Class<T> type) throws IOException {
        if (getReadOption().getArchive()) {
            File f = getReadOption().getArchiveFile();
            try (FileInputStream ts = new FileInputStream(f)) {
                final XSSFWorkbook workbook = new XSSFWorkbook(ts);
                XSSFSheet sheet = workbook.getSheetAt(sheetNumber);
                return mappingObject(sheet, type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            final XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);
            return mappingObject(sheet, type);
        }

    }

    @Override
    public <T> Map<Integer, List<ExcelMappingResult<T>>> readAll(InputStream stream, Class<T> type) throws IOException {
        if (getReadOption().getArchive()) {
            File f = getReadOption().getArchiveFile();
            try (FileInputStream ts = new FileInputStream(f)) {
                return mappingToMap(ts, type);
            }
        } else {
            return mappingToMap(stream, type);
        }
    }

    private <T> Map<Integer, List<ExcelMappingResult<T>>> mappingToMap(InputStream stream, Class<T> type) throws IOException {
        final XSSFWorkbook workbook = new XSSFWorkbook(stream);
        Map<Integer, List<ExcelMappingResult<T>>> map = new HashMap<>();
        Iterator<Sheet> sheetI = workbook.sheetIterator();
        int i = 0;
        while (sheetI.hasNext()) {
            int sheetNumb = i + 1;
            Sheet sheet = sheetI.next();
            List<ExcelMappingResult<T>> mappingResults = mappingObject(sheet, type);
            map.put(sheetNumb, mappingResults);
            i++;
        }
        return map;
    }

}
