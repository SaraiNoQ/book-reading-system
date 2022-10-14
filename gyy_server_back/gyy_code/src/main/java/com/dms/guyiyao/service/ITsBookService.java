package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.book.Imgform1;
import com.dms.guyiyao.pojo.book.TsBook;
import com.dms.guyiyao.pojo.book.TsBook_0;
import com.dms.guyiyao.pojo.page.PageV1;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;


/**
 * <p>
 * 医药典籍总体概述表 服务类
 * </p>
 *
 * @author moyunbo
 * @since 2022-01-18
 */
public interface ITsBookService extends IService<TsBook> {
     PageV1 getBookList(Integer page ,Integer size);
      void addBook(String bookName, String author, String dynasty, String introduction,String uuid,String code) ;
      int deleteBook(String bookId);

    String updateBook(String id ,String bookName, String author, String dunasty, String introduction,Integer sequence);

    String updateBookImg(String id, String imgUrl);
}
