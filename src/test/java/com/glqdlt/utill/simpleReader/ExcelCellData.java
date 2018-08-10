package com.glqdlt.utill.simpleReader;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ExcelCellData {


    private Integer seq;
    private String regDate;
    private Integer rank;
    private String author;
    private String title;
}
