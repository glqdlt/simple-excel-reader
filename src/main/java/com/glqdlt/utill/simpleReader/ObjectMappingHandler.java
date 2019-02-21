package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Workbook;
/**
 * @author Jhun
 */
@FunctionalInterface
public interface ObjectMappingHandler {
    public Workbook mapping(Workbook workbook);
}
