package com.dms.guyiyao.controller;

import com.dms.guyiyao.pojo.ReturnValue;
import com.dms.guyiyao.service.UploadService;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
//待删除接口
//@RestController
//@RequestMapping(value = "/file")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private UploadService uploadService;

    /**
     * 文件上传测试接口
     * @return
     */
//    @AppIdAuthorization
    @RequestMapping("/uploadBookImg")
    public ReturnValue uploadUserImgFile(@RequestParam("uploadFile") MultipartFile zipFile,@RequestParam("book") String book) {
        return uploadService.uploadBookImgFile(zipFile,book);
    }
    @RequestMapping("/uploadUserImg")
    public ReturnValue uploadBookFile(@RequestParam("uploadFile") MultipartFile zipFile,@RequestParam("user")String book) {
        return uploadService.uploadUserImgFile(zipFile,book);
    }
}

