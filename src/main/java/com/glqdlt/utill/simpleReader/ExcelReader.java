package com.glqdlt.utill.simpleReader;

import java.io.File;
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
    <T> Path make(List<T> data, Class<T> type);

    /**
     * @param file 엑셀로 저장할 파일
     * @param data 엑셀로 만들 객체들
     * @param type 매핑 될 객체 타입
     * @param <T>  매핑 될 객체 타입
     * @return 엑셀이 저장 된 Path 반환
     */
    <T> Path make(File file, List<T> data, Class<T> type);

    /**
     * @param sheetNumber 읽을 sheet 번호
     * @param stream 읽을 엑셀 스트림, ExcelReader 는 스트림을 직접 닫지 않습니다.지
     * @param type   매핑 될 객체 타입
     * @param <T>    매핑 될 객체 타입
     * @return 엑셀 데이터가 매핑 되어 반환 됨
     */
    <T> List<T> read(Integer sheetNumber, InputStream stream, Class<T> type);

    /**
     * @param stream 읽을 엑셀 스트림, ExcelReader 는 스트림을 직접 닫지 않습니다.지
     * @param type   매핑 될 객체 타입
     * @param <T>    매핑 될 객체 타입
     * @return 엑셀 데이터가 매핑 되어 반환 됨
     */
    <T> Map<Integer, List<T>> readAll(InputStream stream, Class<T> type);

}
