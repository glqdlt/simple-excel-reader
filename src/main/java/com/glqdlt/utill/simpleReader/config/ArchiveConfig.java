package com.glqdlt.utill.simpleReader.config;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jhun
 * 2020-01-09
 */
public class ArchiveConfig {
    private ArchiveOption readOption;
    private ArchiveOption makeOption;
    private final List<DateTimeFormatter> dateTimeParser = new LinkedList<>();

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

    public void addDateTimeFormat(DateTimeFormatter formatter) {
        this.dateTimeParser.add(formatter);
    }

    public List<DateTimeFormatter> getDateTimeParser() {
        return dateTimeParser;
    }
}
