package com.dms.guyiyao.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsBookOrginalMapper;
import com.dms.guyiyao.pojo.ContentOriginal;
import com.dms.guyiyao.pojo.ContentOriginal_total;
import com.dms.guyiyao.pojo.TsBookOrginal;
import com.dms.guyiyao.service.ITsBookOrginalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 医书原文表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TsBookOrginalServiceImpl extends ServiceImpl<TsBookOrginalMapper, TsBookOrginal> implements ITsBookOrginalService {
@Autowired
 TsBookOrginalMapper orginalMapper;
    @Override
    public List<ContentOriginal> getOriginal(String chapterId) {
       List<String>originalsIds=orginalMapper.getOriginalId(chapterId);
        List data=new LinkedList<>();
       for (String original : originalsIds) {
           List<ContentOriginal> contentOriginal= orginalMapper.getOriginal(original);
          data.add(contentOriginal);
        }
        return data;
    }
}
