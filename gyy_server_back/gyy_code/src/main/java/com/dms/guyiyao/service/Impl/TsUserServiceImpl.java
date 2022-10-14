package com.dms.guyiyao.service.Impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.Regist_Info;
import com.dms.guyiyao.pojo.page.PageV0;
import com.dms.guyiyao.pojo.user.TsUser;
import com.dms.guyiyao.pojo.user.TsUser_0;
import com.dms.guyiyao.service.ITsUserService;
import com.dms.guyiyao.utils.Loggers;
import com.dms.guyiyao.utils.SendEmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.parameters.P;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TsUserServiceImpl extends ServiceImpl<TsUserMapper, TsUser> implements ITsUserService {
    @Autowired
    private  TsUserMapper userMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
/*
* mapper ->
*
* */
    @Override
    public PageV0 getUserList(Integer page, Integer size) {
//        定义分页
        Page<TsUser> userPage=new Page<>(page,size);
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("status",0);
//        按照分页查询
        Page<TsUser> resultPage=this.userMapper.selectPage(userPage,wrapper);
//       获取分页查询的结果
        List<TsUser> userList=resultPage.getRecords();
//        对结果进行处理放入新容器

        List data=new ArrayList();
        for (TsUser info : userList) {
            if (info.getStatus().equals("1"))continue;
            TsUser_0 info_0=new TsUser_0();
            /*reverse by reflect???*/
            info_0.setId(info.getId());
            info_0.setNickname(info.getNickname());
            info_0.setUsername(info.getUsername());
            info_0.setPassword("***********");
            info_0.setEmail(info.getEmail());
            info_0.setUsertype(info.getUsertype());
            data.add(info_0);
        }
//        构建返回对象
        PageV0 pageV0 =new PageV0();
        pageV0.setData(data);
        pageV0.setTotal(resultPage.getTotal());
        return pageV0;
    }

    @Override
    public boolean resetUserPassword(String username) {

        UpdateWrapper<TsUser>wrapper=new UpdateWrapper<>();
        String password_hash=new BCryptPasswordEncoder().encode(RESET_PASSWORD);
        Loggers.info(RESET_PASSWORD+"加密成功");
        wrapper.eq("username",username).set("password",password_hash);
        try {
        userMapper.update(null,wrapper);
            Loggers.info("加密密码"+password_hash+"修改成功");
        }catch (Exception e){
            Loggers.info("加密密码修改失败");
            e.printStackTrace();
            Loggers.info(e.getMessage());
            return false;
        }
        return true;
    }
/*status=1 表示用户已被删除*/
    @Override
    public boolean deleteUser(String id) {

        UpdateWrapper<TsUser>wrapper=new UpdateWrapper<>();
//        wrapper.eq("id",id).set("status","1");
        wrapper.eq("id",id);
        try {
            userMapper.delete(wrapper);
//            userMapper.update(null,wrapper);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
/*
* userName 账号
* nickName 昵称
* email  邮箱
* */
    @Override
    public PageV0 searchForUser(Integer page, Integer size, String username, String nickname, String email) {
        //        按照分页查询

        /*add tiao jian*/
        page=(page-1)*size;

        List<TsUser_0>  data =  userMapper.searchForUser(page , size, username, nickname, email);
        PageV0 pageV0=new PageV0();
        pageV0.setData(data);
        pageV0.setTotal(userMapper.searchForUser_count(username, nickname, email));
        return pageV0;
    }

    @Override
    public ModelAndView commonUserAdd(HttpServletRequest req, HttpServletResponse resp, Regist_Info regist_info,String userId) throws Exception {
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
        boolean isMathch_username= Pattern.matches(reg_username,username);
        boolean isMathch_password= Pattern.matches(reg_password,password);
        boolean isMathch_email=email==null?true:Pattern.matches(reg_email,email);


        /*
         * 向前端返回结果
         * */
        if (isMathch_password&&isMathch_email&&isMathch_username) {//正则验证通过后和数据层接触
            /*
             * 通过检查后向数据库添加数据*/
//            密码加密存储
            String password_hash=new BCryptPasswordEncoder().encode(password);
            /*验证邮箱和账号是否存在*/
            if (email!=null&&userMapper.containEmail(email)!=0){mav.addObject("status","邮箱已被绑定");return  mav;}
            if (userMapper.containUser(username)!=0){mav.addObject("status","用户已存在");return  mav;}
            /*开始写入数据库并进行邮箱通知*/
            if (addUserInfo(username, password_hash, email,userId)) {//数据库写入成功后发送邮箱通知
                mav.addObject("status", "success");
                String title = "注册信息";
                String context = "尊敬的" + username + "您好,你已经成功在北京古医药大学古籍药典阅读对照系统注册账号成功。密码为" + password;
                try{
                  if (email!=null)SendEmailUtil.sendSimpleMail("1164175212@qq.com", email, title, context);
                  else SendEmailUtil.sendSimpleMail("1164175212@qq.com","1164175212@qq.com", title, context);
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
            if (!isMathch_username)err+="用户名格式错误,";
            if (!isMathch_password)err+=err.equals("")?"密码格式错误":",密码格式错误";
            if (!isMathch_email)err+=err.equals("")?"邮箱格式错误":",邮箱格式错误";
            mav.addObject("status",err);
            return mav;
        }
    }
    public boolean addUserInfo(String username, String password, String email,String userId) {

        try {
            String sql="insert  into ts_user(id,username,password,`email`) values(?,?,?,?)";
            jdbcTemplate.update(sql, userId,username, password, email);
        }catch (Exception e){
            return  false;
        }
        return  true;
    }

}
