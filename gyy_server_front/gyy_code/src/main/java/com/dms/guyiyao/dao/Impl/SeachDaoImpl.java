package com.dms.guyiyao.dao.Impl;

import com.dms.guyiyao.dao.SeachDao;
import com.dms.guyiyao.mapper.SeachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SeachDaoImpl implements SeachDao {
@Autowired
    SeachMapper seachMapper;

    @Override
    public String getBookNameByContentId(String contentId) {
        return seachMapper.seachBookNameByContentId(contentId);
    }
}
