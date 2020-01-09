package com.glqdlt.utill.simpleReader.config;

/**
 * @author Jhun
 * 2020-01-09
 */
public class ArchiveConfig {
    private ArchiveOption readOption;
    private ArchiveOption makeOption;

    public ArchiveOption getReadOption() {
        return readOption;
    }

    public ArchiveOption getMakeOption() {
        return makeOption;
    }

    public void setReadOption(ArchiveOption readOption) {
        this.readOption = readOption;
    }

    public void setMakeOption(ArchiveOption makeOption) {
        this.makeOption = makeOption;
    }
}
