package com.dms.guyiyao.daoTest;

import com.dms.guyiyao.mapper.TsBookChapterMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChapterTest {
    @Autowired
    TsBookChapterMapper bookChapterMapper;
    @Test
    public void test(){
        System.out.println(bookChapterMapper.getChapterNode("备急千金要方(宋校版)").size());
    }
}
