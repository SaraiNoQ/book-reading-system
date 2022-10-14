package com.dms.guyiyao.controller;


import com.dms.guyiyao.pojo.CataLog;
import com.dms.guyiyao.pojo.TreeNode;
import com.dms.guyiyao.service.ITsBookChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 医书序卷章节表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags = "目录树管理")
@RestController
@RequestMapping("/bookChapter")
public class TsBookChapterController {
@Autowired
    ITsBookChapterService bookChapterService;
@ApiOperation("获取某本书的目录树")
@PostMapping("/getChapter")
    public List<TreeNode> getCatLog(@ApiParam("图书名") @RequestParam("bookName") String bookName){
    return bookChapterService.getChapter(bookName);
}
}
