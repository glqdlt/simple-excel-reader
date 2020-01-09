package com.glqdlt.utill.simpleReader.config;

/**
 * @author Jhun
 * 2020-01-09
 */
public class ExcelArchiveFileStrategys {
    public static ExcelArchiveFileStrategy SIMPLE_ARCHIVE_STRAEGY = new ExcelArchiveFileStrategy() {
        @Override
        public DirectoryGenerateStrategy getDirectory() {
            return DirectoryGenerateStrategy.DEFAULT_TEMP_DIRECTORY;
        }

        @Override
        public FileNameGenerateStrategy getFileNameGenerator() {
            return FileNameGenerateStrategy.DEFAULT_GENERATOR;
        }
    };
}
