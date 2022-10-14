package com.dms.guyiyao.daoTest;

import com.dms.guyiyao.mapper.TsBookContentMapper;
import com.dms.guyiyao.pojo.book.TsBookContentDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ContentDbTest {
@Autowired
    TsBookContentMapper mapper;
    @Test
    public void test(){
        for (TsBookContentDb tsBookContentDb : mapper.getContentDb("1a4e1c0eef7511ec8c960050568c49e0")) {
            System.out.println(tsBookContentDb);
        }
    
}
}
