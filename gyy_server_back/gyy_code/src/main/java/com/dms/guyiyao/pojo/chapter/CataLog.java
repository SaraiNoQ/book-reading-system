package com.dms.guyiyao.pojo.chapter;

import lombok.Data;

import java.util.List;
    @Data
    public class CataLog {
        private     String  id;
        private  String parentid;
        private  int grade;
        private  String sequence;
        private  String chapter;
        List<CataLog> cataLog;


    }

