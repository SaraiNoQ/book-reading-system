package com.dms.guyiyao.controller;


import com.dms.guyiyao.DemoApplication;
import com.dms.guyiyao.mapper.TsBookChapterMapper;
import com.dms.guyiyao.mapper.TsBookMapper;
import com.dms.guyiyao.pojo.book.Imgform1;
import com.dms.guyiyao.pojo.book.TsBook_0;
import com.dms.guyiyao.pojo.page.PageV1;
import com.dms.guyiyao.service.ITcCodeService;
import com.dms.guyiyao.service.ITsBookService;
import com.dms.guyiyao.service.Impl.UploadServiceImpl;
import com.dms.guyiyao.service.UploadService;
import com.dms.guyiyao.utils.JsonUtil;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * <p>
 * 医药典籍总体概述表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags = "图书信息管理")
@RestController
public class TsBookController {
    @Autowired
    ITsBookService bookService;
    @Autowired
    private UploadServiceImpl uploadService;
    @Autowired
    private ITcCodeService codeService;
@Autowired
    private TsBookMapper bookMapper;
@Autowired
    private TsBookChapterMapper chapterMapper;


      @ApiOperation("获取图书信息")
      @GetMapping("/book/{page}/{size}")
    public PageV1 getBookList(@PathVariable("page")Integer page,@PathVariable("size")Integer size){
            return bookService.getBookList(page,size);
      }
      /*这个方法后期需要添加事务*/
    @ApiOperation("添加图书信息")
      @PostMapping("/book/add")
    public  String   addBook(
               Imgform1 imgform1,
             @RequestParam("bookName")String bookName,
             @RequestParam("author")String author,
             @RequestParam("dynasty")String dunasty,
             @RequestParam("introduction")String introduction){
        for (TsBook_0 datum : bookService.getBookList(1, 1000).getData()) {
            if (datum.getBookname().equals(bookName))return "书籍已存在";
        }

          int rs=0;//状态判断码
//           添加数据库图书信息成功后执行
String uuid=UUID.randomUUID().toString().replace("-","");
//数据库库写入

       List<String>codes=bookMapper.getBookCodes();
        String code= String.valueOf( (int)(Math.random()*1000));
        for (int i = 0; i < codes.size(); i++) {
            if (code.equals(codes.get(i))){
                i=0;
                code=String.valueOf( (int)(Math.random()*1000));  //生成唯一的三位code
            }
        }
        try {
      bookService.addBook(bookName, author, dunasty, introduction,uuid,code);//添加图书基本信息
         chapterMapper.addRootChapter(code,bookName);
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }

        if (imgform1.getImg()!=null)rs=uploadService.addBookImg(imgform1.getImg(),bookName,uuid);//上传到图片服务器
        if (rs==0)return "上传图书基本信息成功。但是封面上传失败。请手动设置封面图片";
        return "success";
    }
      @ApiOperation("获取朝代的码表")
      @ResponseBody
      @PostMapping("/book/getDynasty")
    public List getDynasty(){
          return     codeService.getDynasty();
      }
      @ApiOperation("删除某本书")
      @PostMapping("/book/delete/{bookId}")
      public  String deleteBook(@PathVariable("bookId") String bookId,HttpServletResponse response){
      int result=bookService.deleteBook(bookId);
      if (result==0)return "false";
      return "success";
      }

    /**
     *
     * @param id
     * @param bookName
     * @param author
     * @param dunasty
     * @param introduction
     * @param sequence 大于0的排序字段
     * @return success ->成功 其他->异常处理直接显示在前端
     */
      @ApiOperation("修改图书基本信息不包括图片")
      @PostMapping("/book/update")
      public  String updateBook(String id,
                                 @RequestParam("bookName")String bookName,
                                 @RequestParam("author")String author,
                                 @RequestParam("dynasty")String dunasty,
                                 @RequestParam("introduction")String introduction,
                                 @RequestParam("sequence")Integer sequence){
        if (sequence<0)return "请输入大于0的排序字段";
        String status="error";
        status= bookService.updateBook(id, bookName, author, dunasty, introduction, sequence);
        return status;
    }
     @ApiOperation("更新封面图片")
     @PostMapping("/book/update/img")
     public  String updateBookImg(String id,Imgform1 imgform1){
       String uuid=UUID.randomUUID().toString().replaceAll("-","");
        String imgurl="/book/"+uuid+".png";
//         System.out.println("长度"+imgform1.getImg().length());
         uploadService.addBookImg(imgform1.getImg(),"",uuid);
         bookService.updateBookImg(id,imgurl);
    return "success";
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);
        TsBookController tsBookController = run.getBean("tsBookController", TsBookController.class);
/*
* 获取图书信息
* */
//        System.out.println(tsBookController.getBookList(1, 10));
        /*
        * PageV1(data=[TsBook_0(bookId=1a4e1c0eef7511ec8c960050568c49e0, bookname=备急千金要方(测试), author=孙思邈, dynasty=唐, createtime=2022-06-18T22:11:17, creater=qq, imgurl=/book/00de61ae3b0c4281a7c089ce8f237575.png, introduction=《备急千金要方》共三十卷。内容包括医学总论、妇人、少小婴孺、七窍病、风毒脚气、诸风、伤寒、肝脏、胆腑、小肠腑、脾脏、胃腑、肺腑、大肠腑、肾脏、膀胱腑、心脏、解毒、养性、平脉、针灸等，计233门，共载方5300余首，分门别类，内容博深，具有许多独到、精辟的论述。
对医德方面作了较为全面的论述，博采群经，辑录了《内经》和扁鹊、仲景、华佗、王叔和巢元方等名家论述。在方药方面，广泛收集了前代医家的大量方剂、流传民间的许多有效方药，并参以自己的见解是我国现存的最早的一部医学类书。, sequence=2), TsBook_0(bookId=248e91d20fbb11ed8c960050568c49e0, bookname=备急千金要方, author=孙思邈, dynasty=唐, createtime=2022-07-29T23:53:16, creater=张少乾博士, imgurl=/book/9f24e6e1027d4a0aa14b10a40652130d.png, introduction=唐代孙思邈所著，经宋代校正医书局校改。, sequence=4), TsBook_0(bookId=3ffc3fb225d411ed8c960050568c49e0, bookname=千金翼方, author=孙思邈, dynasty=唐, createtime=2022-08-27T02:48:25, creater=张少乾博士, imgurl=/book/995e2044fb2b4f2dbfd5d6ee9e1f0ac9.png, introduction=《千金翼方》，唐代医学家孙思邈撰，约成书于永淳二年(682)。作者集晚年近三十年之经验，以补早期巨著《千金要方》之不足，故名翼方。孙思邈认为生命的价值贵于千金，而一个处方能救人于危殆，以千金来命名此书极为恰当。《千金翼方》全书共三十卷，北宋时期校正医书局对其传本予以校正，并刊行全国。宋代印本在明代以前失传了，所幸印版保存了下来，明朝万历年间，翰林院纂修官王肯堂奉万历皇帝之命纂刻了宋版《千金翼方》。《千金翼方》是我国历史上最重要的中医药典籍之一。, sequence=6), TsBook_0(bookId=556c1cc5ef7511ec8c960050568c49e0, bookname=新雕孙真人千金方（测试）, author=孙思邈, dynasty=唐, createtime=2022-06-18T22:12:56, creater=张少乾博士, imgurl=/book/e9510a39da0f41819df3cd525485c45a.png, introduction=《孙真人千金方》系清末发现的一种《千金要方》古本。全书仅存二十卷（即卷一至卷五，卷十一至卷十五，卷二十一至卷三十）。书中分别介绍了医学总论、妇人方、少小婴孺方、脏腑病证治（肝、胆、心、小肠、脾）、消渴淋闭及疗肿痔漏的证治，另外，还阐述了各种急救方法、养生导引法、食治及针灸等方面的内容。该书在编次先后，篇章段落，字句方药等方面均与宋本《千金要方》有一定的区别。, sequence=7), TsBook_0(bookId=7216ea03274511ed8c960050568c49e0, bookname=伤寒论, author=张仲景, dynasty=东汉, createtime=2022-08-28T22:51:13, creater=张少乾博士, imgurl=/book/9821e959df48458cb055a4001d012b57.png, introduction=《伤寒论》为东汉张仲景所著汉医经典著作，是一部阐述外感热病治疗规律的专著，全书12卷。现今遗存10卷22篇。, sequence=8)], total=5)
        * */
        System.out.println(tsBookController.updateBook("1a4e1c0eef7511ec8c960050568c49e0", "备急千金要方(测试)", "孙思邈", "唐", "《备急千金要方》共三十卷。内容包括医学总论、妇人、少小婴孺、七窍病、风毒脚气、诸风、伤寒、肝脏、胆腑、小肠腑、脾脏、胃腑、肺腑、大肠腑、肾脏、膀胱腑、心脏、解毒、养性、平脉、针灸等，计233门，共载方5300余首，分门别类，内容博深，具有许多独到、精辟的论述。", 5));
    }

}


