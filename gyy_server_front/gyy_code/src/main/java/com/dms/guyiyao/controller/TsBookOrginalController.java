package com.dms.guyiyao.controller;


import com.dms.guyiyao.pojo.ContentOriginal;
import com.dms.guyiyao.service.Impl.TsBookOrginalServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 医书原文表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags="原文接口")
@RestController
public class TsBookOrginalController {
@Autowired
    TsBookOrginalServiceImpl tsBookOrginalService;
@ApiOperation("获取某个章节的原文")
@RequestMapping("/getOriginal")
    public List<ContentOriginal> getOriginal(@RequestParam("chapterId") String chapterId){
        return tsBookOrginalService.getOriginal(chapterId);
}
}
