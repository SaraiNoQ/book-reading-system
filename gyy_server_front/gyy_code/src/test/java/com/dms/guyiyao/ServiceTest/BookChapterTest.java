package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.pojo.CataLog;
import com.dms.guyiyao.pojo.CataLogList;
import com.dms.guyiyao.service.ITsBookChapterService;
import com.dms.guyiyao.service.ITsBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Stack;

@SpringBootTest
public class BookChapterTest {
@Autowired
    ITsBookChapterService bookChapterService;
@Test
    public  void  test_1() {
//   List<CataLog> list=bookChapterService.getCatLog("备急千金药方");
//   System.out.println(bookChapterService.getCatLog("备急千金药方"));
    System.out.println(bookChapterService.getChapter("孙真人千金方"));
   /*  main:
            root=从数据库中获取grade为0的节点
   *        root.list=获取子节点(root.id，root.grade)

   *   function 获取子节点(id ,grade){
           list=数据库操作获取子节点(id)
           for item in list
                item.setList=获取子节点(item.id,grade+1)
        if(grade==0) return list；
        }

        function 数据库操作获取子节点(id){
             		SELECT id,grade,chapter_name FROM ts_book_chapter
             		 WHERE parentid=#{id}
             		 ORDER BY sequence
                     }
        *
   *-----------------------------------------------------------
           main:

                function 查询结果(code)
                      List<CataLog> list= 从数据库中查出某个code对应的孩子节点(code)
                      itme in list
                        item.setChildList(从数据库中查出某个code对应的孩子节点(item.id))
                      return list
           function 从数据库中查询结果(code)
                SELECT * FROM ts_book_chapter
                WHERE
                 parentid=(SELECT id from ts_book_chapter WHERE parentid=#{code})
   * */



        }

}

