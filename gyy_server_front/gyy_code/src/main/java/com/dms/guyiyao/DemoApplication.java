package com.dms.guyiyao;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.dms.guyiyao.mapper")
public class DemoApplication {

    public static void main(String[] args) {
//
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);
    }

}
