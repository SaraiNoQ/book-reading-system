package com.dms.guyiyao.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsBookChapterMapper;
import com.dms.guyiyao.mapper.TsBookMapper;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.book.Imgform1;
import com.dms.guyiyao.pojo.book.TsBook;
import com.dms.guyiyao.pojo.book.TsBook_0;
import com.dms.guyiyao.pojo.page.PageV1;
import com.dms.guyiyao.pojo.user.TsUser;
import com.dms.guyiyao.service.ITsBookService;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 医药典籍总体概述表 服务实现类
 * </p>
 *
 * @author moyunbo
 * @since 2022-01-18
 */
@Service
public class TsBookServiceImpl extends ServiceImpl<TsBookMapper, TsBook> implements ITsBookService {
    @Autowired
    private TsBookMapper bookMapper;
    @Autowired
    private TsUserMapper userMapper;
    @Autowired
    private TsBookChapterMapper chapterMapper;
    /*
    * 后台系统获取所有图书信息的分页展示
    * */
    @Override
    public PageV1 getBookList(Integer page , Integer size){
        Page<TsBook>page1=new Page(page,size);

        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("status",0);
        wrapper.orderByAsc("sequence");
        Page<TsBook> booksPage= this.bookMapper.selectPage(page1,wrapper);
/*meta数据*/
        List<TsBook>booksList=booksPage.getRecords();
        /*新封装的数据*/
        List<TsBook_0>bookslist_new=new ArrayList<>();
        for(TsBook book : booksList) {
            TsBook_0 data = new TsBook_0();
            data.setImgurl(book.getImgurl());
            data.setIntroduction(book.getIntroduction());
            data.setAuthor(book.getAuthor());
            data.setBookname(book.getBookname());
            data.setCreatetime(book.getCreatetime());
            data.setDynasty(book.getDynasty());
            data.setBookId(book.getId());
            data.setSequence(book.getSequence());
            try {
//                System.out.println("id="+book.getCreaterid());
            TsUser user=userMapper.selectById(book.getCreaterid());
                data.setCreater(user.getNickname());
            }catch (NullPointerException e){
                data.setCreater(null);
            }
            bookslist_new.add(data);
        }
/*最后封装为页面数据格式*/
        PageV1 pageV1=new PageV1();
        pageV1.setData(bookslist_new);
        pageV1.setTotal(booksPage.getTotal());
        return pageV1;

    }
    @Transactional
    public  void addBook(String bookName,String author,String dynasty,String introduction,String uuid,String code)  {
        /*获取安全框架中保存的用户名*/
        Authentication   authentication=SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);
    try {
        bookMapper.addBook(authentication.getName(),timeStr,bookName,author,dynasty,introduction,code);
    }catch (Exception e){
        e.printStackTrace();
        throw new  RuntimeException("图书基本信息添加失败，请重试");
    }
    }

    @Override
    public int deleteBook(String bookId) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);
        int resutl=bookMapper.deleteBook(bookId,authentication.getName(),timeStr);
    return  resutl;
    }

    @Override
    public String updateBook(String id,String bookName, String author, String dunasty, String introduction,Integer sequence) {
        for (Object o : bookMapper.getBookName()) {
            if ( bookName.equals(o)&&!bookMapper.getBookNameById(id).equals(bookName))return "修改后的书名已存在请修改";
        }
      int rs= bookMapper.updateBook(id,bookName,author,dunasty,introduction,sequence);
       if (rs==0)return "添加失败";
       chapterMapper.updateRootChapter(id,bookName);
        return "success";
    }

    @Override
    public String updateBookImg(String id, String imgUrl) {
       int rs=0;
       rs=bookMapper.updateBookImg(id,imgUrl);
    if (rs!=0)return "success";
    return "false";
    }

    public  String  getBookNum(){
       Integer num=bookMapper.getBookNum();
       if (num<10){
           return "00"+num;
       }else if (num<100){
           return "0"+num;
       }else {
           return  ""+num;
       }
    }

}
