package com.dms.guyiyao.controller;

import com.dms.guyiyao.service.ExportWord;
import com.dms.guyiyao.service.Impl.ExportWordImpl;
import io.swagger.annotations.Api;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Api(tags = "导出功能")
@Service
@RestController
public class WordExportController {
@Autowired
    ExportWord exportWord;
@PostMapping("/diff/export")
public void exportDiff(HttpServletResponse response, String chapterId, String tarBook) throws IOException {
    exportWord.ExportWord(response,chapterId,tarBook);
}


}
