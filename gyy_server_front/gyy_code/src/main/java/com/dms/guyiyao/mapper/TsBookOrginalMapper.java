package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.ContentOriginal;
import com.dms.guyiyao.pojo.TsBookOrginal;

import java.util.List;

/**
 * <p>
 * 医书原文表 Mapper 接口
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface TsBookOrginalMapper extends BaseMapper<TsBookOrginal> {
     List<String> getOriginalId(String chapterId);
    List<ContentOriginal> getOriginal(String originalId);
}
