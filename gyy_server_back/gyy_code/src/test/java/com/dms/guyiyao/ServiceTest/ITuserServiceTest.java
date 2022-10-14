package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.service.ITcCodeService;

import com.dms.guyiyao.service.ITsUserService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.dms.guyiyao.mapper")
public class ITuserServiceTest {
@Autowired
ITsUserService iTsUserService;
   /*对删除用户接口的测试*/
    @Test
    public  void test_1(){
        System.out.println(iTsUserService.deleteUser("robin123"));
}
/*获取用户列表的测试*/
    @Test
    public  void test_2(){
        System.out.println(iTsUserService.getUserList(1,10));
    }
    /*重置密码的测试*/
    @Test
    public  void test_3(){
        System.out.println(iTsUserService.resetUserPassword("robin123"));
    }
    @Test
    public  void test_4(){
        System.out.println(iTsUserService.searchForUser(1,1,"robin123",null,null));
    }
}
