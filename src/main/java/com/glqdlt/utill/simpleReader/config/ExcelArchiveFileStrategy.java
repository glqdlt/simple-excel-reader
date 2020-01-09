package com.glqdlt.utill.simpleReader.config;

/**
 * @author Jhun
 * 2020-01-09
 */
public interface ExcelArchiveFileStrategy extends ArchiveFileStrategy {
    @Override
    default String getFileExtends() {
        return ".xslx";
    }
}
