package com.dms.guyiyao.service;

import org.apache.http.HttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExportWord {
    public void exportCheckWord(Map<String, Object> dataList, String savePath) throws IOException, IOException;

    void ExportWord(HttpServletResponse response, String chapterId, String tarBook) throws IOException;
    void ExportWord_v2(HttpServletResponse response,List<List<Object>> list) throws IOException;
}
