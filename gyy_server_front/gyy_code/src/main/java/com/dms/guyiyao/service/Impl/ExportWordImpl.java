package com.dms.guyiyao.service.Impl;

import com.alibaba.fastjson.JSON;
import com.dms.guyiyao.mapper.TsBookChapterMapper;
import com.dms.guyiyao.pojo.ContentDiff;
import com.dms.guyiyao.pojo.word.Book_Chapter;
import com.dms.guyiyao.pojo.word.JsonTrans;
import com.dms.guyiyao.service.ExportWord;
import com.dms.guyiyao.utils.FileUtil;
import com.dms.guyiyao.utils.XWPFHelper;
import com.dms.guyiyao.utils.XWPFHelperTable;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ExportWordImpl implements ExportWord {
    @Autowired
    TsBookDifferentServiceImpl bookDifferentService;//注入异文模块
    @Autowired
    TsBookChapterMapper chapterMapper;
    private XWPFHelperTable xwpfHelperTable = null;
    private XWPFHelper xwpfHelper = null;

    public ExportWordImpl() {
        xwpfHelperTable = new XWPFHelperTable();
        xwpfHelper = new XWPFHelper();
    }

    /**
     * 创建好文档的基本 标题，表格  段落等部分
     */
    public XWPFDocument createXWPFDocument(int row,int cols,List<List<Object>> tableData) {
        XWPFDocument doc = new XWPFDocument();
        createTitleParagraph(doc);

        createTableParagraph(doc, row, cols,tableData);//创建表单样式和加入数据

        return doc;
    }
    /**
     * 创建表格的标题样式
     */
    public void createTitleParagraph(XWPFDocument document) {
        XWPFParagraph titleParagraph = document.createParagraph();    //新建一个标题段落对象（就是一段文字）
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);//样式居中
        XWPFRun titleFun = titleParagraph.createRun();    //创建文本对象
//        titleFun.setText(titleName); //设置标题的名字
        titleFun.setBold(true); //加粗
        titleFun.setColor("000000");//设置颜色
        titleFun.setFontSize(25);    //字体大小
//        titleFun.setFontFamily("");//设置字体
        //...
        titleFun.addBreak();    //换行
    }  /**
     * 创建异文标红样式
     */
    public void createDiffParagraph(XWPFDocument document) {
        XWPFParagraph diffParagraph = document.createParagraph();    //新建一个标题段落对象（就是一段文字）
        diffParagraph.setAlignment(ParagraphAlignment.CENTER);//样式居中
        XWPFRun diffFun =diffParagraph.createRun();    //创建文本对象
//        titleFun.setText(titleName); //设置标题的名字
        diffFun.setBold(true); //加粗
        diffFun.setColor("000000");//设置颜色
        diffFun.setFontSize(25);    //字体大小
//        titleFun.setFontFamily("");//设置字体
        //...
//        titleFun.addBreak();    //换行
    }

    /**
     * 设置表格
     * @param document
     * @param rows
     * @param cols
     */
    public void createTableParagraph(XWPFDocument document, int rows, int cols,List<List<Object>> tableData) {
//        xwpfHelperTable.createTable(xdoc, rowSize, cellSize, isSetColWidth, colWidths)
//        创建表单
//        XWPFTable infoTable = document.createTable(rows, cols);
//        xwpfHelperTable.setTableWidthAndHAlign(infoTable, "11000", STJc.CENTER);
        XWPFTable infoTable=xwpfHelperTable.createTable(document, rows, cols, true, new int[]{300, 2100, 2100, 2100,2100});


        List<XWPFTableRow> rowList = infoTable.getRows();
        for(int i = 0; i < rowList.size(); i++) {
            XWPFTableRow infoTableRow = rowList.get(i);
            List<XWPFTableCell> cellList = infoTableRow.getTableCells();
            List <JsonTrans> json_from=null;
            List <JsonTrans> json_to=null;
            try {
               json_from= (List<JsonTrans>) tableData.get(i).get(5);
               json_to= (List<JsonTrans>) tableData.get(i).get(6);
            }catch (Exception e){
                json_from=null;
                json_to=null;
            }
            for(int j = 0; j < cellList.size(); j++) {
//                获取一个单元格
                XWPFParagraph cellParagraph = cellList.get(j).getParagraphArray(0);

                cellParagraph.setAlignment(ParagraphAlignment.CENTER);
                    String content = String.valueOf(tableData.get(i).get(j));


                if (j==2||j==3){
                try {
                    Map<Integer, Integer> map = new TreeMap<>();//treemap更具key升序排列
//                    j=2取原文
                    if (j==2){
                        for (JsonTrans jsonTrans : json_from) {
                        map.put(Integer.valueOf(jsonTrans.getStartIndex()), Integer.valueOf(jsonTrans.getEndIndex()));
                        }
                    }
//                    j=3取异文
                    if (j==3){
                        for (JsonTrans jsonTrans : json_to) {
                            map.put(Integer.valueOf(jsonTrans.getStartIndex()), Integer.valueOf(jsonTrans.getEndIndex()));
                        }
                    }


                    int end=0;
                    for (Map.Entry<Integer, Integer> integerIntegerEntry : map.entrySet()) {
                        System.out.println(integerIntegerEntry.getKey());
//                        黑体字部分
                        XWPFRun cellParagraphRun = cellParagraph.createRun();
                        int start=integerIntegerEntry.getKey();
                        String content_tmp=content.substring(end,start);
                        cellParagraphRun.setFontSize(12);
                        cellParagraphRun.setColor("000000");
                        cellParagraphRun.setText(content_tmp);
                        end=integerIntegerEntry.getValue();
//                        红字部分
                         cellParagraphRun = cellParagraph.createRun();
                         content_tmp=content.substring(start,end);
                        cellParagraphRun.setFontSize(12);
                        cellParagraphRun.setColor("FF0000");
                        cellParagraphRun.setText(content_tmp);
                    }
                    System.out.println("end");
                }catch (Exception e){
                    XWPFRun cellParagraphRun = cellParagraph.createRun();
                    cellParagraphRun.setFontSize(12);
                    cellParagraphRun.setColor("000000");
                    cellParagraphRun.setText(content);
                }


                }
                else {
                    XWPFRun cellParagraphRun = cellParagraph.createRun();
                    cellParagraphRun.setFontSize(12);
                    cellParagraphRun.setColor("000000");
                    cellParagraphRun.setText(content);

                }


            }
        }

        xwpfHelperTable.setTableHeight(infoTable, 560, STVerticalJc.CENTER);
    }
    public void exportCheckWord(Map<String, Object> dataList, String savePath) throws  IOException {
//       获取表格数据
        List<List<Object>> tableData = (List<List<Object>>) dataList.get("TABLEDATA");
        //        创建表单格式并添入数据
        XWPFDocument document = createXWPFDocument(tableData.size(),tableData.get(0).size()==9?5:tableData.get(0).size(),tableData);

        XWPFParagraph paragraph = document.getParagraphArray(0);
        XWPFRun titleFun = paragraph.getRuns().get(0);
//        获取标题数据
        titleFun.setText(String.valueOf(dataList.get("TITLE")));
        XWPFTable table = document.getTableArray(0);
        xwpfHelper.saveDocument(document, savePath);
    }


    public void fillTableData(XWPFTable table, List<List<Object>> tableData) {
        List<XWPFTableRow> rowList = table.getRows();
        for(int i = 0; i < tableData.size(); i++) {
            List<Object> list = tableData.get(i);
            List<XWPFTableCell> cellList = rowList.get(i).getTableCells();
            for(int j = 0; j < list.size(); j++) {
                XWPFParagraph cellParagraph = cellList.get(j).getParagraphArray(0);
                XWPFRun cellParagraphRun = cellParagraph.getRuns().get(0);

                cellParagraphRun.setText(String.valueOf(list.get(j)));

            }
        }
    }
    @Override
    public void ExportWord_v2(HttpServletResponse response,List<List<Object>> list) throws IOException {
        String path = System.getProperty("user.dir")+"/";
        String name="tmp.docx";
        File savefile = new File(path);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
       /*生成异文数据*/
        for (List<Object> tempList : list) {
            //0为序号不变
            tempList.set(1,"《"+tempList.get(1)+"》"+"            "+tempList.get(2));// bookfrom +chapterFrom
            tempList.set(2,tempList.get(3));  //difffrom
            tempList.set(3,tempList.get(4));
            tempList.set(4,"《"+tempList.get(5).toString()+"》"+"            "+tempList.get(6).toString());
            Map map_from= (Map) JSON.parse(tempList.get(7)==null?"":tempList.get(7).toString());
            Map map_to= (Map) JSON.parse(tempList.get(8)==null?"":tempList.get(8).toString());
            tempList.set(5,JSON.parseArray(map_from==null?"":map_from.get("labels").toString(),JsonTrans.class));//jsonfrom
            tempList.set(6, JSON.parseArray(map_to==null?"":map_to.get("labels").toString(),JsonTrans.class));//jsonto
        }
                Map<String, Object> dataList = new HashMap<String, Object>();
                dataList.put("TITLE", "古医药异文导出");
                dataList.put("TABLEDATA", list);
                exportCheckWord(dataList, path + name);//放入本地文件
                /*导出到IO流*/
                FileUtil.downloadFile(response, path + name);
            }

    public void ExportWord(HttpServletResponse response, String chapterId, String tarBook) throws IOException {
        String path = System.getProperty("user.dir")+"/";
        String name="tmp.docx";
        File savefile = new File(path);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        /*生成异文数据*/
        ExportWordImpl ew = new ExportWordImpl();
        List<List<Object>> list = new ArrayList<List<Object>>();
        List<Object> tempList = new ArrayList<Object>();
//       表头
        tempList.add("序号");
        tempList.add("原文所在章节");
        tempList.add("原文");
        tempList.add("异文");
        tempList.add("异文所在章节");
        list.add(tempList);
//      表内容
        int count=1;
        for (ContentDiff contentDiff : bookDifferentService.getDiff(chapterId,tarBook)) {
            Book_Chapter book_chapter=chapterMapper.getBookAndChapter(chapterId);//正文所在章节
            tempList = new ArrayList<Object>();
            tempList.add(count++);//序号
            tempList.add("《"+book_chapter.getBookname()+"》"+"            "+book_chapter.getChaptername());
            tempList.add(contentDiff.getContentFrom());//正文
            tempList.add(contentDiff.getContentTo());//异文
            tempList.add("《"+contentDiff.getBookName()+"》"+"            "+contentDiff.getChapterName());
            /*添加json*/
            try {
                Map map_from= (Map) JSON.parse(contentDiff.getJsonFrom());
                Map map_to= (Map) JSON.parse(contentDiff.getJsonTo());
                List<JsonTrans> list_from= JSON.parseArray(map_from.get("labels").toString(), JsonTrans.class);
                List<JsonTrans> list_to= JSON.parseArray(map_to.get("labels").toString(), JsonTrans.class);
                tempList.add(list_from);
                tempList.add(list_to);
            }catch (Exception e){}
            list.add(tempList);
        }
        Map<String, Object> dataList = new HashMap<String, Object>();
        dataList.put("TITLE", "古医药异文导出");
        dataList.put("TABLEDATA", list);
        exportCheckWord(dataList, path+name);//放入本地文件
        /*导出到IO流*/
//        athleteName = path + "一级运动员称号公示" + currentMill + ".docx";
//       String athleteName = path + "export" + ".docx";
//        FileOutputStream fos = new FileOutputStream(path+name);
//        document.write(fos);
//        fos.close();
        FileUtil.downloadFile(response, path+name);
    }
}
