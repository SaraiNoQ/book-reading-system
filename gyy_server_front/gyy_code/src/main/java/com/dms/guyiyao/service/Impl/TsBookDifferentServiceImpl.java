package com.dms.guyiyao.service.Impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsBookDifferentMapper;
import com.dms.guyiyao.pojo.ContentDiff;
import com.dms.guyiyao.pojo.ContentOriginal;
import com.dms.guyiyao.pojo.Diffresult;
import com.dms.guyiyao.pojo.TsBookDifferent;
import com.dms.guyiyao.service.ITsBookDifferentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 异文信息关联表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TsBookDifferentServiceImpl extends ServiceImpl<TsBookDifferentMapper, TsBookDifferent> implements ITsBookDifferentService {
@Autowired
TsBookDifferentMapper differentMapper;
    @Override
    public List<ContentDiff> getDiff(String chapterId,String tarBook) {
      List<ContentDiff> list=differentMapper.getAllDiffInfo(chapterId,tarBook);
        return list;
          }



}



