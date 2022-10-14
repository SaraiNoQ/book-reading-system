package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.pojo.SearchPage;
import com.dms.guyiyao.service.SearchContentService;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class EsTest {
    @Autowired
    private SearchContentService searchContentService;
    @Autowired
    private RestHighLevelClient client;
    @Test
    void getInfo() throws IOException {
        GetRequest getRequest=new GetRequest("jd1","1");
//    getRequest.fetchSourceContext(new FetchSourceContext(false));
//    getRequest.storedFields("_none_");
        GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(documentFields.getSourceAsString());
    }
    @Test
    void  searchKeyWord() throws IOException {

    String keyword="测试";
   int pageIndex=0;
   int pageSize=10;

        SearchRequest searchRequest = new SearchRequest("content");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        // 模糊查询
        MatchQueryBuilder queryBuilder=new MatchQueryBuilder("content",keyword);
        TermQueryBuilder termQueryBuilder=new TermQueryBuilder("status",0);
            boolQueryBuilder.must(queryBuilder).must(termQueryBuilder);

    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

    searchSourceBuilder.query(boolQueryBuilder);

    // 分页
    searchSourceBuilder.from(pageIndex);
    searchSourceBuilder.size(pageSize);
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
        Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
        // 高亮字段
        Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
        HighlightField content = highlightFields.get("content");
        // 替换
        if (content != null){
            Text[] fragments = content.fragments();
            StringBuilder new_name = new StringBuilder();
            for (Text text : fragments) {
                new_name.append(text);
            }
            sourceAsMap.put("content",new_name.toString());
        }
        results.add(sourceAsMap);
    }
        System.out.println(results.size());
        for (Map<String, Object> result : results) {
            for (String s : result.keySet()) {
                System.out.println(s+":"+result.get(s));
            }
        }
}
//@Test
//    public void testSearch() throws IOException {
//    List<Map<String, Object>> maps = searchContentService.searchContent("上","","0",1,2);
//    for (Map<String, Object> result : maps) {
//        for (String s : result.keySet()) {
//            System.out.println(s + ":" + result.get(s));
//        }
//    }
//    }
    @Test
    void diffSearch() throws IOException {
        for (Map<String, Object> map : searchContentService.searchDiff("治阴脱","备急千金要方(宋校版)","新雕孙真人千金方","不分", 1, 10)) {
            for (String s : map.keySet()) {
                System.out.print(s+":"+map.get(s));
            }
            System.out.println();
        }

    }

}
