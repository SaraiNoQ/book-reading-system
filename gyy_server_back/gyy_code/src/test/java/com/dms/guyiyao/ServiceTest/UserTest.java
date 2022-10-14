package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.service.Impl.TsUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
@Autowired
    TsUserServiceImpl userService;
    @Test
    public void test1(){
        System.out.print(userService.getUserList(1, 10));
}
}
