package com.dms.guyiyao.service.Impl;

import com.dms.guyiyao.mapper.TsBookDifferentMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncService {
    @Autowired
    TsBookDifferentMapper bookDifferentMapper;

    @SneakyThrows
    @Async("asyncPoolTaskExecutor")
    public void DiffAdd_IO(String contentFrom_uuid,String contentTo_uuid, String contentFrom, String contentTo, String chapterIdFrom, String chapterIdTo,String datetime,String opUser){
        bookDifferentMapper.addDiffContent(contentFrom_uuid,contentFrom,chapterIdFrom,datetime,opUser);
        bookDifferentMapper.addDiffContent(contentTo_uuid,contentTo,chapterIdTo,datetime,opUser);
        bookDifferentMapper.addConnection(contentFrom_uuid,contentTo_uuid,datetime,opUser);
    }
}
