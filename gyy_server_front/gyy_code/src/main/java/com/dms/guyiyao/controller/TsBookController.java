package com.dms.guyiyao.controller;


import com.dms.guyiyao.pojo.PageV1;
import com.dms.guyiyao.pojo.TsBook_0;
import com.dms.guyiyao.service.ITsBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 医药典籍总体概述表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags = "图书信息接口")
@RestController
@RequestMapping("/book")
public class TsBookController {
    @Autowired
    ITsBookService bookService;
    @ApiOperation("获取首页图书信息")
    @GetMapping("/getBookInfo/{page}/{size}")
    public PageV1 getBookInfo (@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        return  bookService.getIndexBookInfo(page,size);
    }

}
