package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.utils.FtpUtil;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


@SpringBootTest
public class ImgTest {
    @Test
    public  void test_1(){
       InputStream fileInputStream=null;
        try {
             fileInputStream= new FileInputStream("D:\\project\\test.jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(FtpUtil.uploadFile("114.55.85.25",21,"ftpuser","mima12345","/home/img","/test","test.jpeg",fileInputStream));
    }
}
