package com.dms.guyiyao.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TcCodeMapper;
import com.dms.guyiyao.pojo.TcCode;
import com.dms.guyiyao.service.ITcCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 基础码表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TcCodeServiceImpl extends ServiceImpl<TcCodeMapper, TcCode> implements ITcCodeService {
@Autowired
private TcCodeMapper codeMapper;
    @Override
    public List<String> getDynasty() {
      return   codeMapper.getDynasty();
    }
}
