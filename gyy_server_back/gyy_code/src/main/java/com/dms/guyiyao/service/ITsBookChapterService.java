package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.TreeNode;
import com.dms.guyiyao.pojo.book.TsBookChapter;
import com.dms.guyiyao.pojo.chapter.CataLog;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 医书序卷章节表 服务类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface ITsBookChapterService extends IService<TsBookChapter> {
//使用excel导入数据
    int ExcelAdd(MultipartFile file,String bookName) throws Exception;
    int getSequence(String chapter_id);
    String getParentId(String chapter_id);
//  获取后台目录信息
List<TreeNode> getChapter(String bookName);
String updateChapter(String id,String parentId,String chapterName,int sequence,String grade);

    String delete(String id);
    String addChapter(String bookId,String parentId, String chapterName, int sequence, String grade);
}
