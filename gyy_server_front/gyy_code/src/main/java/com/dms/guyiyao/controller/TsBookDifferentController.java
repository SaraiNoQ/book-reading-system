package com.dms.guyiyao.controller;


import com.dms.guyiyao.pojo.ContentDiff;
import com.dms.guyiyao.service.ITsBookDifferentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 异文信息关联表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags = "获取原生异文")
@RestController
@RequestMapping("/content")
public class TsBookDifferentController {
    @Autowired
    ITsBookDifferentService bookDifferentService;
    @ApiOperation("通过章节id获取原生异文列表")
@PostMapping ("/diff/{chapterId}")
    public List<ContentDiff> getDiff(@ApiParam("章节id") @PathVariable("chapterId")String chapterId,String tarBook){//新增参数
        return bookDifferentService.getDiff(chapterId,tarBook);
}
}
