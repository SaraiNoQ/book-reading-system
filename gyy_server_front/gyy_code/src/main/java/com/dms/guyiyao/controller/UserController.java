package com.dms.guyiyao.controller;

import com.dms.guyiyao.pojo.ImgForm;
import com.dms.guyiyao.pojo.UserInfo;
import com.dms.guyiyao.security.userdetail.UserdetailImpl;
import com.dms.guyiyao.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Api(tags = "个人中心")
@RestController
public class UserController {
//    Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
    @Autowired
    private UserService userService;
    @PostMapping("/user/getUserInfo")
    public UserInfo getUserInfo(HttpServletRequest request, HttpServletResponse response){
//        System.out.println(authentication);
        String username = request.getRemoteUser();
        UserInfo userInfo=userService.getUserInfo(username);
      if (userInfo==null)userInfo=userService.getUserInfoByemail(username);
        return userInfo;
    }
    @PostMapping("/user/updateIcon")
    public String updateIcon(HttpServletRequest request, HttpServletResponse response,ImgForm imgForm) throws Exception {
        String username = request.getRemoteUser();
        return userService.updateIcon(username,imgForm);
    }
    @PostMapping("user/updateNickname")
    public  String updateNickName(HttpServletRequest request, HttpServletResponse response,@RequestParam("nickName") String nickName){
        String username = request.getRemoteUser();
        return userService.updateNickName(username,nickName);
    }
    @PostMapping("user/updatePassword")
    public  String updateNickName(@RequestParam("oldPass") String oldPass,@RequestParam("newPass")String newPass){
       oldPass=oldPass.trim();
       newPass=newPass.trim();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserdetailImpl userdetail=(UserdetailImpl)authentication.getPrincipal();
        System.out.println("密码是"+userdetail.getPassword());
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(oldPass, userdetail.getPassword()))return "旧密码错误";
        return     userService.updatePass(userdetail.getUsername(),bCryptPasswordEncoder.encode(newPass));
    }

}
