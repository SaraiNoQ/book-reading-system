package com.dms.guyiyao.service;

import com.dms.guyiyao.pojo.ReturnValue;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ReturnValue uploadUserImgFile(MultipartFile zipFile,String user);
    void uploadUserDB(String user,String url);

    ReturnValue uploadBookImgFile(MultipartFile zipFile,String user);
    void uploadBookDB(String book ,String url);
}
