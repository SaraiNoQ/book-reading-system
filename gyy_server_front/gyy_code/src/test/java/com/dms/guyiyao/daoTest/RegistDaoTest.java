package com.dms.guyiyao.daoTest;

import com.dms.guyiyao.dao.RegistDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.midi.Soundbank;

/**
 * @author QIqi
 * @create 2022/1/22 - 17:30
 */
@SpringBootTest
public class RegistDaoTest {
@Autowired
    RegistDao registDao;

    @Test
    public  void test_1(){
   System.out.println(registDao.getUserNameByName("alis"));
        boolean isMathch_username=registDao.getUserNameByName("robin1234444").equals("");
        System.out.println(isMathch_username);
    }
}
