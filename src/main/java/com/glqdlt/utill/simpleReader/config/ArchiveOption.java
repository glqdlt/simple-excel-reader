package com.glqdlt.utill.simpleReader.config;

import java.io.File;

/**
 * @author Jhun
 * 2020-01-09
 */
public class ArchiveOption {
    private Boolean archive;
    private ArchiveFileStrategy config;

    public ArchiveOption(Boolean archive, ArchiveFileStrategy config) {
        this.archive = archive;
        this.config = config;
    }

    public Boolean getArchive() {
        return archive;
    }

    public File getArchiveFile() {
        return this.config.getArchiveFile();
    }

}
