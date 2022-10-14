package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.TsBookContent;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;

/**
 * <p>
 * 医书内容表 服务类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface ITsBookContentService extends IService<TsBookContent> {
LinkedList getContent_diff(String chapterId,String tarBook);


}
