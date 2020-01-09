package com.glqdlt.utill.simpleReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * @author Jhun
 * 2020-01-09
 */
public interface ExcelReader {
    /**
     * @param data 엑셀로 만들 객체들
     * @param type 매핑 될 객체 타입
     * @param <T>  매핑 될 객체 타입
     * @return 엑셀이 저장 된 Path 반환
     */
    <T> File make(List<T> data, Class<T> type);

    /**
     * @param file 엑셀로 저장할 파일
     * @param data 엑셀로 만들 객체들
     * @param type 매핑 될 객체 타입
     * @param <T>  매핑 될 객체 타입
     * @return 엑셀이 저장 된 Path 반환
     */
    <T> File make(File file, List<T> data, Class<T> type);

    /**
     * @param <T>         매핑 될 객체 타입
     * @param sheetNumber 읽을 sheet 번호
     * @param stream      읽을 엑셀 스트림, ExcelReader 는 스트림을 직접 닫지 않습니다.지
     * @param type        매핑 될 객체 타입
     * @return 엑셀 데이터가 매핑 되어 반환 됨
     */
    <T> List<ExcelMappingResult<T>> read(Integer sheetNumber, InputStream stream, Class<T> type) throws IOException;

    /**
     * @param <T>    매핑 될 객체 타입
     * @param stream 읽을 엑셀 스트림, ExcelReader 는 스트림을 직접 닫지 않습니다.지
     * @param type   매핑 될 객체 타입
     * @return 엑셀 데이터가 매핑 되어 반환 됨
     */
    <T> Map<Integer, List<ExcelMappingResult<T>>> readAll(InputStream stream, Class<T> type) throws IOException;

}
