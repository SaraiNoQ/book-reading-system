package com.dms.guyiyao.ServiceTest;


import com.dms.guyiyao.service.ITsBookContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;

@SpringBootTest
public class ContentTest {
@Autowired
ITsBookContentService bookContentService;
    @Test
    public  void test_1(){
        LinkedList<String[]> content_diff = bookContentService.getContent_diff("82e6ce42ad0911ec98b700163e30620d","孙真人千金方");
        for (String[] o : content_diff) {
            for (String s : o) {
                System.out.println(s+"\n");
            }
        }

    }
/*获取备急千金药方 《备急千金药方》序 下一章的内容 */
@Test
    public  void  test_2(){
//    System.out.println(bookContentService.getContent_Around("备急千金药方","《备急千金药方》序",1));

}
}
