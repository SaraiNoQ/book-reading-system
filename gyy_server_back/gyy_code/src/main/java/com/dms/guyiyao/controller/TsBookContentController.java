package com.dms.guyiyao.controller;


import com.dms.guyiyao.pojo.ContentType;
import com.dms.guyiyao.pojo.book.TsBookContent;
import com.dms.guyiyao.service.ITsBookContentService;
import com.dms.guyiyao.utils.FileUtils;
import com.dms.guyiyao.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.multi.MultiToolTipUI;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 医书内容表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags = "图书内容管理")
@RestController
@RequestMapping
public class TsBookContentController {
    @Autowired
    ITsBookContentService bookContentService;
@ApiOperation("获取某个章节的内容")
    @PostMapping  ("/book/bookContent")
    public List<TsBookContent> getContent(@RequestParam("chapterId") String chapterId) {

        return bookContentService.getContent(chapterId);
    }
@ApiOperation("删除某个章节的内容")
    @PostMapping("/content/delete")
    public String deleteContent(@RequestParam("contentId") String contentId) {
        return bookContentService.deleteContent(contentId);
    }
@ApiOperation("添加某个章节的内容")
    @PostMapping("/content/add")
    public String addContent(String chapterId,@RequestParam("content") String content, @RequestParam("sequence") int sequence, @RequestParam("type") String type) {
        return bookContentService.addContent(chapterId,content, sequence, type);
    }
@ApiOperation("更新某个章节的内容")
    @PostMapping("/content/update")
    public String updateContent(@RequestParam("contentId") String contentId,@RequestParam("sequence")int sequence,@RequestParam("content") String content,@RequestParam("type") String type) {
        return bookContentService.updateContent(contentId,sequence,content, type);
    }

    @ApiOperation("添加内容类型")
    @PostMapping("/content/category/add")
    public  String  addContentType(String chapter_id,String content,int sequence,String[] label){
    return  bookContentService.addContentType(chapter_id,content,sequence,label);
    }
    @ApiOperation("修改内容类型")
    @PostMapping("/content/category/update")
    public String updateContentType(String chapter_id,int sequence,String label[]){
    return     bookContentService.updateContentType(chapter_id,sequence,label);
    }
    @ApiOperation("获取内容类型")
    @PostMapping("/content/category/get")
    public  List<ContentType> getContentType(String chapterId){
    return  bookContentService.getContentType(chapterId);
    }
    @ApiOperation("删除类型")
    @PostMapping("/content/category/delete")
    public  String deleteContentType(String chapterId,int sequence){
    return bookContentService.deleteContentType(chapterId,sequence);
    }
    @ApiOperation("获取系统表")
    @PostMapping("/content/table")
    public  void getDbTable(HttpServletResponse response,String bookid) throws IOException {
        String path = System.getProperty("user.dir")+"\\test.xls";
        bookContentService.getContentDb(response,bookid);
        FileUtils.downloadFile(response,path);
    }
    @ApiOperation("导入excel表更新数据库状态")
    @PostMapping("/content/table/in")
    public String  setDb(String bookid,MultipartFile infotable) throws IOException {
     return bookContentService.updateContentDb(bookid,infotable);
    }


}

