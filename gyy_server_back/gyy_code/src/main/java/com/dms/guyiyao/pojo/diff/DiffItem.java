package com.dms.guyiyao.pojo.diff;

import lombok.Data;

@Data
public class DiffItem {
    private String id;
private String bookFrom;
private  String chapterFrom;
private  String diffFrom;

private  String bookTo;
private String chapterTo;
private  String diffTo;
}
