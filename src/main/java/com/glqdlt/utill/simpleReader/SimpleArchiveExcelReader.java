package com.glqdlt.utill.simpleReader;

import com.glqdlt.utill.simpleReader.annotation.ExcelColumnIgnore;
import com.glqdlt.utill.simpleReader.config.ArchiveConfig;
import com.glqdlt.utill.simpleReader.config.ArchiveOption;
import com.glqdlt.utill.simpleReader.config.ExcelArchiveFileStrategys;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final ArchiveOption makeOption;

    private ArchiveOption getReadOption() {
        return readOption;
    }

    private ArchiveOption getMakeOption() {
        return makeOption;
    }

    public SimpleArchiveExcelReader(ArchiveConfig config) {
        this.readOption = config.getReadOption();
        this.makeOption = config.getMakeOption();
    }

    public SimpleArchiveExcelReader() {
        this.readOption = new ArchiveOption(false, null);
        this.makeOption = new ArchiveOption(false, null);

    }

    @Override
    public <T> Path make(List<T> data, Class<T> type) {
        return make(ExcelArchiveFileStrategys.SIMPLE_ARCHIVE_STRAEGY.getArchiveFile(), data, type);
    }

    @Override
    public <T> Path make(File file, List<T> data, Class<T> type) {
        if (data.size() < 1) {
            log.debug("maybe data is empty");
        }
        if (file.isDirectory()) {
            throw new ExcelException(String.format("%s is not file", file.getAbsolutePath()));
        }
        return null;
    }


    private <T> List<T> _read(Integer sheetNumber, InputStream stream, Class<T> type) {

        return null;
    }

//    public <T> T rowToObject(Sheet sheet, Class<T> type) throws IllegalAccessException, InstantiationException {
//        List<T> list = new LinkedList<>();
//        Iterator<Row> rows = sheet.rowIterator();
//        while (rows.hasNext()) {
//            Row row = rows.next();
//            T t = type.newInstance();
//            Field[] fields = type.getFields();
//            for (Field field : fields) {
//                Annotation[] a = field.getAnnotations();
//                boolean isIgnore = Stream.of(a).anyMatch(x -> x.getClass() == ExcelColumnIgnore.class);
//                if (isIgnore) {
//                    continue;
//                }
//                System.out.println("");
//            }
//        }
//
//    }

    public Method findMethod(Method[] methods, Field field) {
        final String callMethodName = this.makeMethodName("set", field.getName());
        return Stream.of(methods).filter(x -> {
            String methodName = x.getName();
            return methodName.equals(callMethodName);
        }).findAny().orElseThrow(() -> new ExcelException(String.format("Not Found Method '%s'", callMethodName)));
    }

    public String makeMethodName(String prefix, String fileName) {
        String first = fileName.substring(0, 1).toUpperCase();
        String last = fileName.substring(1);
        return prefix + first + last;
    }

    public <T> List<T> mappingObject(Sheet sheet, Class<T> type) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Iterator<Row> rows = sheet.rowIterator();
        List<Field> fields = checkFields(type);
        List<FieldAndMethod> methods = extractSetMethods(type.getMethods(), fields);
        List<T> result = new ArrayList<>();
        if (rows.hasNext()) {
            Row row = rows.next();
            T obj = type.newInstance();
            int i = 0;
            for (FieldAndMethod m : methods) {
                Cell cell = row.getCell(i);
                if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    continue;
                }
                Class<?> fType = m.getF().getType();
                Object data;
                if (fType.isAssignableFrom(String.class)) {
                    data = cell.getStringCellValue();
                } else if (fType.isAssignableFrom(Integer.class) || fType.isAssignableFrom(Long.class)) {
                    data = cell.getNumericCellValue();
                } else if (fType.isAssignableFrom(Date.class)) {
                    data = cell.getDateCellValue();
                } else if (fType.isAssignableFrom(LocalDateTime.class)) {
                    Date date = cell.getDateCellValue();
                    data = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                } else {
                    throw new ExcelException(String.format("Not Supported Field Type '%s'", fType.getTypeName()));
                }
                m.getM().invoke(obj, data);
                i++;
            }
            result.add(obj);
        }
        return result;
    }

    public List<FieldAndMethod> extractSetMethods(Method[] methods, List<Field> fields) {
        final List<FieldAndMethod> findMethod = new ArrayList<>();
        final List<Method> methodRaws = Stream.of(methods).collect(Collectors.toList());
        for (Field field : fields) {
            final String expected = this.makeMethodName("set", field.getName());
            Method setMethod = methodRaws.stream()
                    .filter(x -> x.getName().equals(expected)).findAny().orElseThrow(() -> new ExcelException(String.format("not found public method '%s'")));
            findMethod.add(new FieldAndMethod(field, setMethod));
        }
        return findMethod;
    }

    public List<Field> checkFields(Class type) {
        Field[] fields = type.getDeclaredFields();
        List<Field> result = new ArrayList<>();
        for (Field f : fields) {
            Annotation[] anno = f.getAnnotations();
            boolean isIgnore = Stream.of(anno).anyMatch(x -> x instanceof ExcelColumnIgnore);
            if (!isIgnore) {
                result.add(f);
            }
        }
        return result;
    }

    @Override
    public <T> List<T> read(Integer sheetNumber, InputStream stream, Class<T> type) {
//   TODO     추후에는 아카이빙 처리하는 건 로직 마지막으로 옮겨서 비동기로 뽑는 게 좋을거임
        if (getReadOption().getArchive()) {
            File f = getReadOption().getArchiveFile();
            try {
                FileUtils.copyInputStreamToFile(stream, f);
                try (FileInputStream is = new FileInputStream(f)) {
                    return _read(sheetNumber, is, type);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new ExcelException(e.getMessage());
            }
        }
        return _read(sheetNumber, stream, type);
    }

    @Override
    public <T> Map<Integer, List<T>> readAll(InputStream stream, Class<T> type) {
        return null;
    }

}
