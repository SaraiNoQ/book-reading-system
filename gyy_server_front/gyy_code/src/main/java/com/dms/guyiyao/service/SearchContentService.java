package com.dms.guyiyao.service;

import com.dms.guyiyao.pojo.SearchPage;
import org.springframework.context.annotation.Description;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SearchContentService {
    List<Map<String, Object>> searchContent(String keyword, String bookName[],String[] type ,String searchType,int page, int size) throws IOException;
    List<Map<String, Object>> searchContent_Main_MH(String keyword, String bookName[],String[] type,String searchType,int page, int size) throws IOException;
    void exportSearch(HttpServletResponse response, String keyword, String[] bookName, String[] type, List<Map<String, Object>> result);
    List<Map<String, Object>> searchDiff(String keyword,String bookFrom, String bookTo, String searchType, int page, int size) throws IOException;

//    List<Map<String, Object>> searchContent_Main_JQ(String keyword, String[] bookName, String[] type, int page, int size);

//    List<Map<String, Object>> searchContent_Main_JQ(String keyword, String[] bookName, String[] type, int page, int size);
}
