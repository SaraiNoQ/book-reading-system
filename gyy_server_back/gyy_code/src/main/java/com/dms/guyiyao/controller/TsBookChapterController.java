package com.dms.guyiyao.controller;


import com.dms.guyiyao.mapper.TsBookMapper;
import com.dms.guyiyao.pojo.TreeNode;
import com.dms.guyiyao.pojo.chapter.CataLog;
import com.dms.guyiyao.service.ITsBookChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 医书序卷章节表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags = "章节管理接口")
@RestController
@RequestMapping("/chapter")
public class TsBookChapterController {
    @Autowired
    ITsBookChapterService bookChapterService;
    @Autowired
    TsBookMapper bookMapper;
    @Autowired

    @ApiOperation("获取目录树")
    @GetMapping("/getChapter")
    public List<List<TreeNode>>  getCatLog(){
        List<List<TreeNode>>list=new ArrayList<>();
        for (String bookName : (List<String>)bookMapper.getBookName()) {
        list.add(bookChapterService.getChapter(bookName));
        }
        return list;
    }

@ApiOperation("使用excel表导入目录树")
    @PostMapping("/import")
    @ResponseBody
    @Transactional
    public String excelImport(@RequestParam(value="filename") MultipartFile file,@RequestParam("bookName")String bookName)  {

        int count = 0;
        try {
            count = bookChapterService.ExcelAdd(file,bookName);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        if(count==0)return "数据添加异常，请重试";
       return "成功添加【"+count+"】条记录";
    }
@ApiOperation("更新章节信息")
    @PostMapping("/update")
    public  String  update(@RequestParam("id") String id,@RequestParam("parentId")String parentId,@RequestParam("chapterName")String chapterName,@RequestParam("sequence")int sequence,@RequestParam("grade")String grade){
    return    bookChapterService.updateChapter(id,parentId,chapterName,sequence,grade);
    }
    @ApiOperation("删除章节信息")
    @PostMapping("/delete")
    public String deleteChapter(@RequestParam("id")String id){
       return bookChapterService.delete(id);
    }
    @ApiOperation("添加章节信息")
    @PostMapping("/add")
    public String addChapter(@RequestParam("bookId")String bookId,@RequestParam("parentId")String parentId,@RequestParam("chapterName")String chapterName,@RequestParam("sequence")int sequence,@RequestParam("grade")String grade){
        if (bookId.equals("")||parentId.equals("")||chapterName.equals(""))return "存在空值请检查";
        return  bookChapterService.addChapter(bookId,parentId,chapterName,sequence,grade);
    }
}
