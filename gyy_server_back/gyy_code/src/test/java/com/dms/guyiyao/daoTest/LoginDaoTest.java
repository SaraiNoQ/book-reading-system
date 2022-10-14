package com.dms.guyiyao.daoTest;

import com.dms.guyiyao.dao.LoginDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;

@SpringBootTest
public class LoginDaoTest {
    @Autowired
    LoginDao loginDao;
@Test
    public void test_() throws Exception {
        System.out.println(loginDao.getUserByusername("robin"));
}
}
