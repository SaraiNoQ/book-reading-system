package com.dms.guyiyao.service.Impl;

import com.dms.guyiyao.service.FtpService;
import com.dms.guyiyao.utils.FtpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
@Service
public class FtpServiceImpl implements FtpService {
    @Value("${FTP.ADDRESS}")
    private String host;
    // 端口
    @Value("${FTP.PORT}")
    private int port;
    // ftp用户名
    @Value("${FTP.USERNAME}")
    private String userName;
    // ftp用户密码
    @Value("${FTP.PASSWORD}")
    private String passWord;
    // 文件在服务器端保存的主目录
    @Value("${FTP.BASEPATH}")
    private String basePath;
    // 访问图片时的基础url
    @Value("${IMAGE.BASE.URL}")
    private String baseUrl;

    @Override
    public String FtpUpload(String fileName, String dirPath, InputStream inputStream) {
        boolean result = FtpUtil.uploadFile(host, port, userName, passWord, basePath, "/"+dirPath, fileName, inputStream);
        System.out.println(result);
        if(result) {
            return "success";
        }else {
            return "false";
        }
    }
}
