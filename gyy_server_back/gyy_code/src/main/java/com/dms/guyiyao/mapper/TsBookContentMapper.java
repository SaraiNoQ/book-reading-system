package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.ContentType;
import com.dms.guyiyao.pojo.book.TsBookContent;
import com.dms.guyiyao.pojo.book.TsBookContentDb;

import java.util.List;

/**
 * <p>
 * 医书内容表 Mapper 接口
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface TsBookContentMapper extends BaseMapper<TsBookContent> {
    List<TsBookContent> getContent(String chapterId);

    int deleteContent(String contentId,String opUser,String opTime);

    int addContent(String chapterId, String content, int sequence, String type,String opTime,String opUser);

    int updateContent(String contentId, int sequence,String content, String type,String opTime,String opUser);

    int addContentType(String chapter_id, String content, int sequence, String labels,String createTime,String opUser);

    int updateContentType(String chapter_id, int sequence, String labels, String time,String opUser);

    List<ContentType> getContentType(String chapterId);

    int deleteContentType(String chapterId, int sequence,String opTime,String opUser);

    int containContent(String chapterId);

    List<TsBookContentDb> getContentDb(String bookid);

    int updateContentByChapterId(String chapterid, String content, String type, String opTime, String opUser);
}
