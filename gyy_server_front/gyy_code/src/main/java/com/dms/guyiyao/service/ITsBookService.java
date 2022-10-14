package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.PageV1;
import com.dms.guyiyao.pojo.TsBook;

/**
 * <p>
 * 医药典籍总体概述表 服务类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface ITsBookService extends IService<TsBook> {
    PageV1 getIndexBookInfo(Integer page,Integer size);

}
