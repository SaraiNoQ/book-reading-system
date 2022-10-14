package com.dms.guyiyao.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsBookChapterMapper;
import com.dms.guyiyao.mapper.TsBookMapper;
import com.dms.guyiyao.pojo.TreeNode;
import com.dms.guyiyao.pojo.book.TsBook;
import com.dms.guyiyao.pojo.book.TsBookChapter;
import com.dms.guyiyao.pojo.chapter.CataLog;
import com.dms.guyiyao.pojo.chapter.Chapter_db;
import com.dms.guyiyao.service.ITsBookChapterService;
import freemarker.core.ParseException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 医书序卷章节表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TsBookChapterServiceImpl extends ServiceImpl<TsBookChapterMapper, TsBookChapter> implements ITsBookChapterService {
@Autowired
TsBookChapterMapper chapterMapper;
@Autowired
TsBookMapper bookMapper;
    @Override
        public int ExcelAdd(MultipartFile file,String bookName) throws Exception {
        String  code = bookMapper.getBookCode(bookName);
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);
            if (code==null)throw new IOException("关联图书不存在");
            if (authentication.getName()==null)throw  new Exception("请先登录后操作");
            int result = 0;
            //存放excel表中所有账号的信息
            List<Chapter_db>  chapterList = new ArrayList<>();
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            InputStream ins = file.getInputStream();
            Workbook wb = null;
            if (suffix.equals("xlsx")) {
                wb = new XSSFWorkbook(ins);
            } else {
                wb = new HSSFWorkbook(ins);
            }


            Sheet sheet = wb.getSheetAt(0);

        DataFormatter formatter = new DataFormatter();

            String bookCode = bookMapper.getBookCode(bookName);
            int countRow=0;

        System.out.println("aaaa"+countRow);
            for (Row row : sheet) {
                   if (row.getRowNum()==0)continue;//跳过第一行
                if (row==null)break;//到最后一行后终止
                    int Num=0;
                    Chapter_db chapter_db = new Chapter_db();
                    int blankCount=0;//计算一行中有几个空格
                    for (Cell cell : row) {
                        Num++;//列数，从1开始对应第0列
                        String text = formatter.formatCellValue(cell);
                        if (text.equals("")||text==null){
                            blankCount++;
                            continue;
                        }
                        if (Num==2) {//获取第二列 ps:第一列的id是无用数据不用获取
                                chapter_db.setChapter_id(bookCode+text.substring(2));
                                chapter_db.setSequence(getSequence(text));//添加sequence 同级章节间排序
                            }

                        else if (Num==3)chapter_db.setChapter_name(text);
                       else if (Num==4)chapter_db.setGrade(Integer.valueOf(text));
                    }
                if (blankCount>=1&&blankCount<3){//出现异常数据提示用户
                    System.out.println("bbb"+blankCount);throw new Exception("在第:"+row.getRowNum()+"行存在空行数据请检查");}
                if (blankCount==3)break;//出现多个空格认为是到了最后几行了就结束遍历
                chapter_db.setChapter_id_parent(getParentId(chapter_db.getChapter_id()));
                    chapter_db.setBookName(bookName);
                    chapter_db.setOpUser(authentication.getName());
                    chapter_db.setOpTime(timeStr);
                    chapterList.add(chapter_db);
                }
            /*
            * 在数据库中添加处理后的数据
            * */
                int count=0;
                try {
                for (Chapter_db chapter : chapterList) {
                   result= chapterMapper.addChapterByExcel(chapter);
//                   if (count==10) {throw new Exception("test");}
//                   if (result==0)throw new Exception("数据库添加失败");
//                   else count++;
                   count++;
                }
                }catch (DuplicateKeyException e){
                    throw new RuntimeException("此书已进行了导入请勿重复添加");
                }
                catch (Exception e){
                    throw new RuntimeException("数据库添加异常添加失败,请重新操作");
                }
            return count;
        }
    @Override
    public int getSequence(String chapter_id) {
String sequence_str=chapter_id.substring(chapter_id.length()-3);
int sequence=Integer.parseInt(sequence_str);
        return sequence;
    }
    @Override
    public String getParentId(String chapter_id) {
//去掉前两位,前两位加对应图书的code，后三位直接去掉
         String   parentid=chapter_id.substring(0,chapter_id.length()-3);
         return  parentid;
    }

    @Override
    public List<TreeNode> getChapter(String bookName) {
        //从数据库种获取当前书籍所有的章节信息
        List<TreeNode>nodeList=chapterMapper.getChapterNode(bookName);

//         构建树结构并返回
        TreeBuild treeBuild = new TreeBuild(nodeList);
        List<TreeNode> nodeList1 = treeBuild.buildTree();
        return  nodeList1;
    }



    @Override
    public String updateChapter(String id, String parentId, String chapterName, int sequence, String grade) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);
        int result=chapterMapper.updateChpter(id,parentId,chapterName,sequence, grade,timeStr,authentication.getName());
        if (result==0)return "false";
                    return "success";
    }

    @Override
    public String delete(String id) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);
        int rs=chapterMapper.deleteChapter(id,timeStr,authentication.getName());
        if (rs==0)return "false";
        return "success";
    }

    @Override
    public String addChapter(String bookId,String parentId, String chapterName, int sequence, String grade) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);

        int rs=chapterMapper.addChapterByUser(bookId,parentId,chapterName,sequence,grade,timeStr,authentication.getName());
        if (rs==0)return "false";
        return "success";
    }


}


