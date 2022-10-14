package com.dms.guyiyao.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsBookContentMapper;
import com.dms.guyiyao.pojo.ContentDiff;
import com.dms.guyiyao.pojo.ContentOriginal;
import com.dms.guyiyao.pojo.TsBookContent;
import com.dms.guyiyao.service.ITsBookContentService;
import com.dms.guyiyao.utils.Loggers;
import com.sun.media.jfxmedia.logging.Logger;
import org.apache.commons.collections.EnumerationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 医书内容表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TsBookContentServiceImpl extends ServiceImpl<TsBookContentMapper, TsBookContent> implements ITsBookContentService {

    @Autowired
    TsBookContentMapper bookContentMapper;
    @Autowired
    TsBookDifferentServiceImpl differentService;
    @Autowired
    TsBookOrginalServiceImpl orginalService;

    @Override
    public LinkedList<String[]> getContent_diff(String chapterId,String tarBook) {
        String content = bookContentMapper.getContent(chapterId);
        List<ContentDiff> diff = differentService.getDiff(chapterId,tarBook);
        Loggers.info("获取到当前章节内容异文【"+(diff==null?0:diff.size())+"】条");
        for (ContentDiff contentDiff : diff) {
            if (content.contains(contentDiff.getContentFrom())) {
                content = content.replace(contentDiff.getContentFrom(), "##"+contentDiff.getContentFrom() + "##");
            }
            }
            content=content==null?"":content;
            String content_[] = content.split("\\r\\n|\\\\r\\\\n");
            LinkedList<String[]> row = new LinkedList();
            for (String s : content_) {
                String[] data = s.split("##");
                row.add(data);
            }
            Loggers.info("内容共有【"+row.size()+"】段");
            return row;
        }
        public LinkedList<String[]> getContent_original(String chapterId) {
        String content = bookContentMapper.getContent(chapterId);
        List<ContentOriginal> diff = orginalService.getOriginal(chapterId);
            return null;
        }

    public static void main(String[] args) {
        System.out.println("hello");
        String str="aaa\\r\\nbbbbcccddd";
        System.out.println(str);
        String[] split = str.split("\\\\r\\\\n");
        for (String s : split) {
            System.out.println(s);
        }
    }

}
