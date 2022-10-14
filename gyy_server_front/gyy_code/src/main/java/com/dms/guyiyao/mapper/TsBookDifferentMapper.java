package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.ContentDiff;
import com.dms.guyiyao.pojo.Diffresult;
import com.dms.guyiyao.pojo.TsBookDifferent;

import java.util.List;

/**
 * <p>
 * 异文信息关联表 Mapper 接口
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface TsBookDifferentMapper extends BaseMapper<TsBookDifferent> {
    List<ContentDiff> getAllDiffInfo(String chapterId,String tarBook);
}
