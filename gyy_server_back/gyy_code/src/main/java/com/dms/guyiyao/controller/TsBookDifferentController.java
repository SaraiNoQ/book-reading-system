package com.dms.guyiyao.controller;


import com.dms.guyiyao.pojo.diff.DiffItem;
import com.dms.guyiyao.pojo.diff.DiffJson;
import com.dms.guyiyao.pojo.diff.diff_From_Excel;
import com.dms.guyiyao.service.ITsBookDifferentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 异文信息关联表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags="异文管理")
@RestController
@RequestMapping("/diff")
public class TsBookDifferentController {
//    @Autowired
//    ITsBookDifferentService bookDifferentService;
//    @RequestMapping("/import")
//    @ResponseBody
//    public String excelImport(@RequestParam(value="filename") MultipartFile file){
//        int result = 0;
//        try {
//            result = bookDifferentService.ExcelAdd(file);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(result > 0){
//            return "excel文件数据导入成功！";
//        }else{
//            return "请查看文件内容是否有误，确认无误后请继续上传！！";
//        }
//    }
    @Autowired
    ITsBookDifferentService differentService;
    @ApiOperation("添加异文")
    @PostMapping("/addDiff")
    public String  addDiff(@RequestParam("contentFrom") String contentFrom,@RequestParam("contentTo") String contentTo,@RequestParam("chapterIdFrom")String chapterIdFrom,@RequestParam("chapterIdTo")String chapterIdTo){
       int result= differentService.addDiff(contentFrom,contentTo,chapterIdFrom,chapterIdTo);
       if (result==1) return "success";
       else return  "false";
    }
    @ApiOperation("获取异文")
    @PostMapping("/getDiff")
    public List<DiffItem> getDiff(@RequestParam("chapterId") String chapterId){
           return  differentService.getDiff(chapterId);
    }
    @ApiOperation("删除异文")
    @PostMapping("/deleteDiff")
    public String delteDiff(@RequestParam("diffId")String diffId){
        return  differentService.deleteDiff(diffId);
    }
    @ApiOperation("更新异文")
    @PostMapping("/updateDiff")
    public  String updateDiff(@RequestParam("diffId") String diffId,@RequestParam("contentFrom")String contentFrom,@RequestParam("contentTo")String contentTo){
        return  differentService.updateDiff(diffId,contentFrom,contentTo);
    }
    @ApiOperation("更新异文标记信息")
    @PostMapping("/updateHighlight")
    public String updateHighlight(@ApiParam("异文映射表id")@RequestParam("diffId") String diffId, @ApiParam("正文标红的json") @RequestParam("jsonFrom") String jsonFrom, @ApiParam("异文标红的json")@RequestParam("jsonTo") String jsonTo){
       return differentService.updateHighlight(diffId,jsonFrom,jsonTo);
    }
    @ApiOperation("获取异文标记信息")
    @PostMapping("/GetHighlight")
    public DiffJson getHighlight(@ApiParam("异文映射表id")@RequestParam("diffId") String diffId){
       return differentService.getHighlight(diffId);
    }
    @PostMapping("/addDiffByExcel")
    @Transactional
    @ApiOperation("通过excel表导入异文信息")
    public  String  addDiffByExcel(String bookFrom,String bookTo,@RequestParam(value="file") MultipartFile diffFile)  {
       int count=0;
       try {
        count=differentService.addDiffByexcel(bookFrom,bookTo,diffFile);
       }catch (Exception e){
           return e.getMessage();
       }
        return "成功添加【"+count+"】条异文";

    }
//    @PostMapping("/addDiffByExcel")
//    @Transactional
//    @ApiOperation("通过excel表导入异文信息")
//    public  String  addDiffByExcel(String bookFrom,String bookTo,@RequestParam(value="diffFrom") MultipartFile diffFrom,@RequestParam(value="diffTo") MultipartFile diffTo,@RequestParam(value="diffConnect") MultipartFile diffConnect)  {
//        int count=0;
//        try {
//            count=differentService.addDiffByexcel(bookFrom,bookTo,diffFrom,diffTo,diffConnect);
//        }catch (Exception e){
//            return e.getMessage();
//        }
//        return "成功添加【"+count+"】条异文";
//
//    }




}
