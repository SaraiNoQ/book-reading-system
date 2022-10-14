package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.service.ITsBookContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ContentTest {
    @Autowired
    ITsBookContentService bookContentService;
    @Test
    public void test1(){
        System.out.println(bookContentService.getContent("002000"));
    }
    @Test
    public void test2(){
        System.out.println(bookContentService.addContent("test2","test2",1,"000"));
    }
    @Test
    public void test3(){
        System.out.println(bookContentService.deleteContent("00"));
    }
    @Test
    public void test4(){
        System.out.println(bookContentService.updateContent("00",1,"test4","1"));
    }
@Test
public  void test_5() throws IOException {
    bookContentService.getContentDb(null,"1a4e1c0eef7511ec8c960050568c49e0");
}
}
