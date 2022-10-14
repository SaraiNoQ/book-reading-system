package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.book.TsBookDifferent;
import com.dms.guyiyao.pojo.diff.DiffItem;
import com.dms.guyiyao.pojo.diff.DiffJson;
import com.dms.guyiyao.pojo.diff.diff_From_Excel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 异文信息关联表 服务类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface ITsBookDifferentService extends IService<TsBookDifferent> {
    int addDiff(String contentFrom, String contentTo, String chapterIdFrom, String chapterIdTo);


    List<DiffItem> getDiff(String chapterId);

    String deleteDiff(String diffId);

    String updateDiff(String diffId, String contentFrom, String contentTo);

    String updateHighlight(String diffId,String jsonFrom, String jsonTo);

    DiffJson getHighlight(String diffId);

    int addDiffByexcel(String bookFrom,String bookTo,MultipartFile diffFrom) throws IOException, Exception;
    Map<String, diff_From_Excel> getInfo(MultipartFile file,String bookCode,String dataFrom) throws Exception;

//    int ExcelAdd(MultipartFile file) throws IOException, ParseException;
}
