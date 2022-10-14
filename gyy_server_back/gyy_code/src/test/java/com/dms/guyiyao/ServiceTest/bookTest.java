package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.service.ITsBookOrginalService;
import com.dms.guyiyao.service.ITsBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@SpringBootTest
public class bookTest {
@Autowired
    ITsBookService bookService;
@Autowired
    ITsBookOrginalService bookOrginalService;
@Test
public void getChapter(){
    System.out.println(bookOrginalService.getPicUrl("002002"));
}

    @Test
    public void getChapter_(){
            System.out.println(bookOrginalService.deleteImgUrl("asdasda44433455","bjqjyf\\bjqjyfx_2.png"));
    }
    @Test
    public void deleteBook(){
        System.out.println(bookService.deleteBook("2d47fb5a8f3a11ec98b700163e30620d"));
    }

@Test
  public void getBookList(){
    System.out.println(bookService.getBookList(0,10));
}
}