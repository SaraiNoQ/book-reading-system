package com.dms.guyiyao.controller;

import com.alibaba.fastjson.JSON;
import com.dms.guyiyao.service.ExportWord;
import com.dms.guyiyao.service.SearchContentService;
import com.sun.org.glassfish.gmbal.DescriptorFields;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Api(tags="高级搜索框架")
@RestController
public class SearchController {
    @Autowired
    SearchContentService searchContentService;
    @Autowired
    ExportWord exportWord;
    @ApiOperation("高级搜索")
    @PostMapping("/Search/content")
    public List<Map<String, Object>> SearchContent(
            HttpServletResponse response,
            @RequestParam("searchType")@DescriptorFields("分词/不分")@ApiParam("搜索模式") String searchType,
            @RequestParam("isExport") @DescriptorFields("是/否")@ApiParam("是否导出")String isExport,
            @ApiParam("用户查询的关键字")@RequestParam("keyword") String keyword
            , @ApiParam("查询的图书名")@RequestParam("bookName") String bookName[]
            ,@ApiParam("内容类型") @RequestParam("type")String[] type
            , @ApiParam("查询第几页")@RequestParam("page") int page
            , @ApiParam("每页多少条数据")@RequestParam("size") int size) throws IOException {
        List<Map<String, Object>> result=null;
            result=searchContentService.searchContent_Main_MH(keyword, bookName, type,searchType,page, size);
        if (isExport.equals("否"))return  result;
        searchContentService.exportSearch(response,keyword,bookName,type,result);
//        return  result;
        return null;
    }
    @ApiOperation("异文高级搜索")
    @PostMapping("/Search/diff")
    public List<Map<String, Object>> SearchDiff(
            HttpServletResponse response,
            @ApiParam("用户查询的关键字")@RequestParam("keyword") String keyword
            ,@RequestParam("searchType")@DescriptorFields("分词/不分")@ApiParam("搜索模式") String searchType
            , @ApiParam("bookFrom")@RequestParam(value = "bookFrom",required = false) String bookFrom
            ,@ApiParam("bookTo")@RequestParam(value = "bookTo" ,required = false)String bookTo
            , @ApiParam("查询第几页")@RequestParam("page") int page
            , @ApiParam("每页多少条数据")@RequestParam("size") int size) throws IOException {

        bookFrom=bookFrom==null?"":bookFrom;
        bookTo=bookTo==null?"":bookTo;
        return     searchContentService.searchDiff(keyword, bookFrom, bookTo,searchType,page, size);
    }
    @ApiOperation("导出选中异文")
    @PostMapping("/Search/Export")
    public  void  ExportDiff(HttpServletResponse response, @RequestParam("list") String list) throws IOException {
//        list="[\n" +
//                "\t[0, \"备急千金要方(宋校版)\", \"大医精诚第二\", \"张湛曰：夫经方之难精，由来尚矣。今病有内同而外异，亦有内异而外同，故五脏六腑之盈虚，血脉荣卫之通塞，固非耳目之所察，必先诊候以审之。而寸口关尺，有浮沉弦紧之乱；腧穴流注，有高下浅深之差；肌肤筋骨，有厚薄刚柔之异。唯用心精微者，始可与言于兹矣。今以至精至微之事，求之于至粗至浅之思，其不殆哉!若盈而益之，虚而损之，通而彻之，塞而壅之，寒而冷之，热而温之，是重加其疾，而望其生，吾见其死矣。故医方卜筮，艺能之难精者也。既非神授，何以得其幽微？世有愚者，读方三年，便谓天下无病可治；及治病三年，乃知天下无方可用。故学者必须博极医源，精勤不倦，不得道听途说，而言医道已了，深自误哉！\", \"张湛曰：夫经方之难精，由来尚矣。今病有内同而外异，亦□□□而外同，故五脏六腑之盈虚，血脉荣卫之通塞，固非耳目□□□，必诊候以审之。脉有浮沉弦紧之乱；腧穴流注，有高下浅□□□；肌肤筋骨，有厚薄刚柔之异。唯用心精微者，始可言于兹□。□以至精至微之事，求之于至粗至浅之思，其不殆哉！若盈而益之，虚而损之，通而彻之，塞而拥之，寒而冷之，热而温之，是重其疾，而望其生，吾见其死矣。故医方卜筮，艺能之难精者也。既非神授，何以得其幽微？世有愚者，读方三年，便谓天下无病可治；及治病三年，乃知天下无方可疗。故学者必须博极医源，精勤不倦，不得道听途说，而言医道已了，深自误哉。\", \"新雕孙真人千金方\", \"精诚第二\", null, null],\n" +
//                "\t[1, \"备急千金要方(宋校版)\", \"大医精诚第二\", \"张湛曰：夫经方之难精，由来尚矣。今病有内同而外异，亦有内异而外同，故五脏六腑之盈虚，血脉荣卫之通塞，固非耳目之所察，必先诊候以审之。而寸口关尺，有浮沉弦紧之乱；腧穴流注，有高下浅深之差；肌肤筋骨，有厚薄刚柔之异。唯用心精微者，始可与言于兹矣。今以至精至微之事，求之于至粗至浅之思，其不殆哉!若盈而益之，虚而损之，通而彻之，塞而壅之，寒而冷之，热而温之，是重加其疾，而望其生，吾见其死矣。故医方卜筮，艺能之难精者也。既非神授，何以得其幽微？世有愚者，读方三年，便谓天下无病可治；及治病三年，乃知天下无方可用。故学者必须博极医源，精勤不倦，不得道听途说，而言医道已了，深自误哉！\", \"张湛曰：夫经方之难精，由来尚矣。今病有内同而外异，亦□□□而外同，故五脏六腑之盈虚，血脉荣卫之通塞，固非耳目□□□，必诊候以审之。脉有浮沉弦紧之乱；腧穴流注，有高下浅□□□；肌肤筋骨，有厚薄刚柔之异。唯用心精微者，始可言于兹□。□以至精至微之事，求之于至粗至浅之思，其不殆哉！若盈而益之，虚而损之，通而彻之，塞而拥之，寒而冷之，热而温之，是重其疾，而望其生，吾见其死矣。故医方卜筮，艺能之难精者也。既非神授，何以得其幽微？世有愚者，读方三年，便谓天下无病可治；及治病三年，乃知天下无方可疗。故学者必须博极医源，精勤不倦，不得道听途说，而言医道已了，深自误哉。\", \"新雕孙真人千金方\", \"精诚第二\", null, null],\n" +
//                "\t[2, \"备急千金要方(宋校版)\", \"大医精诚第二\", \"张湛曰：夫经方之难精，由来尚矣。今病有内同而外异，亦有内异而外同，故五脏六腑之盈虚，血脉荣卫之通塞，固非耳目之所察，必先诊候以审之。而寸口关尺，有浮沉弦紧之乱；腧穴流注，有高下浅深之差；肌肤筋骨，有厚薄刚柔之异。唯用心精微者，始可与言于兹矣。今以至精至微之事，求之于至粗至浅之思，其不殆哉!若盈而益之，虚而损之，通而彻之，塞而壅之，寒而冷之，热而温之，是重加其疾，而望其生，吾见其死矣。故医方卜筮，艺能之难精者也。既非神授，何以得其幽微？世有愚者，读方三年，便谓天下无病可治；及治病三年，乃知天下无方可用。故学者必须博极医源，精勤不倦，不得道听途说，而言医道已了，深自误哉！\", \"张湛曰：夫经方之难精，由来尚矣。今病有内同而外异，亦□□□而外同，故五脏六腑之盈虚，血脉荣卫之通塞，固非耳目□□□，必诊候以审之。脉有浮沉弦紧之乱；腧穴流注，有高下浅□□□；肌肤筋骨，有厚薄刚柔之异。唯用心精微者，始可言于兹□。□以至精至微之事，求之于至粗至浅之思，其不殆哉！若盈而益之，虚而损之，通而彻之，塞而拥之，寒而冷之，热而温之，是重其疾，而望其生，吾见其死矣。故医方卜筮，艺能之难精者也。既非神授，何以得其幽微？世有愚者，读方三年，便谓天下无病可治；及治病三年，乃知天下无方可疗。故学者必须博极医源，精勤不倦，不得道听途说，而言医道已了，深自误哉。\", \"新雕孙真人千金方\", \"精诚第二\", \"{\\\"content\\\":\\\"张湛曰：夫经方之难精，由来尚矣。今病有内同而外异，亦有内异而外同，故五脏六腑之盈虚，血脉荣卫之通塞，固非耳目之所察，必先诊候以审之。而寸口关尺，有浮沉弦紧之乱；腧穴流注，有高下浅深之差；肌肤筋骨，有厚薄刚柔之异。唯用心精微者，始可与言于兹矣。今以至精至微之事，求之于至粗至浅之思，其不殆哉!若盈而益之，虚而损之，通而彻之，塞而壅之，寒而冷之，热而温之，是重加其疾，而望其生，吾见其死矣。故医方卜筮，艺能之难精者也。既非神授，何以得其幽微？世有愚者，读方三年，便谓天下无病可治；及治病三年，乃知天下无方可用。故学者必须博极医源，精勤不倦，不得道听途说，而言医道已了，深自误哉！\\\\n\\\",\\\"labelCategories\\\":[{\\\"id\\\":0,\\\"text\\\":\\\"异文\\\",\\\"color\\\":\\\"#eac0a2\\\",\\\"borderColor\\\":\\\"#a38671\\\"}],\\\"labels\\\":[{\\\"id\\\":0,\\\"categoryId\\\":0,\\\"startIndex\\\":4,\\\"endIndex\\\":33}],\\\"connectionCategories\\\":[],\\\"connections\\\":[]}\", \"{\\\"content\\\":\\\"张湛曰：夫经方之难精，由来尚矣。今病有内同而外异，亦□□□而外同，故五脏六腑之盈虚，血脉荣卫之通塞，固非耳目□□□，必诊候以审之。脉有浮沉弦紧之乱；腧穴流注，有高下浅□□□；肌肤筋骨，有厚薄刚柔之异。唯用心精微者，始可言于兹□。□以至精至微之事，求之于至粗至浅之思，其不殆哉！若盈而益之，虚而损之，通而彻之，塞而拥之，寒而冷之，热而温之，是重其疾，而望其生，吾见其死矣。故医方卜筮，艺能之难精者也。既非神授，何以得其幽微？世有愚者，读方三年，便谓天下无病可治；及治病三年，乃知天下无方可疗。故学者必须博极医源，精勤不倦，不得道听途说，而言医道已了，深自误哉。\\\\n\\\",\\\"labelCategories\\\":[{\\\"id\\\":0,\\\"text\\\":\\\"异文\\\",\\\"color\\\":\\\"#eac0a2\\\",\\\"borderColor\\\":\\\"#a38671\\\"}],\\\"labels\\\":[{\\\"id\\\":0,\\\"categoryId\\\":0,\\\"startIndex\\\":4,\\\"endIndex\\\":15}],\\\"connectionCategories\\\":[],\\\"connections\\\":[]}\"]\n" +
//                "]";
        String s = JSON.parseObject(list, String.class);
        List<List<Object>> arr= JSON.parseObject(s,new ArrayList<ArrayList<Object>>().getClass());
        System.out.println("解析结果"+arr);
        exportWord.ExportWord_v2(response,arr);
    }






//    @ApiOperation("搜索导出")
//    @PostMapping("/Search/export")
//    public void ExportSearch(HttpServletResponse response, @ApiParam("用户查询的关键字")@RequestParam("keyword") String keyword
//            , @ApiParam("查询的图书名")@RequestParam("bookName") String bookName[]
//            , @ApiParam("内容类型") @RequestParam("type")String[] type
//            , @ApiParam("查询第几页")@RequestParam("page") int page
//            , @ApiParam("每页多少条数据")@RequestParam("size") int size) throws IOException{
//        List<Map<String, Object>> result= searchContentService.searchContent_Main_MH(keyword,bookName,type,page,size);
//        searchContentService.exportSearch(response,keyword,bookName,type,result);
//    }
}
