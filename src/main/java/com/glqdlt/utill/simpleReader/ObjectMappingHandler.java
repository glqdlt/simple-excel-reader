package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Workbook;

@FunctionalInterface
public interface ObjectMappingHandler {
    public Workbook mapping(Workbook workbook);
}
