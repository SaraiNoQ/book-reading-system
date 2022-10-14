package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.book.TsBookOrginal;
import com.dms.guyiyao.pojo.original.ContentOriginal;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 医书原文表 Mapper 接口
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Transactional
public interface TsBookOrginalMapper extends BaseMapper<TsBookOrginal> {

    List<TsBookOrginal> getPicUrl(String chapterId);

    int deleteImgUrl(String imgId);

    int addImg(String chapterId, String imgName, String imgUrl);

    int addMatch_1(String contentId,String chapterId,String content,int sequence);
    int addMatch_2(String contentId,String chapterId,String content, String imgId,int sequence);
    List<String> getOriginalId(String chapterId);
    List<ContentOriginal> getOriginal(String originalId);
    int deleteMatch(String chapterId,int sequence);
}
