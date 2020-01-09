package com.glqdlt.utill.simpleReader.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Jhun
 * 2020-01-09
 */
public interface FileNameGenerateStrategy {
    FileNameGenerateStrategy DEFAULT_GENERATOR = () -> LocalDateTime.now(ZoneId.systemDefault()).format((DateTimeFormatter.ofPattern("yyyy_MM_dd_HH:mm_ss")));

    String getExcelNamePrefix();
}
