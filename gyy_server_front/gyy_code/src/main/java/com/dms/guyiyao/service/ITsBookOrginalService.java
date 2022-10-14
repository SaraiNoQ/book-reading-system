package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.ContentOriginal;
import com.dms.guyiyao.pojo.TsBookOrginal;

import java.util.List;

/**
 * <p>
 * 医书原文表 服务类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface ITsBookOrginalService extends IService<TsBookOrginal> {

    List<ContentOriginal> getOriginal(String chapterId);
}
