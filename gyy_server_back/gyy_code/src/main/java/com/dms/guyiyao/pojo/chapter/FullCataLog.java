package com.dms.guyiyao.pojo.chapter;

import lombok.Data;

@Data
public class FullCataLog {
  private   String bookname;
  private  String parentid;
  private  String chaptername;
  public FullCataLog(){
    bookname="";
    parentid="";
    chaptername="";
  }
}
