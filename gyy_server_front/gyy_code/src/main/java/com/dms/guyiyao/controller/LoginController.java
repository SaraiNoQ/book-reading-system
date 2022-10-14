package com.dms.guyiyao.controller;

import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.resp.ResponseResult;
import com.dms.guyiyao.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.security.pkcs11.SunPKCS11;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {
    @Autowired
    LoginService loginService;


@PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user, HttpServletRequest request){

    return loginService.login(request,user);
}
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

}



