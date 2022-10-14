package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.pojo.Regist_Info;
import com.dms.guyiyao.service.Impl.RegistServiceImpl;
import com.dms.guyiyao.service.RegistService;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@SpringBootTest
public class RegistTest {
   @Autowired
   RegistService registService;
/*
* 注册成功并邮箱通知
* */
   @Test
    public  void test_1() throws Exception{
//        Regist_Info regist_info=new Regist_Info("GIT测试","1164175212@qq.com","robin");
//       ModelAndView modelAndView=registService.regist_sub(new HttpServletRequest() {
//       }regist_info);
//       System.out.println(modelAndView.getModel());
   }
   @Test
    public  void test_2(){
       System.out.println(new  BCryptPasswordEncoder().encode("robin123"));
   }
}


