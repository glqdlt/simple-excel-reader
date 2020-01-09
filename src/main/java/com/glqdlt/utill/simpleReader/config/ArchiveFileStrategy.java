package com.glqdlt.utill.simpleReader.config;

import java.io.File;

/**
 * @author Jhun
 * 2020-01-09
 */
public interface ArchiveFileStrategy {

    default File getArchiveFile() {
        final String extend;
        if (!getFileExtends().substring(0).equals(".")) {
            extend = "." + getFileExtends();
        } else {
            extend = getFileExtends();
        }
        final String filename = getFileNameGenerator() + extend;
        return new File(getDirectory() + File.separator + filename);
    }

    String getFileExtends();

    DirectoryGenerateStrategy getDirectory();

    FileNameGenerateStrategy getFileNameGenerator();
}
