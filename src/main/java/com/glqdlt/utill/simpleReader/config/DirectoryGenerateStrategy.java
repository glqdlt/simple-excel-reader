package com.glqdlt.utill.simpleReader.config;

import java.io.File;

/**
 * @author Jhun
 * 2020-01-09
 */
public interface DirectoryGenerateStrategy {
    DirectoryGenerateStrategy DEFAULT_TEMP_DIRECTORY =
            () -> new File(System.getenv("java.io.tmpdir") + File.separator + "excel").getAbsoluteFile();
    DirectoryGenerateStrategy DEFAULT_USER_HOME_DIRECTORY =
            () -> new File(System.getenv("user.home") + File.separator + "excel").getAbsoluteFile();

    File getDirectoryPath();
}
