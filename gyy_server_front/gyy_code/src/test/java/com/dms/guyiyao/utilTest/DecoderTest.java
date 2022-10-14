package com.dms.guyiyao.utilTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class DecoderTest {
@Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    public void test_1(){
        String encode = bCryptPasswordEncoder.encode("123");
        System.out.println(encode);
        boolean matches = bCryptPasswordEncoder.matches("123", encode);
        System.out.println(matches);
    }
//    $2a$10$.HljRMuL9aGhkWZ.VgShdu
//    $2a$10$.HljRMuL9aGhkWZ.VgShdujoXUmafQx.MA/e2zdxLKvGKB9cH9Q4u

}
