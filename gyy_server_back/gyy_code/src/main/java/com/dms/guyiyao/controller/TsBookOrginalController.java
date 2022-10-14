package com.dms.guyiyao.controller;


import com.dms.guyiyao.pojo.book.TsBookOrginal;
import com.dms.guyiyao.pojo.original.ContentOriginal;
import com.dms.guyiyao.pojo.original.ImgForm;
import com.dms.guyiyao.pojo.original.OriginalReturn;
import com.dms.guyiyao.service.ITsBookOrginalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 医书原文表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags="原文管理")
@RestController
@RequestMapping("/original")
public class TsBookOrginalController {
    @Autowired
    ITsBookOrginalService bookOrginalService;
    @ApiOperation("获取某个章节的所有图片url")
    @PostMapping("/getImgUrl/{chapterId}")
    public List<TsBookOrginal>GetPicUrl(@PathVariable("chapterId") String chapterId){
        return bookOrginalService.getPicUrl(chapterId);
    }
@ApiOperation("删除图片")
    @PostMapping("/deleteImg")
    public  String deleteImg(@RequestParam("imgId") String imgId,@RequestParam("imgUrl") String imgUrl){
       int rs=0;
//        imgUrl=imgUrl.replace("/","\\");
      rs=  bookOrginalService.deleteImgUrl(imgId,imgUrl);

    if (rs!=0)return "success";
    else return "false";
    }
    @ApiOperation("更新图片")
    @PostMapping("/uploadImg")
    public void uploadImg(ImgForm imgForm){
        if (imgForm.getFile().length==2&&!imgForm.getFile()[1].contains("base64,")){
           String fixbug[]=new String[1];
            fixbug[0]=imgForm.getFile()[0]+","+imgForm.getFile()[1];
            imgForm.setFile(fixbug);
        }
//        System.out.println(imgForm);
        bookOrginalService.uploadImg(imgForm);
    }
    @ApiOperation("添加图文映射")
    @PostMapping ("/addMathch")
    public String addMatch(@RequestParam("chapterId")String chapterId ,@RequestParam("content") String content,@RequestParam("imgId") String[] imgId,@RequestParam("sequence")int sequence){
       return bookOrginalService.addMatch(chapterId,content,imgId,sequence);
    }
    @ApiOperation("获取图文映射")
    @PostMapping("/getMatch")
    public Map<Integer, OriginalReturn> getMatch(@RequestParam("chapterId")String chapterId){
              return bookOrginalService.getOriginal(chapterId);
    }
//    @RequestMapping("/delteMatch")
//    public  String deleteMatch(@RequestParam("originalId")String originalId){
//        return  bookOrginalService.deleteMathch(originalId);
//    }
    @ApiOperation("删除图文映射")
    @PostMapping("/delteMatch")
    public  String deleteMatch(@RequestParam("chapterId")String chapterId,@RequestParam("sequence") int sequence){
        return  bookOrginalService.deleteMathch(chapterId,sequence);
    }

//    @ApiOperation("图片测试")
//    @RequestMapping("/test")
//    public  String test(@RequestParam("pic") MultipartFile uploadFile,
//                        @RequestParam("username")String username,
//                        @RequestParam("password") String password){
//        return  bookOrginalService.testFtp(uploadFile,username,password);
//    }
/**/
}
