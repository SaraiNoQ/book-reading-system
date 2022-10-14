package com.dms.guyiyao.service.Impl;

import com.dms.guyiyao.dao.RegistDao;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.Regist_Info;
import com.dms.guyiyao.service.RegistService;
import com.dms.guyiyao.utils.SendEmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;
@Service
public class RegistServiceImpl implements RegistService {
    @Autowired
    private RegistDao registDao;
@Autowired
    private TsUserMapper userMapper;

    @Transactional
    @Override
    public ModelAndView regist_sub(HttpServletRequest req, HttpServletResponse resp,Regist_Info regist_info) {
    ModelAndView mav=new ModelAndView();

/*
* 从前端获取提交表单信息
* */
        //邮箱格式验证
        String username=regist_info.getUsername();
        String password=regist_info.getPassword();
        String email=regist_info.getEmail();
//        boolean isMathch_username=registDao.getUserNameByName(username).equals("")&&registDao.getUserNameByEmail(email).equals("");

        /*
         * 验证密码和邮箱合法性
         * */

//        String reg_username= "^\\w{8,16}$";
        String reg_username= "^[a-zA-Z0-9_-]{4,16}$";       //4-16位用户名(可以是数字，字母，下划线)
//        String reg_password= "^\\w{8,16}$";
        String reg_password= "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{6,15}$";     //6-15位密码（字母数字特殊字符至少包含两种）
        String reg_email="^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";//邮箱正则
        boolean isMathch_username= Pattern.matches(reg_username,password);
        boolean isMathch_password= Pattern.matches(reg_password,password);
        boolean isMathch_email=Pattern.matches(reg_email,email);

        /*
        * 向前端返回结果
        * */
        if (isMathch_password&&isMathch_email&&isMathch_username) {//正则验证通过后和数据层接触
            /*
             * 通过检查后向数据库添加数据*/
//            密码加密存储
            String password_hash=new BCryptPasswordEncoder().encode(password);
/*验证邮箱和账号是否存在*/
           if (userMapper.containEmail(email)!=0){mav.addObject("status","邮箱已被绑定");return  mav;}
           if (userMapper.containUser(username)!=0){mav.addObject("status","用户已存在");return  mav;}
           /*开始写入数据库并进行邮箱通知*/
           if (registDao.addUserInfo(username, password_hash, email)) {//数据库写入成功后发送邮箱通知
                mav.addObject("status", "success");
                String title = "注册信息";
                String context = "尊敬的" + username + "您好,你已经成功在北京古医药大学古籍药典阅读对照系统注册账号成功。密码为" + password;
               try{
                SendEmailUtil.sendSimpleMail("1164175212@qq.com", email, title, context);
               }catch (Exception e){
                   throw new RuntimeException("请输入正确邮箱");
               }
//                System.out.println(req.getMethod());
                return mav;//所有操作成功并返回
            }
            mav.addObject("status", "用户数据添加失败");
            return mav;
        }else {//正则未通过进行返回
            String err="";
         if (!isMathch_username)err+="用户名";
         if (!isMathch_password)err+=err.equals("")?"密码格式错误":",密码格式错误";
         if (!isMathch_email)err+=err.equals("")?"邮箱格式错误":",邮箱格式错误";
           mav.addObject("status",err);
           return mav;
        }


    }
}
