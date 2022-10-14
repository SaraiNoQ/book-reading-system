package com.dms.guyiyao.controller;


import com.dms.guyiyao.service.ITsBookContentService;
import com.dms.guyiyao.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * <p>
 * 医书内容表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags = "章节内容接口")
@RestController
public class TsBookContentController {
    @Autowired
    ITsBookContentService bookContentService;
    @ApiOperation("获取内容内容类型")
@PostMapping ("/book/bookContent")
    public LinkedList<String[]> getContent_diff(@RequestParam("chapterId")String chapterId, @RequestParam("tarBook") String tarBook){
    return  bookContentService.getContent_diff(chapterId,tarBook);
    }

}
