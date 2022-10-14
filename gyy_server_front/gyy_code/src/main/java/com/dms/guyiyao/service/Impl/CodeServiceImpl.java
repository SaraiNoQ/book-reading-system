package com.dms.guyiyao.service.Impl;

import com.dms.guyiyao.mapper.TcCodeMapper;
import com.dms.guyiyao.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CodeServiceImpl implements CodeService {
   @Autowired
   private TcCodeMapper codeMapper;
    @Override
    public List<String> getContentType() {
        return codeMapper.getContentType();
    }
}
