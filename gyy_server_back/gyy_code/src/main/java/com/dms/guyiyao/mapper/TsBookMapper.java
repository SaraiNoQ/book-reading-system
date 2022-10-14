package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.book.TsBook;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

import java.util.List;

/**
 * <p>
 * 医药典籍总体概述表 Mapper 接口
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface TsBookMapper extends BaseMapper<TsBook> {
int addBook(String bookCreater,String addTime,String bookName,String author,String dynasty,String introduction,String code);
    int getBookNum();
    String getBookCode(String bookName);
    List getBookName();
    int deleteBook(String bookId,String opUser,String opTime);
    List<String> getBookCodes();
    int updateBook(String id,String bookName, String author, String dunasty, String introduction,Integer sequence);

    int updateBookImg(String id, String imgUrl);

    String getBookNameById(String id);

//    List<TsBook> getBookInfo_chapter();
}
