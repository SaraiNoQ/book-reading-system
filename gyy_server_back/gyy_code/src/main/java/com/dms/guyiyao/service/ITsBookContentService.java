package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.ContentType;
import com.dms.guyiyao.pojo.book.TsBookContent;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 医书内容表 服务类
 * </p>
 *
  * @since 2022-01-18
 */
public interface ITsBookContentService extends IService<TsBookContent> {
    List<TsBookContent> getContent(String chapterId);

    String deleteContent(String contentId);

    String addContent(String chapterId, String content, int sequence, String type);

    String updateContent(String contentId,int sequence, String content, String type);

    String addContentType(String chapter_id, String content, int sequence, String[] label);

    String updateContentType(String chapter_id, int sequence, String[] label);

    List<ContentType> getContentType(String chapterId);

    String deleteContentType(String chapterId, int sequence);
    void getContentDb(HttpServletResponse response,String bookId) throws IOException;

    String updateContentDb(String bookid, MultipartFile infotable) throws IOException;
}
