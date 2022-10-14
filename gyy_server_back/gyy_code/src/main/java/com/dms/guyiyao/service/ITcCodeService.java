package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.TcCode;

import java.util.List;

/**
 * <p>
 * 基础码表 服务类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface ITcCodeService extends IService<TcCode> {

    List<String> getDynasty();
}
