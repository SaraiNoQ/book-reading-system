package com.dms.guyiyao.pojo.chapter;

import lombok.Data;

@Data
public class Chapter_db {
            String chapter_id;
            String chapter_id_parent;
            String bookName;
        String chapter_name;
        int grade ;
        int sequence;
        String opTime;
        String opUser;
}
