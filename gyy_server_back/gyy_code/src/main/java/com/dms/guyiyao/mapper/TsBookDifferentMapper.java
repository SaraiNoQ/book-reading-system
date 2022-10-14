package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.book.TsBookDifferent;
import com.dms.guyiyao.pojo.diff.DiffJson;

import java.util.List;

/**
 * <p>
 * 异文信息关联表 Mapper 接口
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface TsBookDifferentMapper extends BaseMapper<TsBookDifferent> {

    int addDiffContent(String contentUuid, String content,String chapterId,String createtime,String opUser);
    int addConnection(String contentFrom_uuid, String contentTo_uuid,String createtime,String opUser);

//    通过章节id得出所有异文content的id
    List<String>getId(String chapterId);


    //    通过异文id获取所有的diff表id
    String diffItem(String contentId);
//通过diff id 获取所有正文内容
    String getDiffFrom(String diffId);
//通过diff id 获取所有异文内容
    String getDiffTo(String diffId);
//获取原文来源
    String getBookFrom(String diffId);
//获取异文来源
    String getBookTo(String diffId);

    String getChapterTo(String diffId);

    String getChapterFrom(String diffId);

    int deleteDiff(String diffId,String opTime,String opUser);

    int updateDiff(String diffId, String contentFrom, String contentTo);

    int updateHighLight(String diffId, String jsonFrom, String jsonTo);

    DiffJson getDiffJson(String diffId);
}
