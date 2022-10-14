package com.dms.guyiyao.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.DemoApplication;
import com.dms.guyiyao.mapper.TsBookMapper;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.PageV1;
import com.dms.guyiyao.pojo.TsBook;
import com.dms.guyiyao.pojo.TsBook_0;
import com.dms.guyiyao.service.ITsBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 医药典籍总体概述表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TsBookServiceImpl extends ServiceImpl<TsBookMapper, TsBook> implements ITsBookService {
    @Autowired
    TsBookMapper bookMapper;
    /*
    * 获取前台首页图书信息
    * */
    public PageV1 getIndexBookInfo(Integer page, Integer size){
        Page p=new Page(page,size);
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("status","0");
        wrapper.orderByAsc("sequence");
        Page<TsBook>meta=bookMapper.selectPage(p,wrapper);
        List<TsBook>books=meta.getRecords();
        List data =new ArrayList();
        for (TsBook book : books) {
//            status为1表示图书已被删除，不需显示信息
            TsBook_0 book_0=new TsBook_0();
           book_0.setIntroduction(book.getIntroduction());
            book_0.setBookname(book.getBookname());
            book_0.setAuthor(book.getAuthor());
            book_0.setDetailinfo(book.getDetailinfo());
            book_0.setDynasty(book.getDynasty());
            book_0.setImgurl(book.getImgurl());
            book_0.setVersion(book.getVersion());
            book_0.setSequence(book.getSequence());
            data.add(book_0);
        }
        PageV1 pageV1=new PageV1();
        pageV1.setData(data);
        pageV1.setTotal(meta.getTotal());
        return  pageV1;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);
        TsBookServiceImpl tsBookServiceImpl = run.getBean("tsBookServiceImpl", TsBookServiceImpl.class);
        PageV1 indexBookInfo = tsBookServiceImpl.getIndexBookInfo(1, 10);
        System.out.println(indexBookInfo);

    }
}
