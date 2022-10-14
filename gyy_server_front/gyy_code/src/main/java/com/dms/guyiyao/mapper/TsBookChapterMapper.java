package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.CataLog;
import com.dms.guyiyao.pojo.TreeNode;
import com.dms.guyiyao.pojo.TsBookChapter;
import com.dms.guyiyao.pojo.word.Book_Chapter;

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
//List<CataLog>getChildFromDB(String id);
//CataLog getRootList(String bookName, int grade);
    List<CataLog> getChapter(String code);
    List<CataLog> getChapter_grade_1(String bookName);

    List<TreeNode> getChapterNode(String bookName);
    Book_Chapter getBookAndChapter(String chapterid);
}
