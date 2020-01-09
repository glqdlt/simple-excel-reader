package com.glqdlt.utill.simpleReader;

import org.apache.poi.ss.usermodel.Workbook;
/**
 * @author Jhun
 */
@FunctionalInterface
public interface ObjectMappingHandler {
    Workbook mapping(Workbook workbook);
}
