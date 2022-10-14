package com.dms.guyiyao.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dms.guyiyao.mapper.TsBookMapper;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.ReturnCodeAndMsgEnum;
import com.dms.guyiyao.pojo.ReturnValue;
import com.dms.guyiyao.pojo.book.TsBook;
import com.dms.guyiyao.pojo.original.ImgForm;
import com.dms.guyiyao.pojo.user.TsUser;
import com.dms.guyiyao.service.UploadService;
import com.dms.guyiyao.utils.FtpUtil;
import com.dms.guyiyao.utils.PYUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {
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


    @Autowired
    TsBookMapper bookMapper;
    @Autowired
    TsUserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);
    @Override
    public ReturnValue uploadUserImgFile(MultipartFile zipFile,String user) {
        String local=System.getProperty("user.dir");
        String targetFilePath =local+"\\src\\main\\resources\\static\\img\\user\\";
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

//    @Override
//    public ReturnValue uploadBookImgFile(MultipartFile zipFile, String book) {
//        String local=System.getProperty("user.dir");
////        生成文件路径
//        String targetFilePath =local+"\\gyy_code\\src\\main\\resources\\static\\img\\book\\";
////            String fileName = UUID.randomUUID().toString().replace("-", "")+".png";
////       生成文件名
//        String fileName = PYUtil.getHeadPy(book)+".png";
////        生成目标文件
//        File targetFile = new File(targetFilePath + File.separator + fileName);
////        生成输出流
//        FileOutputStream fileOutputStream = null;
//        try {
//            fileOutputStream = new FileOutputStream(targetFile);
//            IOUtils.copy(zipFile.getInputStream(), fileOutputStream);
//            logger.info("------>>>>>>uploaded a file successfully!<<<<<<------");
//            String dburl="\\img\\book\\"+fileName;
//            uploadBookDB(book,dburl);
//        } catch (IOException e) {
//            return new ReturnValue<>(-1, null);
//        } finally {
//            try {
//               fileOutputStream.close();
//            } catch (IOException e) {
//                logger.error("", e);
//            }
//        }
//        return new ReturnValue<>(ReturnCodeAndMsgEnum.Success, null);
//    }




    @Override
    public void uploadBookDB(String book, String url) {
        UpdateWrapper<TsBook> wrapper=new UpdateWrapper<TsBook>();
        wrapper.eq("bookname",book).set("imgurl",url);
        this.bookMapper.update(null,wrapper);
    }
    @Override
    public int addBookImg(String img[], String bookName,String uuid){
        String local = System.getProperty("user.dir");
        String dataPrix = ""; //base64格式前头
        String data = "";//实体部分数据
//        String[] d =img.split("base64,");//将字符串分成数组
        dataPrix = img[0];
        data = img[1];
        String suffix = "";//图片后缀，用以识别哪种格式数据
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
        }
//        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//       获取时间戳
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();
//        String timeStr = time.format(date).replace(" ", "-");
//
//        String imgName = PYUtil.getHeadPy(bookName)+".png";
        BASE64Decoder decoder = new BASE64Decoder();
        try{
            //Base64解码
            byte[] b = decoder.decodeBuffer(data);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    //调整异常数据
                    b[i] += 256;
                }
            }
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            FtpUpload(uuid+".png","/book/",bi);

//            out.write(b);
//            out.flush();
//            out.close();
            uploadBookDB(bookName,"/book/"+uuid+".png");
//            String imgurl="http://xxxxxxxx/"+tempFileName;
            //imageService.save(imgurl);
//            return;
        }catch (IOException e) {
            e.printStackTrace();
//            写入失败
            return 0;
        }
//        成功写入
    return  1;
    }
    public String FtpUpload(String fileName,
                            String dirPath,InputStream inputStream){

//    FtpUtil.getFTPClient(host,userName,passWord,port);
        //1、给上传的图片生成新的文件名
        //1.1获取原始文件名
//        String oldName = uploadFile.getOriginalFilename();
        //1.2使用IDUtils工具类生成新的文件名，新文件名 = newName + 文件后缀
//        String newName = IDUtils.genImageName();
        String newName = "dd";
//        newName = newName + oldName.substring(oldName.lastIndexOf("."));
        //1.3生成文件在服务器端存储的子目录
//        String filePath = new DateTime().toString("/yyyy/MM/dd");
//        String filePath = "test";

        //2、把前端输入信息，包括图片的url保存到数据库
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setPicture(baseUrl + filePath + "/" + newName);
//        userService.insertUser(user);

        //3、把图片上传到图片服务器
        //3.1获取上传的io流
//        InputStream input = uploadFile.getInputStream();

        //3.2调用FtpUtil工具类进行上传
        boolean result = FtpUtil.uploadFile(host, port, userName, passWord, basePath, "/"+dirPath, fileName, inputStream);
        System.out.println(result);
        if(result) {
            return "success";
        }else {
            return "false";
        }
    }



}

