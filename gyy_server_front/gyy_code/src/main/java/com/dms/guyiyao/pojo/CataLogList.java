package com.dms.guyiyao.pojo;

import lombok.Data;

import java.util.List;
@Data
public class CataLogList {
    int grade;
    String val;
    List<CataLogList>list;
   public CataLogList(int grade,String val,List<CataLogList>list){
    this.grade=grade;
    this.val=val;
    this.list=list;
}
    public CataLogList(int grade,String val) {
        this.grade = grade;
        this.val = val;
    }
}
