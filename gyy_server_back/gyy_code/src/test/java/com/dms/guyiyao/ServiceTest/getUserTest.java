package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.service.ITsUserService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.dms.guyiyao.mapper")
public class getUserTest {
    @Autowired
    private ITsUserService userService;
    @Test
    public  void  getUser(){
//        System.out.println(userService.getUserList());
    }
}
