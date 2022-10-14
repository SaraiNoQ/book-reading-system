package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.TreeNode;
import com.dms.guyiyao.pojo.book.TsBookChapter;
import com.dms.guyiyao.pojo.chapter.CataLog;
import com.dms.guyiyao.pojo.chapter.Chapter_db;
import com.dms.guyiyao.pojo.chapter.FullCataLog;

import java.util.List;

/**
 * <p>
 * 医书序卷章节表 Mapper 接口
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface TsBookChapterMapper extends BaseMapper<TsBookChapter> {
 int addChapterByExcel(Chapter_db chapter);

// List<CataLog> getChapter_grade_1(String bookName);
// List<CataLog> getChapter(String id);

    FullCataLog getFullCataLog(String chapterId);


    int updateChpter(String id, String parentid, String chapterName, int sequence, String grade,String opTime,String opUser);

    int deleteChapter(String id,String opTime,String opUser);

    int addChapterByUser(String bookid,String parentid, String chapterName, int sequence, String grade,String opTime,String opUser);
    List<TreeNode>getChapterNode(String bookName);
    void addRootChapter(String id,String bookName);

    int updateRootChapter(String id, String bookName);
}
