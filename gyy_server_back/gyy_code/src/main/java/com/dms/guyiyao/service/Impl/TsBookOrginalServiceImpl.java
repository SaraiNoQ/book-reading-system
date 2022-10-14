package com.dms.guyiyao.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsBookChapterMapper;
import com.dms.guyiyao.mapper.TsBookOrginalMapper;
import com.dms.guyiyao.pojo.ReturnCodeAndMsgEnum;
import com.dms.guyiyao.pojo.ReturnValue;
import com.dms.guyiyao.pojo.book.TsBook;
import com.dms.guyiyao.pojo.book.TsBookOrginal;
import com.dms.guyiyao.pojo.chapter.FullCataLog;
import com.dms.guyiyao.pojo.original.ContentOriginal;
import com.dms.guyiyao.pojo.original.ImgForm;
import com.dms.guyiyao.pojo.original.OriginalReturn;
import com.dms.guyiyao.service.ITsBookOrginalService;
import com.dms.guyiyao.utils.FtpUtil;
import com.dms.guyiyao.utils.PYUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 医书原文表 服务实现类
 */
@Service
public class TsBookOrginalServiceImpl extends ServiceImpl<TsBookOrginalMapper, TsBookOrginal> implements ITsBookOrginalService {
    String local = System.getProperty("user.dir");

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
public String FtpUpload(String fileName,
                       String dirPath,InputStream inputStream){

//    FtpUtil.getFTPClient(host,userName,passWord,port);
    //1、给上传的图片生成新的文件名
    //1.1获取原始文件名
//        String oldName = uploadFile.getOriginalFilename();
    //1.2使用IDUtils工具类生成新的文件名，新文件名 = newName + 文件后缀
//        String newName = IDUtils.genImageName();

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



@Autowired
TsBookOrginalMapper orginalMapper;
@Autowired
    TsBookChapterMapper chapterMapper;

    @Override
    public List<TsBookOrginal> getPicUrl(String chapterId) {
        return  orginalMapper.getPicUrl(chapterId);
    }
    /*
    * 删除照片的主方法
    * */
    @Override
    @Transactional
    public int deleteImgUrl(String imgId,String imgUrl) {
        /*获取安全框架中保存的用户名*/
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*获取当前时间*/
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String timeStr=time.format(date);

//        String targetFilePath = local + "\\gyy_code\\src\\main\\resources\\static" + imgUrl;
        String targetFilePath =imgUrl;
            File file = new File(targetFilePath);
          String fileName="";
          StringBuilder filepath=new StringBuilder();
        String[] strs= imgUrl.split("/");
        for (int i = 0; i < strs.length-1; i++) {
            filepath.append(strs[i]).append("/");
        }
        fileName=strs[strs.length-1];
            FtpUtil.deleteFile(host,port,userName,passWord,basePath,filepath.toString(),fileName);


//            创建输入输出流为回滚照片做准备
        FileInputStream fileInputStream= null;
        FileOutputStream fileOutputStream=null;

//        默认照片最大10M，如果照片超过这个大小照片回滚会失败
            byte[]temp=new byte[8*1024*1024*10];
            int len=0;
        try {
//            提前备份照片后面用于回滚
            fileInputStream = new FileInputStream(targetFilePath);
            len=fileInputStream.read(temp);
//            关闭该流不然后面删除会失败
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int rs=0;
        try {
//            照片删除主要逻辑
            rs = orginalMapper.deleteImgUrl(imgId);
            /*IO流删除照片*/
            delete(file);
        if (rs==0)throw new Exception("数据库操作异常");
//异常模拟
        }catch (Exception e){
//            出现异常进行处理
            e.printStackTrace();
            //数据库回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            文件回滚
            try {
                fileOutputStream=new FileOutputStream(targetFilePath);
                fileOutputStream.write(temp,0,len);
                fileOutputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }finally {
            try {
             if (fileInputStream!=null)  fileInputStream.close();
             if (fileOutputStream!=null)  fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }
/*
* 上传照片的主方法
* */
@Transactional
    @Override
    public String uploadImg(ImgForm imgForm) {
//        获取多张图片
        for (int j = 0; j < imgForm.getFile().length; j++) {
//        log.info("==上传图片==");
//        log.info("==接收到的数据=="+base64Data);
            String dataPrix = ""; //base64格式前头
            String data = "";//实体部分数据
            if (imgForm == null || "".equals(imgForm)) {
                return "请上传照片";
            } else {
                String[] d = imgForm.getFile()[j].split("base64,");//将字符串分成数组
                if (d != null && d.length == 2) {
                    dataPrix = d[0];
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
                return "图片格式错误";
            }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//       获取时间戳
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            Date date = new Date();
            String timeStr = time.format(date).replace(" ", "-");

            String tempFileName = timeStr+"-"+uuid+ suffix;
            String cataLog = PYUtil.getHeadPy(getFullcataLog(imgForm.getId()));
//            String imgFilePath = local + "\\gyy_code\\src\\main\\resources\\static\\" + cataLog + "\\" + tempFileName;//新生成的图片
            String imgFilePath = cataLog ;//新生成的图片
//            File file = new File(local + "\\gyy_code\\src\\main\\resources\\static\\" + cataLog);
            int result=uploadImgDb(imgForm.getId(),tempFileName,"/"+cataLog.replace("\\","/") + "/" + tempFileName);
           if (result==0)return "数据库操作异常";//先执行数据库操作，如果出现异常就不执行IO流操作(事务会自动回滚异常数据库的影响)

//            if (!file.exists()) {
//                file.mkdirs();
//            }
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                //Base64解码
                byte[] b = decoder.decodeBuffer(data);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        //调整异常数据
                        b[i] += 256;
                    }
                }
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                FtpUpload(tempFileName,imgFilePath,bi);
//                OutputStream out = new FileOutputStream(imgFilePath);
//                out.write(b);
//                out.flush();
//                out.close();
//
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败";
            }

        }
        return "上传成功";
    }

    @Override
    public String addMatch(String chapterId,String content, String[] imgId,int sequence) {
//        如果匹配的段落已经匹配则删除后在匹配
    if (getOriginal(chapterId).keySet().contains(sequence)) orginalMapper.deleteMatch(chapterId,sequence);
//    生成通用的uuid，在添加两张表时可以用同一个id
    String contentUuid=UUID.randomUUID().toString().replace("-","");
//        System.out.println(contentUuid);
//        添加原文内容表
    int rs1=orginalMapper.addMatch_1(contentUuid,chapterId,content,sequence);
    int rs2=0;
        for (int i = 0; i < imgId.length; i++) {
//            添加内容表和照片表的映射表
         rs2=orginalMapper.addMatch_2(contentUuid,chapterId,content,imgId[i],i)==0?0:1;
        }
//        任一操作失败返回false;
    if (rs1==0||rs2==0)return "false";
    return "success";
    }

    @Override
    public Map<Integer,OriginalReturn>getOriginal(String chapterId) {
        List<String>originalsIds=orginalMapper.getOriginalId(chapterId);
        Map<Integer,OriginalReturn>map=new HashMap<>();
        for (String original : originalsIds) {
        OriginalReturn originalReturn=new OriginalReturn();
            //一个自然段的
//            System.out.println(original+"\t");
            List<ContentOriginal> contentOriginal= orginalMapper.getOriginal(original);
//            System.out.println(contentOriginal);
            originalReturn.setData(contentOriginal);
            originalReturn.setCount(contentOriginal.size());
            if (contentOriginal.size()!=0) {
                originalReturn.setSequence(Integer.valueOf(contentOriginal.get(0).getOutSequence()));
                //多个自然段的
                map.put(originalReturn.getSequence(), originalReturn);
            }
        }

        return map;
    }

//
    public String deleteMathch(String chapterId,int sequence) {
       int rs=orginalMapper.deleteMatch(chapterId,sequence);
        if (rs!=0)return "success";
        return "false";
    }

    /*
    * 上传照片后更新数据到数据库
    * */
    @Transactional
    public int uploadImgDb(String chapterId,String imgName,String imgUrl){
       int result=0;
        result= orginalMapper.addImg(chapterId,imgName,imgUrl);
        return  result;
    }
/*   通过章节id获取照片存储的完整目录地址
*
* */
    public String getFullcataLog(String chapterId){
        FullCataLog fullCataLog=chapterMapper.getFullCataLog(chapterId);
        FullCataLog fullCataLog_temp=fullCataLog;
        while (fullCataLog!=null){
            String chapterName=fullCataLog.getChaptername();
            fullCataLog_temp=chapterMapper.getFullCataLog(fullCataLog.getParentid());
            if(fullCataLog_temp==null)break;
            chapterName=fullCataLog_temp.getChaptername()+"/"+chapterName;
            fullCataLog.setChaptername(chapterName);
            fullCataLog.setParentid(fullCataLog_temp.getParentid());
        }
        String result=fullCataLog.getBookname()+"/"+fullCataLog.getChaptername();
        return  result;
    }

    //    删除文件夹照片的方法
    public  static   void delete(File file){
        //检查文件是否存在，如果不存在直接返回，不进行下面的操作
        if(!file.exists()){
            return;
        }
        //如果是文件删除，就删除文件，然后返回，不进行下面的操作
        if(file.isFile()){
            System.out.println(file.delete());
            return;
        }
        //是文件夹
        if(file.isDirectory()){
            //循环所有文件夹里面的内容并删除
            File[] files=file.listFiles();
            if (files!=null) {
                for (File f : files) {
                    //使用迭代，调用自己
                    delete(f);
                }
            }
            //删除自己
            file.delete();
        }
    }




}
