package com.dms.guyiyao.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsBookContentMapper;
import com.dms.guyiyao.pojo.ContentType;
import com.dms.guyiyao.pojo.book.TsBookContent;
import com.dms.guyiyao.pojo.book.TsBookContentDb;
import com.dms.guyiyao.service.ITsBookContentService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 医书内容表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TsBookContentServiceImpl extends ServiceImpl<TsBookContentMapper, TsBookContent> implements ITsBookContentService {
    @Autowired
    TsBookContentMapper bookContentMapper;
    @Override
    public List<TsBookContent> getContent(String chapterId) {
        List<TsBookContent> list =bookContentMapper.getContent(chapterId);
        return  list;
    }

    @Override
    public String deleteContent(String contentId) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);

      int rs= bookContentMapper.deleteContent(contentId,authentication.getName(),timeStr);
        if (rs==0)return "false";
      return "success";
    }

    @Override
    public String addContent(String chapterId, String content, int sequence, String type) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);
        int count=bookContentMapper.containContent(chapterId);
        if (count!=0)return "请删除后重新添加";
        int rs= bookContentMapper.addContent(chapterId,content,sequence,type,timeStr,authentication.getName());
        if (rs==0)return "false";
       return "success";
    }

    @Override
    public String updateContent(String contentId,int sequence, String content, String type) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);

      int rs= bookContentMapper.updateContent(contentId,sequence,content,type,timeStr,authentication.getName());
    if (rs==0)return "false";
        return "success";
    }

    @Override
    public String addContentType(String chapter_id, String content, int sequence, String[] label) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();


        String labels="";
        for (String s : label) {
            labels+=s+",";
        }
        Date data=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd hh:mm:ss");
         String time = simpleDateFormat.format(data);
        bookContentMapper.deleteContentType(chapter_id,sequence,time,authentication.getName());
        int rs= bookContentMapper.addContentType(chapter_id,content,sequence,labels,time,authentication.getName());
        if (rs!=0)return "success";
        return "false";
    }

    @Override
    public String updateContentType(String chapter_id, int sequence, String[] label) {
        Date data=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        String time = simpleDateFormat.format(data);
        String labels="";
        for (String s : label) {
            labels+=s+",";
        }
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

      int rs=  bookContentMapper.updateContentType(chapter_id,sequence,labels,time,authentication.getName());
       if (rs!=0)return "success";
        return "false";
    }

    @Override
    public List<ContentType> getContentType(String chapterId) {
    List<ContentType> rs =bookContentMapper.getContentType(chapterId);
        return rs;
    }
    @Override
    public String deleteContentType(String chapterId, int sequence) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);
      int rs= bookContentMapper.deleteContentType(chapterId,sequence,timeStr,authentication.getName());
      if(rs!=0)return "success";
        return "false";
    }
    @Override
    public void getContentDb(HttpServletResponse response,String bookId) throws IOException {
        String path=System.getProperty("user.dir")+"\\test.xls";
       List<TsBookContentDb> rs=bookContentMapper.getContentDb(bookId);
        Collections.sort(rs, new Comparator<TsBookContentDb>() {
            @Override
            public int compare(TsBookContentDb o1, TsBookContentDb o2) {
                return  o1.getChapterid().compareTo(o2.getChapterid());
            }
        });
        Iterator<TsBookContentDb> iterator = rs.iterator();
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("批量导入正文");
        int k=1;
        HSSFRow row_0 =  sheet.createRow(0);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        row_0.setHeight((short) 500);
        row_0.createCell(0).setCellValue("书名");
        row_0.createCell(1).setCellValue("章节ID");
        row_0.createCell(2).setCellValue("章节");
        row_0.createCell(3).setCellValue("正文内容");
        while (iterator.hasNext()){
            TsBookContentDb contentDb =null;
            try {
               contentDb= iterator.next();
            }catch (Exception e){
                e.printStackTrace();
            }
            HSSFRow row =  sheet.createRow(k);
            row.setHeight((short)500);
    row.createCell(0).setCellValue(contentDb.getBookname());
    row.createCell(1).setCellValue(contentDb.getChapterid());
    row.createCell(2).setCellValue(contentDb.getChaptername());
    row.createCell(3).setCellValue(contentDb.getContent());
    k++;
        }
    wb.write(new FileOutputStream(path));
    }

    @Override
    public String updateContentDb(String bookid, MultipartFile infotable) throws IOException {
        InputStream ins_from = infotable.getInputStream();
        String suffix=infotable.getOriginalFilename();
        Workbook wb = null;
        if (suffix.equals("xlsx")) {
            wb = new XSSFWorkbook(ins_from);
        } else {
            wb = new HSSFWorkbook(ins_from);
        }
        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();
        Map<String,String> map=new HashMap();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            String  chapterId= row.getCell(1).getStringCellValue();
            String content=row.getCell(3)==null?"":row.getCell(3).getStringCellValue();
            map.put(chapterId,content);
        }
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String opTime = time.format(date);
        String opUser=authentication.getName();
        for (String s : map.keySet()) {
            if (bookContentMapper.containContent(s)!=0){
             int i=bookContentMapper.updateContentByChapterId(s,map.get(s),"0",opTime,opUser);
             if (i==0)return "false";
            }else {
                int i = bookContentMapper.addContent(s, map.get(s), 0, "0", opTime, opUser);
                if (i==0)return "false";
            }
        }

        return "success";
    }
}
