package com.dms.guyiyao.service.Impl;

import com.dms.guyiyao.dao.SeachDao;
import com.dms.guyiyao.mapper.TsBookChapterMapper;
import com.dms.guyiyao.pojo.SearchPage;
import com.dms.guyiyao.pojo.word.Book_Chapter;
import com.dms.guyiyao.service.SearchContentService;
import com.dms.guyiyao.utils.FileUtil;
import com.dms.guyiyao.utils.XWPFHelper;
import lombok.val;
import org.apache.lucene.search.TotalHits;
import org.apache.poi.xwpf.usermodel.*;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

@Service
public class SearchContentImpl implements SearchContentService {
    @Autowired
    private RestHighLevelClient client;
@Autowired
    private SeachDao seachDao;
@Autowired
    private TsBookChapterMapper chapterMapper;
    @Override
    public List<Map<String, Object>> searchContent(String keyword, String bookName[],String[] type, String searchType,int page, int size) throws IOException {

       SearchRequest searchRequest = new SearchRequest("content");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
//       多条件查询
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        BoolQueryBuilder boolQueryBuilder_bookName=new BoolQueryBuilder();

        // 固定查询
        MatchQueryBuilder queryBuilder_content_1 =null;
        MatchPhraseQueryBuilder queryBuilder_content_2=null;
        if (searchType.equals("分词"))queryBuilder_content_1=new MatchQueryBuilder("content", keyword);
        if (searchType.equals("不分"))queryBuilder_content_2= QueryBuilders.matchPhraseQuery("content", keyword);

        TermQueryBuilder termQueryBuilder_status = new TermQueryBuilder("status", 0);

        for (int i = 0; i < bookName.length&&!bookName[i].equals("ALL"); i++) {
            boolQueryBuilder_bookName.should(new TermQueryBuilder("bookname.keyword",bookName[i]));
            if (i==bookName.length-1)boolQueryBuilder.must(boolQueryBuilder_bookName);//最后一次循环加入上一级布尔查询
        }

        if (searchType.equals("分词"))boolQueryBuilder.must(queryBuilder_content_1).must(termQueryBuilder_status);
        if (searchType.equals("不分"))boolQueryBuilder.must(queryBuilder_content_2).must(termQueryBuilder_status);

        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchSourceBuilder.query(boolQueryBuilder);
        String[] includeFields = new String[] {"id","content", "sequence","type","bookname","ts_book_chapter_id"};
        String[] excludeFields = new String[] {""};
        searchSourceBuilder.fetchSource(includeFields,excludeFields);
        // 分页,前端页面从1开始
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);
        // 高亮 =========
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        if (searchType.equals("分词"))highlightBuilder.field("content");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 执行查询
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit documentFields : hits.getHits()) {

            // 使用新的字段值（高亮），覆盖旧的字段值
            Map<String, Object> sourceAsMap =  documentFields.getSourceAsMap();
//从数据库中添加书名字段
//            sourceAsMap.put("bookName",seachDao.getBookNameByContentId((String)sourceAsMap.get("id")));

            // 高亮字段
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField content = highlightFields.get("content");
            // 替换
            if (content != null) {//精确匹配时此字段为null
                Text[] fragments = content.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
//                    System.out.println(text);
                    new_name.append(text);
                }
//                if (searchType.equals("不分"))sourceAsMap.put("content",((String)sourceAsMap.get("content")).replaceAll(keyword,"<span style='color:red'>"+keyword+"</span>")) ;
//                else
                    sourceAsMap.put("content", new_name.toString());
            }else {
                sourceAsMap.put("content",((String)sourceAsMap.get("content")).replaceAll(keyword,"<span style='color:red'>"+keyword+"</span>")) ;
            }
            sourceAsMap.put("total", hits.getTotalHits().value);
            results.add(sourceAsMap);
        }

//        System.out.println(results.size());
//        for (Map<String, Object> result : results) {
//            for (String s : result.keySet()) {
//                System.out.println(s + ":" + result.get(s));
//            }
//        }
        return results;
    }

    @Override
    public List<Map<String, Object>> searchContent_Main_MH(String keyword, String bookName[], String[] type,String searchType, int page, int size) throws IOException {
        if (type[0].equals("ALL")){
            return searchContent(keyword,bookName,type,searchType,page,size);
        }
        SearchRequest searchRequest = new SearchRequest("content_type");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
//       多条件查询
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // 固定查询
        MatchQueryBuilder queryBuilder_content_1 =null;
        MatchPhraseQueryBuilder queryBuilder_content_2=null;



        TermQueryBuilder termQueryBuilder_1 = new TermQueryBuilder("status", 0);
        if (searchType.equals("不分"))queryBuilder_content_2= QueryBuilders.matchPhraseQuery("content", keyword);
        if (searchType.equals("分词"))queryBuilder_content_1=new MatchQueryBuilder("content", keyword);


//        boolQueryBuilder.must(queryBuilder).must(termQueryBuilder_1);
        if (searchType.equals("分词"))boolQueryBuilder.must(queryBuilder_content_1).must(termQueryBuilder_1);
        if (searchType.equals("不分"))boolQueryBuilder.must(queryBuilder_content_2).must(termQueryBuilder_1);
//        可变条件构造器
       BoolQueryBuilder boolQueryBuilder_book=new BoolQueryBuilder();
       BoolQueryBuilder boolQueryBuilder_type=new BoolQueryBuilder();
//        如果类型没有要求就扫content库

//有要求就扫描content_type库
        for (String s : bookName) {
            if (s.equals("ALL"))break;
            boolQueryBuilder_book.should(new TermQueryBuilder("bookname.keyword", s));
        }
//        添加类内容条件筛选,存在一定的bug，如果存在 es 中有三种类型 ，用户查询两个类型。三种类型的也会被查出来
        for (String s : type) {
            if (s.equals("ALL"))break;
           if (type.length==1)boolQueryBuilder_type.must(new MatchQueryBuilder("label.keyword",s));
           if (type.length!=1)boolQueryBuilder_type.must(new MatchQueryBuilder("label",s));
        }

        boolQueryBuilder.must(boolQueryBuilder_book).must(boolQueryBuilder_type);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchSourceBuilder.query(boolQueryBuilder);
        String[] includeFields = new String[] {"id","content", "sequence","label","bookname","chapterid"};
        String[] excludeFields = new String[] {""};
        searchSourceBuilder.fetchSource(includeFields,excludeFields);
        // 分页,前端页面从1开始
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);

        // 高亮 =========
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("content");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 执行查询
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit documentFields : hits.getHits()) {

            // 使用新的字段值（高亮），覆盖旧的字段值
            Map<String, Object> sourceAsMap =  documentFields.getSourceAsMap();
//从数据库中添加书名字段
//            sourceAsMap.put("bookName",seachDao.getBookNameByContentId((String)sourceAsMap.get("id")));

            // 高亮字段
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField content = highlightFields.get("content");
            // 替换
            if (content != null) {
                Text[] fragments = content.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
                    new_name.append(text);
                }
                if (searchType.equals("不分"))sourceAsMap.put("content",((String)sourceAsMap.get("content")).replaceAll(keyword,"<span style='color:red'>"+keyword+"</span>")) ;
                else sourceAsMap.put("content", new_name.toString());
            }
            sourceAsMap.put("total", hits.getTotalHits().value);
            results.add(sourceAsMap);
        }


//        System.out.println(results.size());
//        for (Map<String, Object> result : results) {
//            for (String s : result.keySet()) {
//                System.out.println(s + ":" + result.get(s));
//            }
//        }
        return results;
    }
    String path = System.getProperty("user.dir")+"/";
    String name="tmp.docx";
    @Override
    public void exportSearch(HttpServletResponse response, String keyword, String[] bookName, String[] type, List<Map<String, Object>> result) {
        XWPFDocument xwpfDocument=new XWPFDocument();
        XWPFParagraph title = xwpfDocument.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = title.createRun();
        /*标题设置*/
        run.setText("高级搜索导出");
        run.setBold(true);
        run.setFontSize(35);
        run.addBreak();
        XWPFParagraph searchInfo = xwpfDocument.createParagraph();

//设置内容左对齐
        searchInfo.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run1 = searchInfo.createRun();

        run1.setFontSize(15);
        run1.setUnderline(UnderlinePatterns.DASH);//关键信息加虚线
        run1.setText("关键字："+keyword);run1.addBreak();
        String books="";
        for (String s : bookName) {
            books+=s+",";
        }

        if (!books.equals("ALL,")){
        run1.setText("搜索图书:"+books.substring(0,books.length()-1));run1.addBreak();
        }else {
            run1.setText("搜索图书:无限制");run1.addBreak();
        }
        String types="";
        for (String s : type) {
            types+=s+",";
        }
        if (!types.equals("ALL,")){
     run1.setText("内容类型:"+types.substring(0,types.length()-1));run1.addBreak();}
        else {run1.setText("内容类型:无限制");run1.addBreak();}
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);
      run1.setText("搜索时间:"+timeStr);
      run1.addBreak();run1.addBreak();

        XWPFParagraph body = xwpfDocument.createParagraph();
        XWPFRun run2 = body.createRun();
        int count=1;
        for (Map<String, Object> map : result) {
            run2.setColor("000000");
            run2.setFontSize(30);
            run2.setText("No."+count++);
           run2.setText("-----------------------------------------------------------------------------------");run2.addBreak();
            if (!map.containsKey("label")) {
            Book_Chapter ts_book_chapter_id = chapterMapper.getBookAndChapter((String) map.get("ts_book_chapter_id"));
            if (ts_book_chapter_id==null)ts_book_chapter_id=new Book_Chapter();
                run2.setText("来源:" +"《"+ts_book_chapter_id.getBookname()+"》_"+ts_book_chapter_id.getChaptername());
                run2.addBreak();
            }else {
                Book_Chapter chapterid = chapterMapper.getBookAndChapter((String) map.get("chapterid"));
                run2.setText("来源:" +"《"+chapterid.getBookname()+"》_"+chapterid.getChaptername());
                run2.addBreak();
            }

            if (!map.containsKey("label")){
                if (map.get("type")!=null&&!map.get("type").equals("0")){
            run2.setText("类型:"+map.get("type"));run2.addBreak();
                }else {
                    run2.setText("类型:无限制");run2.addBreak();
                }
            }else {
                run2.setText("类型:"+map.get("label"));run2.addBreak();
            }
          run2.setText("内容");run2.addBreak();
           run2.setFontSize(15);//正文黑体字大小
            String content = (String) map.get("content");
            String []contents=content.split("<span style='color:red'>");
            for (int i=0;i<contents.length;i++) {
                run2 = body.createRun();

                if (contents[i].contains("</span>")){
                    run2.setFontSize(20);//正文红色字体大小
                    run2.setColor("FF0000");
                    run2.setText(contents[i].split("</span>")[0]);
                   if (contents[i].split("</span>").length!=1) {
                       run2 = body.createRun();
                       run2.setFontSize(15);
                       run2.setText(contents[i].split("</span>")[1]);
                   }
                }else {
                    run2.setColor("000000");
                    run2.setFontSize(15);
                    run2.setText(contents[i]);
                }
          if (i==contents.length-1)run2.addBreak();//最后一次拼接后换行
            }run2.addBreak();
        }
        /*文件导入本地并导出到web端口*/
        try {
            saveDocument(xwpfDocument,path+name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileUtil.downloadFile(response,path+name);
    }

    @Override
    public List<Map<String, Object>> searchDiff(String keyword,String bookFrom, String bookTo, String searchType, int page, int size) throws IOException {
        SearchRequest searchRequest = new SearchRequest("diff");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
//       多条件查询
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        /*
        * 关键字
        * */
            BoolQueryBuilder boolQueryBuilder_keyword = new BoolQueryBuilder();
            if (searchType.equals("不分")){
              boolQueryBuilder_keyword.should(QueryBuilders.matchPhraseQuery("difffrom",keyword));
              boolQueryBuilder_keyword.should(QueryBuilders.matchPhraseQuery("diffto",keyword));
        }else{
            boolQueryBuilder_keyword.should(new MatchQueryBuilder("difffrom",keyword));
            boolQueryBuilder_keyword.should(new MatchQueryBuilder("diffto",keyword));
        }
        boolQueryBuilder.must(boolQueryBuilder_keyword);
        /*
        * BookName
        * */
        BoolQueryBuilder boolQueryBuilder_book = new BoolQueryBuilder();
        if (!bookFrom.equals(""))boolQueryBuilder_book.must(new MatchQueryBuilder("bookfrom.keyword",bookFrom));
        if (!bookTo.equals(""))boolQueryBuilder_book.must(new MatchQueryBuilder("bookto.keyword",bookTo));
        boolQueryBuilder.must(boolQueryBuilder_book);
  /*
  * 其他条件
  * */
        searchSourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));
        searchSourceBuilder.query(boolQueryBuilder);
        String[] includeFields = new String[] {"id","difffrom","bookfrom","chapterfromid","chapterfrom","jsonfrom","diffto","jsonto","bookto","chaptertoid","chapterto"};
        String[] excludeFields = new String[] {""};
        searchSourceBuilder.fetchSource(includeFields,excludeFields);
        // 分页,前端页面从1开始
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);
        // 高亮 =========
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("difffrom");
        highlightBuilder.field("diffto");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 执行查询
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit documentFields : hits.getHits()) {

            // 使用新的字段值（高亮），覆盖旧的字段值
            Map<String, Object> sourceAsMap =  documentFields.getSourceAsMap();
//从数据库中添加书名字段
//            sourceAsMap.put("bookName",seachDao.getBookNameByContentId((String)sourceAsMap.get("id")));
            sourceAsMap.put("total",hits.getTotalHits().value);
            // 高亮字段
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField difffrom = highlightFields.get("difffrom");
            HighlightField diffto = highlightFields.get("diffto");
            // 替换
            if (difffrom != null) {
                Text[] fragments = difffrom.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
                    new_name.append(text);
                }
                if (searchType.equals("不分"))sourceAsMap.put("difffrom",((String)sourceAsMap.get("difffrom")).replaceAll(keyword,"<span style='color:red'>"+keyword+"</span>")) ;
                else sourceAsMap.put("difffrom", new_name.toString());
            }
            if (diffto != null) {
                Text[] fragments = diffto.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
                    new_name.append(text);
                }
                if (searchType.equals("不分"))sourceAsMap.put("diffto",((String)sourceAsMap.get("diffto")).replaceAll(keyword,"<span style='color:red'>"+keyword+"</span>")) ;
                else sourceAsMap.put("diffto", new_name.toString());
            }




            sourceAsMap.put("total", hits.getTotalHits().value);
            results.add(sourceAsMap);
        }


//        System.out.println(results.size());
//        for (Map<String, Object> result : results) {
//            for (String s : result.keySet()) {
//                System.out.println(s + ":" + result.get(s));
//            }
//        }
        return results;
    }


    public void saveDocument(XWPFDocument document, String savePath) throws IOException {
        OutputStream os = new FileOutputStream(savePath);
        document.write(os);
        os.close();
    }
}