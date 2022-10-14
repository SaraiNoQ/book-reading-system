package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.TsBookContent;

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
    /*通过书名和章节获取内容*/
String getContent(String chapterId);





}
