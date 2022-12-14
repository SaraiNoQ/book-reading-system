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
//       ???????????????
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        BoolQueryBuilder boolQueryBuilder_bookName=new BoolQueryBuilder();

        // ????????????
        MatchQueryBuilder queryBuilder_content_1 =null;
        MatchPhraseQueryBuilder queryBuilder_content_2=null;
        if (searchType.equals("??????"))queryBuilder_content_1=new MatchQueryBuilder("content", keyword);
        if (searchType.equals("??????"))queryBuilder_content_2= QueryBuilders.matchPhraseQuery("content", keyword);

        TermQueryBuilder termQueryBuilder_status = new TermQueryBuilder("status", 0);

        for (int i = 0; i < bookName.length&&!bookName[i].equals("ALL"); i++) {
            boolQueryBuilder_bookName.should(new TermQueryBuilder("bookname.keyword",bookName[i]));
            if (i==bookName.length-1)boolQueryBuilder.must(boolQueryBuilder_bookName);//?????????????????????????????????????????????
        }

        if (searchType.equals("??????"))boolQueryBuilder.must(queryBuilder_content_1).must(termQueryBuilder_status);
        if (searchType.equals("??????"))boolQueryBuilder.must(queryBuilder_content_2).must(termQueryBuilder_status);

        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchSourceBuilder.query(boolQueryBuilder);
        String[] includeFields = new String[] {"id","content", "sequence","type","bookname","ts_book_chapter_id"};
        String[] excludeFields = new String[] {""};
        searchSourceBuilder.fetchSource(includeFields,excludeFields);
        // ??????,???????????????1??????
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);
        // ?????? =========
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        if (searchType.equals("??????"))highlightBuilder.field("content");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // ????????????
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // ???????????? ==========
        SearchHits hits = searchResponse.getHits();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit documentFields : hits.getHits()) {

            // ?????????????????????????????????????????????????????????
            Map<String, Object> sourceAsMap =  documentFields.getSourceAsMap();
//?????????????????????????????????
//            sourceAsMap.put("bookName",seachDao.getBookNameByContentId((String)sourceAsMap.get("id")));

            // ????????????
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField content = highlightFields.get("content");
            // ??????
            if (content != null) {//???????????????????????????null
                Text[] fragments = content.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
//                    System.out.println(text);
                    new_name.append(text);
                }
//                if (searchType.equals("??????"))sourceAsMap.put("content",((String)sourceAsMap.get("content")).replaceAll(keyword,"<span style='color:red'>"+keyword+"</span>")) ;
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
//       ???????????????
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // ????????????
        MatchQueryBuilder queryBuilder_content_1 =null;
        MatchPhraseQueryBuilder queryBuilder_content_2=null;



        TermQueryBuilder termQueryBuilder_1 = new TermQueryBuilder("status", 0);
        if (searchType.equals("??????"))queryBuilder_content_2= QueryBuilders.matchPhraseQuery("content", keyword);
        if (searchType.equals("??????"))queryBuilder_content_1=new MatchQueryBuilder("content", keyword);


//        boolQueryBuilder.must(queryBuilder).must(termQueryBuilder_1);
        if (searchType.equals("??????"))boolQueryBuilder.must(queryBuilder_content_1).must(termQueryBuilder_1);
        if (searchType.equals("??????"))boolQueryBuilder.must(queryBuilder_content_2).must(termQueryBuilder_1);
//        ?????????????????????
       BoolQueryBuilder boolQueryBuilder_book=new BoolQueryBuilder();
       BoolQueryBuilder boolQueryBuilder_type=new BoolQueryBuilder();
//        ??????????????????????????????content???

//??????????????????content_type???
        for (String s : bookName) {
            if (s.equals("ALL"))break;
            boolQueryBuilder_book.should(new TermQueryBuilder("bookname.keyword", s));
        }
//        ???????????????????????????,???????????????bug??????????????? es ?????????????????? ???????????????????????????????????????????????????????????????
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
        // ??????,???????????????1??????
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);

        // ?????? =========
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("content");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // ????????????
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // ???????????? ==========
        SearchHits hits = searchResponse.getHits();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit documentFields : hits.getHits()) {

            // ?????????????????????????????????????????????????????????
            Map<String, Object> sourceAsMap =  documentFields.getSourceAsMap();
//?????????????????????????????????
//            sourceAsMap.put("bookName",seachDao.getBookNameByContentId((String)sourceAsMap.get("id")));

            // ????????????
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField content = highlightFields.get("content");
            // ??????
            if (content != null) {
                Text[] fragments = content.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
                    new_name.append(text);
                }
                if (searchType.equals("??????"))sourceAsMap.put("content",((String)sourceAsMap.get("content")).replaceAll(keyword,"<span style='color:red'>"+keyword+"</span>")) ;
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
        /*????????????*/
        run.setText("??????????????????");
        run.setBold(true);
        run.setFontSize(35);
        run.addBreak();
        XWPFParagraph searchInfo = xwpfDocument.createParagraph();

//?????????????????????
        searchInfo.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run1 = searchInfo.createRun();

        run1.setFontSize(15);
        run1.setUnderline(UnderlinePatterns.DASH);//?????????????????????
        run1.setText("????????????"+keyword);run1.addBreak();
        String books="";
        for (String s : bookName) {
            books+=s+",";
        }

        if (!books.equals("ALL,")){
        run1.setText("????????????:"+books.substring(0,books.length()-1));run1.addBreak();
        }else {
            run1.setText("????????????:?????????");run1.addBreak();
        }
        String types="";
        for (String s : type) {
            types+=s+",";
        }
        if (!types.equals("ALL,")){
     run1.setText("????????????:"+types.substring(0,types.length()-1));run1.addBreak();}
        else {run1.setText("????????????:?????????");run1.addBreak();}
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);
      run1.setText("????????????:"+timeStr);
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
                run2.setText("??????:" +"???"+ts_book_chapter_id.getBookname()+"???_"+ts_book_chapter_id.getChaptername());
                run2.addBreak();
            }else {
                Book_Chapter chapterid = chapterMapper.getBookAndChapter((String) map.get("chapterid"));
                run2.setText("??????:" +"???"+chapterid.getBookname()+"???_"+chapterid.getChaptername());
                run2.addBreak();
            }

            if (!map.containsKey("label")){
                if (map.get("type")!=null&&!map.get("type").equals("0")){
            run2.setText("??????:"+map.get("type"));run2.addBreak();
                }else {
                    run2.setText("??????:?????????");run2.addBreak();
                }
            }else {
                run2.setText("??????:"+map.get("label"));run2.addBreak();
            }
          run2.setText("??????");run2.addBreak();
           run2.setFontSize(15);//?????????????????????
            String content = (String) map.get("content");
            String []contents=content.split("<span style='color:red'>");
            for (int i=0;i<contents.length;i++) {
                run2 = body.createRun();

                if (contents[i].contains("</span>")){
                    run2.setFontSize(20);//????????????????????????
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
          if (i==contents.length-1)run2.addBreak();//???????????????????????????
            }run2.addBreak();
        }
        /*??????????????????????????????web??????*/
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
//       ???????????????
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        /*
        * ?????????
        * */
            BoolQueryBuilder boolQueryBuilder_keyword = new BoolQueryBuilder();
            if (searchType.equals("??????")){
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
  * ????????????
  * */
        searchSourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));
        searchSourceBuilder.query(boolQueryBuilder);
        String[] includeFields = new String[] {"id","difffrom","bookfrom","chapterfromid","chapterfrom","jsonfrom","diffto","jsonto","bookto","chaptertoid","chapterto"};
        String[] excludeFields = new String[] {""};
        searchSourceBuilder.fetchSource(includeFields,excludeFields);
        // ??????,???????????????1??????
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);
        // ?????? =========
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("difffrom");
        highlightBuilder.field("diffto");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // ????????????
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // ???????????? ==========
        SearchHits hits = searchResponse.getHits();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit documentFields : hits.getHits()) {

            // ?????????????????????????????????????????????????????????
            Map<String, Object> sourceAsMap =  documentFields.getSourceAsMap();
//?????????????????????????????????
//            sourceAsMap.put("bookName",seachDao.getBookNameByContentId((String)sourceAsMap.get("id")));
            sourceAsMap.put("total",hits.getTotalHits().value);
            // ????????????
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField difffrom = highlightFields.get("difffrom");
            HighlightField diffto = highlightFields.get("diffto");
            // ??????
            if (difffrom != null) {
                Text[] fragments = difffrom.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
                    new_name.append(text);
                }
                if (searchType.equals("??????"))sourceAsMap.put("difffrom",((String)sourceAsMap.get("difffrom")).replaceAll(keyword,"<span style='color:red'>"+keyword+"</span>")) ;
                else sourceAsMap.put("difffrom", new_name.toString());
            }
            if (diffto != null) {
                Text[] fragments = diffto.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
                    new_name.append(text);
                }
                if (searchType.equals("??????"))sourceAsMap.put("diffto",((String)sourceAsMap.get("diffto")).replaceAll(keyword,"<span style='color:red'>"+keyword+"</span>")) ;
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