package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.TcCode;

import java.util.List;

/**
 * <p>
 * 基础码表 Mapper 接口
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface TcCodeMapper extends BaseMapper<TcCode> {
    List<String> getContentType();
}
