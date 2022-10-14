package com.dms.guyiyao.service.Impl;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.ImgForm;
import com.dms.guyiyao.pojo.TsUser;
import com.dms.guyiyao.pojo.UserInfo;
import com.dms.guyiyao.service.FtpService;
import com.dms.guyiyao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TsUserServiceImpl  extends ServiceImpl<TsUserMapper, TsUser> implements UserService  {
//    Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
    @Autowired
    TsUserMapper userMapper;
    @Autowired
    FtpService ftpService;

    @Override
    public UserInfo getUserInfo(String username) {

        UserInfo userInfo= userMapper.getUserInfo(username);
        return userInfo;
    }

    @Override
    public String updateIcon(String username, ImgForm imgForm) throws Exception {

//        log.info("==上传图片==");
//        log.info("==接收到的数据=="+base64Data);
            String dataPrix = ""; //base64格式前头
            String data = "";//实体部分数据
            if (imgForm == null || "".equals(imgForm)) {
                return "请上传照片";
            } else {
                String[] d = imgForm.getFile();
                if (d != null && d.length == 2) {
                    dataPrix = d[0].replace(";base64",";");
                    data = d[1];
                } else {
                    return "数据获取失败";
                }
            }
            String suffix = "";//图片后缀，用以识别哪种格式数据
            //data:image/jpeg;base64,base64编码的jpeg图片数据
            if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {
                suffix = ".jpg";
            } else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {
                //data:image/x-icon;base64,base64编码的icon图片数据
                suffix = ".ico";
            } else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {
                //data:image/gif;base64,base64编码的gif图片数据
                suffix = ".gif";
            } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {
                //data:image/png;base64,base64编码的png图片数据
                suffix = ".png";
            } else {
                System.out.println(1);
                return "图片格式错误";
            }

            String filename=UUID.randomUUID().toString().replaceAll("-","")+suffix ;
            String url="/user/";
            BASE64Decoder decoder = new BASE64Decoder();

                //Base64解码
                byte[] b = decoder.decodeBuffer(data);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        //调整异常数据
                        b[i] += 256;
                    }
                }
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                 if (ftpService.FtpUpload(filename,url,bi).equals("false")) throw new Exception("头像上传到ftp服务器失败");
                 if (userMapper.updateIcon(username,url+filename)==0)throw  new Exception("头像连接上传到图片服务器失败");

        return "success";
    }

    @Override
    public String updateNickName(String username, String nickName) {
      int rs=userMapper.updateNickName(username,nickName);
      if (rs==0)return "false";
        return "success";
    }

    @Override
    public String updatePass(String username, String newPass) {
     int rs=  userMapper.updatePass(username,newPass);
     if (rs==0)return "false";
     return "success";
    }

    @Override
    public UserInfo getUserInfoByemail(String username) {
       return    userMapper.getUserInfoByEmail(username);
    }
}
