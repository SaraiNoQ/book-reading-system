package com.dms.guyiyao.pojo.original;

import lombok.Data;

import java.util.List;

@Data
public class OriginalReturn {
private  int sequence;
private List<ContentOriginal> data;
private int count;
}
