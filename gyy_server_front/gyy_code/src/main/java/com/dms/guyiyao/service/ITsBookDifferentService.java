package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

import com.dms.guyiyao.pojo.ContentDiff;
import com.dms.guyiyao.pojo.ContentOriginal;
import com.dms.guyiyao.pojo.TsBookDifferent;
/**
 * <p>
 * 异文信息关联表 服务类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface ITsBookDifferentService extends IService<TsBookDifferent>  {
public List<ContentDiff> getDiff(String chapterId,String tarBook);//获取某个章节对应某本书的异文


}
