package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.CataLog;
import com.dms.guyiyao.pojo.TreeNode;
import com.dms.guyiyao.pojo.TsBookChapter;

import java.util.List;

/**
 * <p>
 * 医书序卷章节表 服务类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface ITsBookChapterService extends IService<TsBookChapter> {

   List <TreeNode> getChapter(String bookName);

}
