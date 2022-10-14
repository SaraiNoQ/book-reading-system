package com.dms.guyiyao.ServiceTest;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@MapperScan("com.dms.guyiyao.mapper")
public class getUserTest {
//    @Autowired
//    private ITsUserService userService;
//    @Test
//    public  void  getUser(){
//        System.out.println(userService.getUserList());
//    }
    @Test
    public void test(){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.matches("12345678", "$10$C1HSq8oPxl6BUQLKj29N1OJZsMDZg/ibILDCCpm7qKD89Re.PovnK"));
        System.out.println(bCryptPasswordEncoder.matches("admin123", "$10$C1HSq8oPxl6BUQLKj29N1OJZsMDZg/ibILDCCpm7qKD89Re.PovnK"));

    }
}
