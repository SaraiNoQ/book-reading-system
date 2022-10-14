package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.service.ITsBookChapterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChapterTest {
@Autowired
    ITsBookChapterService bookChapterService;
    @Test
    public void test1(){
        System.out.println(bookChapterService.updateChapter("002000","002","新校备急千金要方序1",0,"1"));
}
    @Test
    public void test2(){
        System.out.println(bookChapterService.delete("002000"));
    }
    @Test
    public void test3(){
        System.out.println(bookChapterService.addChapter("1222","123","test_tt",1,"0"));
    }
    @Test
    public  void  test_4(){
//        System.out.println(bookChapterService.getChapter(""));
    }
}
