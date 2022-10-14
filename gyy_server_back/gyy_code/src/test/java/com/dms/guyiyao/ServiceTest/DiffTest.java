package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.mapper.TsBookDifferentMapper;
import com.dms.guyiyao.service.Impl.TsBookDifferentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DiffTest {
@Autowired
    TsBookDifferentServiceImpl bookDifferentService;
    @Test
    public void test_1(){
        bookDifferentService.addDiff("太子右赞善大夫臣高保衡、尚书","测试一下","002000","002002");

}
@Test
    public void  test_2(){
    System.out.print( bookDifferentService.getDiff("002004002"));
}
@Test
    public  void  test_3(){
    System.out.println(bookDifferentService.deleteDiff("06d70182a1c911ec98b700163e30620d"));
}
@Test
    public  void test_4(){
    System.out.println(bookDifferentService.updateDiff("d484b176a43811ec98b700163e30620d","千金","千金的测试"));
}
@Test
    public  void test_5(){
    System.out.println(bookDifferentService.getHighlight("0fbad8d2ad2411ec98b700163e30620d"));
}
}
