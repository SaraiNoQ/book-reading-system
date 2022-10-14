package com.dms.guyiyao.controller;

import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.resp.ResponseResult;
import com.dms.guyiyao.service.LoginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "后台登录接口")
@RestController
public class LoginController {

    @Autowired
  private   LoginService loginService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user, HttpServletRequest request){

        return loginService.login(user,request);
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
