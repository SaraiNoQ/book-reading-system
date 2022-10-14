package com.dms.guyiyao.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dms.guyiyao.mapper.TsBookMapper;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.ReturnCodeAndMsgEnum;
import com.dms.guyiyao.pojo.ReturnValue;
import com.dms.guyiyao.pojo.TsBook;
import com.dms.guyiyao.pojo.TsUser;
import com.dms.guyiyao.service.UploadService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    TsBookMapper bookMapper;
    @Autowired
    TsUserMapper userMapper;
     private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);
    @Override
    public ReturnValue uploadUserImgFile(MultipartFile zipFile,String user) {
            String local=System.getProperty("user.dir");
            String targetFilePath =local+"\\gyy_code\\src\\main\\resources\\static\\img\\user\\";
//            String fileName = UUID.randomUUID().toString().replace("-", "")+".png";
            String fileName = user+".png";
            File targetFile = new File(targetFilePath + File.separator + fileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(targetFile);
                IOUtils.copy(zipFile.getInputStream(), fileOutputStream);
                String dburl="\\img\\user\\"+fileName+".png";
                uploadUserDB(user,dburl);
                logger.info("------>>>>>>uploaded a file successfully!<<<<<<------");
            } catch (IOException e) {
                return new ReturnValue<>(-1, null);
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
            return new ReturnValue<>(ReturnCodeAndMsgEnum.Success, null);
        }

    @Override
    public void uploadUserDB(String user, String url) {
//                    QueryWrapper<TsUser>  wrapp=new QueryWrapper<TsUser>();
//                          wrapp.eq("username",user);
//                 TsUser username= userService.getOne(wrapp);
//                    if (username==null){
//                        return;
//                    }
        UpdateWrapper<TsUser> wrapper=new UpdateWrapper<TsUser>();
        wrapper.eq("username",user).set("icon",url);
        this.userMapper.update(null,wrapper);
    }

    @Override
    public ReturnValue uploadBookImgFile(MultipartFile zipFile, String book) {
        String local=System.getProperty("user.dir");
        String targetFilePath =local+"\\gyy_code\\src\\main\\resources\\static\\img\\book\\";
//            String fileName = UUID.randomUUID().toString().replace("-", "")+".png";
        String fileName = book+".png";
        File targetFile = new File(targetFilePath + File.separator + fileName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(targetFile);
            IOUtils.copy(zipFile.getInputStream(), fileOutputStream);
            logger.info("------>>>>>>uploaded a file successfully!<<<<<<------");
            String dburl="\\img\\book\\"+fileName+".png";
            uploadBookDB(book,dburl);
        } catch (IOException e) {
            return new ReturnValue<>(-1, null);
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        return new ReturnValue<>(ReturnCodeAndMsgEnum.Success, null);
    }

    @Override
    public void uploadBookDB(String book, String url) {
        UpdateWrapper<TsBook> wrapper=new UpdateWrapper<TsBook>();
        wrapper.eq("bookname",book).set("imgurl",url);
        this.bookMapper.update(null,wrapper);
    }


}
