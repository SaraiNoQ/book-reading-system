package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.ReturnValue;
import com.dms.guyiyao.pojo.book.TsBookOrginal;
import com.dms.guyiyao.pojo.original.ContentOriginal;
import com.dms.guyiyao.pojo.original.ImgForm;
import com.dms.guyiyao.pojo.original.OriginalReturn;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 医书原文表 服务类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface ITsBookOrginalService extends IService<TsBookOrginal> {
//    ReturnValue uploadOriginalImgFile(MultipartFile zipFile, String chapterId);
    List<TsBookOrginal> getPicUrl(String ChapterId);
    int deleteImgUrl(String imgId,String imgUrl) ;

    String uploadImg(ImgForm imgForm);

    String addMatch(String chapterId,String content, String[] imgId,int sequence);

    Map<Integer, OriginalReturn> getOriginal(String chapterId);

    String deleteMathch(String chapterId,int sequence);
//    void uploadBookDB(String book ,String url);
String FtpUpload(String fileName,
                 String dirPath, InputStream inputStream);

}
