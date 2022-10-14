package com.dms.guyiyao.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsBookDifferentMapper;
import com.dms.guyiyao.mapper.TsBookMapper;
import com.dms.guyiyao.pojo.book.TsBookDifferent;
import com.dms.guyiyao.pojo.chapter.Chapter_db;
import com.dms.guyiyao.pojo.diff.DiffItem;
import com.dms.guyiyao.pojo.diff.DiffJson;
import com.dms.guyiyao.pojo.diff.diff_From_Excel;
import com.dms.guyiyao.service.ITsBookDifferentService;

import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 异文信息关联表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TsBookDifferentServiceImpl extends ServiceImpl<TsBookDifferentMapper, TsBookDifferent> implements ITsBookDifferentService {
    @Autowired
    private TsBookDifferentMapper bookDifferentMapper;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private TsBookMapper bookMapper;
    @Override
    public int addDiff(String contentFrom, String contentTo, String chapterIdFrom, String chapterIdTo) {
        String  contentFrom_uuid=UUID.randomUUID().toString().replace("-","");
      String contentTo_uuid=UUID.randomUUID().toString().replace("-","");
      String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        /*获取安全框架中保存的用户名*/
        Authentication   authentication=SecurityContextHolder.getContext().getAuthentication();

//使用异步多线程加快异文导入速度
      asyncService.DiffAdd_IO(contentFrom_uuid,contentTo_uuid,contentFrom,contentTo,chapterIdFrom,chapterIdTo,datetime,authentication.getName());
//        long t2=System.currentTimeMillis();
//        System.out.println("main线程花费时间:"+(t2-t1));
        return 1;
    }



    @Override
    public List<DiffItem> getDiff(String chapterId) {
         List<String>contentId=bookDifferentMapper.getId(chapterId);
        List<String>diffIds=new LinkedList<>();
        for (String s : contentId) {
          String  diffId=bookDifferentMapper.diffItem(s);
          diffIds.add(diffId);
        }
        for (String diffId : diffIds) {
            System.out.println(diffId+"\t");
        }
        List<DiffItem>diffDate=new LinkedList<>();
        for (String diffId : diffIds) {
            DiffItem diffItem=new DiffItem();
            diffItem.setId(diffId);
            diffItem.setDiffFrom(bookDifferentMapper.getDiffFrom(diffId));
            diffItem.setDiffTo(bookDifferentMapper.getDiffTo(diffId));

            diffItem.setBookFrom(bookDifferentMapper.getBookFrom(diffId));
            diffItem.setBookTo(bookDifferentMapper.getBookTo(diffId));
            diffItem.setChapterFrom(bookDifferentMapper.getChapterFrom(diffId));
            diffItem.setChapterTo(bookDifferentMapper.getChapterTo(diffId));
          if (diffId!=null) diffDate.add(diffItem);
        }
//        System.out.println(diffDate);
        return diffDate;
    }

    @Override
    public String deleteDiff(String diffId) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);
        int rs=bookDifferentMapper.deleteDiff(diffId,timeStr,authentication.getName());
    if (rs!=0)return "success";
        return "false";
    }

    @Override
    public String updateDiff(String diffId, String contentFrom, String contentTo) {
        int rs=bookDifferentMapper.updateDiff(diffId,contentFrom,contentTo);
        if (rs!=0)return "success";
        return "fasle";
    }

    @Override
    public String updateHighlight(String diffId, String jsonFrom, String jsonTo) {
       int rs=0;
       rs= bookDifferentMapper.updateHighLight(diffId,jsonFrom,jsonTo);
        if (rs!=0)return "success";
        return "false";
    }

    @Override
    public DiffJson getHighlight(String diffId) {
      return   bookDifferentMapper.getDiffJson(diffId);

    }
    @Override
    public int addDiffByexcel(String bookFrom,String bookTo,MultipartFile difffile) throws Exception {
        String  code_from=bookMapper.getBookCode(bookFrom);
       String code_to= bookMapper.getBookCode(bookTo);
       if (code_from==null||code_to==null)throw new Exception("所选图书名称存在异常");
//       Map<String,diff_From_Excel>map_from=getInfo(diffFrom,code_from,"异文文件L");
//        Map<String,diff_From_Excel>map_to=getInfo(diffTo,code_to,"异文文件R");

        /**
         *
         * 判断文件版本
         */
        String fileName = difffile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);

//        String fileName_from = diffConnect.getOriginalFilename();
//        String suffix = fileName_from.substring(fileName_from.lastIndexOf(".") + 1);
        InputStream insFile = difffile.getInputStream();
        Workbook wb = null;
     if (suffix.equals("xlsx")) {
        wb = new XSSFWorkbook(insFile);
    } else {
        wb = new HSSFWorkbook(insFile);
    }
        Sheet sheet = wb.getSheetAt(0);
     int count=0;
     if (null != sheet){
            Iterator<Row> row_ite=sheet.rowIterator();
            row_ite.next();
            while ( row_ite.hasNext()){
                Row row=row_ite.next();
                if (row==null)break;
                try {
                    if (row.getCell(0)==null&&row.getCell(1)==null)break;
//                    row.getCell(0).setCellType(CellType.STRING);
//                    row.getCell(1).setCellType(CellType.STRING);
//                    String chapterFrom=map_from.get(row.getCell(0).getStringCellValue())==null?"":map_from.get(row.getCell(0).getStringCellValue()).getChapterId();//通过excelId获取章节id
//                    String chapterTo=map_to.get(row.getCell(1).getStringCellValue())==null?"":map_to.get(row.getCell(1).getStringCellValue()).getChapterId();//通过excelId获取章节id
//                    String  contentFrom=map_from.get(row.getCell(0).getStringCellValue())==null?"":map_from.get(row.getCell(0).getStringCellValue()).getContent();//通过excelId获取章节名称
//                    String contentTo=map_to.get(row.getCell(1).getStringCellValue())==null?"":map_to.get(row.getCell(1).getStringCellValue()).getContent();//通过excelId获取章节名称

                    String chapterFrom=row.getCell(1).getStringCellValue();//通过excelId获取章节id
                    chapterFrom=code_from+chapterFrom.substring(3);
                    String  contentFrom=row.getCell(2).getStringCellValue();
                    String contentTo=row.getCell(3).getStringCellValue();//通过excelId获取章节名称

                    row.getCell(4).setCellType(CellType.STRING);
                    String chapterTo=row.getCell(4).getStringCellValue();
                    chapterTo=code_to+chapterTo.substring(2);
                    addDiff(contentFrom,contentTo,chapterFrom,chapterTo);
                    System.out.println(count+"开始加入数据库");
                    count++;
                }
                catch (Exception e){
                    throw new RuntimeException("异文文件M第【"+(count+2)+"】条数据存在异常，请检查");
                }
            }
        }
        return count;
    }

    public Map<String, diff_From_Excel> getInfo(MultipartFile file,String bookCode,String dataFrom) throws Exception {
        Map<String,diff_From_Excel>map=new HashMap<>();
        String fileName_from = file.getOriginalFilename();
        String suffix = fileName_from.substring(fileName_from.lastIndexOf(".") + 1);
        InputStream ins_from = file.getInputStream();
        Workbook wb = null;
        if (suffix.equals("xlsx")) {
            wb = new XSSFWorkbook(ins_from);
        } else {
            wb = new HSSFWorkbook(ins_from);
        }
        Sheet sheet = wb.getSheetAt(0);

        if (null != sheet) {
            DataFormatter formatter = new DataFormatter();
            for (Row row : sheet) {
                    if (row.getRowNum()==0)continue;//跳过第一行
                    if (row==null)break;//最后一行中止
                int Num=0;
                int blankCount=0;//计算一行中有几个空格
                    diff_From_Excel diff=new diff_From_Excel();//异文数据
                for (Cell cell : row) {
                   if (Num==3)break;
                    String text=formatter.formatCellValue(cell);
                    if (text.equals("")||text==null){
                        blankCount++;
                    }
                    Num++;//列数，从1开始对应第0列

                    if (Num==1)diff.setId(text);//获取excelId
                    if (Num==2)diff.setChapterId(bookCode+text.substring(3));//生成chapterId
                    if (Num==3)diff.setContent(text);//获取章节名称
                }
                if (blankCount>=1&&blankCount<3&&Num==3){//出现异常数据提示用户
                  throw new Exception("在"+dataFrom+"第:"+row.getRowNum()+"行存在空行数据请检查");
                }
                if (blankCount==3){
                    break;//最后一行end
                }
                map.put(diff.getId(),diff);//放入返回结果中

            }













//            Iterator<Row> row_ite = sheet.rowIterator();
//            row_ite.next();
//            while (row_ite.hasNext()) {
////                Chapter_db chapter_db = new Chapter_db();
//                Row row = row_ite.next();
//                if (null == row) {
//                    continue;
//                }
//                //将从Excel表中读取的内容转成String格式（如果Excel表格中纯数字，不执行一下操作，会报错，建议一下操作全部执行。）
//                try {
//                    row.getCell(0).setCellType(CellType.STRING);
//                    row.getCell(1).setCellType(CellType.STRING);
//                    row.getCell(2).setCellType(CellType.STRING);
//                    String id=row.getCell(0).getStringCellValue();
//                    String chapter=row.getCell(1).getStringCellValue();
//                    chapter=bookCode+chapter.substring(3);
//                    String content=row.getCell(2).getStringCellValue();
//
////                    System.out.println("------------建立map--------------");
////                    System.out.print(id + "\t");
////                    System.out.print(chapter+"\t");
////                    System.out.print(content + "\t");
////                    System.out.println("------------建立map--------------");
//                        diff_From_Excel diff=new diff_From_Excel();
//                        diff.setId(id);
//                        diff.setChapterId(chapter);
//                        diff.setContent(content);
//                        map.put(id,diff);
////                    break;//待删除
//                } catch (Exception e) {
//                    break;
//                }
//            }
//
//        }else {
//            throw new Exception("请保证数据放在excel表格的第一页");
//        }
//    return map;
    }
        return map;
    }


}
