package com.dms.guyiyao.pojo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class Regist_Info {
private String username;
private  String email;
private  String password;
public  Regist_Info(String username,@RequestParam(required = false) String email,String password){
    this.username=username;
    this.email=email;
    this.password=password;
}
}
