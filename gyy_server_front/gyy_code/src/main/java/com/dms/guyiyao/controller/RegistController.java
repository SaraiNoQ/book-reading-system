package com.dms.guyiyao.controller;

import com.dms.guyiyao.pojo.Regist_Info;
import com.dms.guyiyao.service.RegistService;
import com.dms.guyiyao.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

@Api(tags = "注册接口")
//@Controller
public class RegistController {
    @Autowired
    RegistService registService;
    @Autowired
    protected AuthenticationManager authenticationManager;
//@ApiOperation("提交注册")
//    @ResponseBody
//   @RequestMapping("/regist")
    public void regist(HttpServletRequest req,HttpServletResponse resp, Regist_Info regist_info) throws Exception {
    ModelAndView modelAndView =new ModelAndView();
       try{
            modelAndView = registService.regist_sub(req, resp, regist_info);
       }catch (Exception e){
           modelAndView.addObject("status",e.getMessage());
       }

        HashMap map = modelAndView.getModelMap();
        if(map.get("status").equals("success")){
            authenticateUserAndSetSession(regist_info.getUsername(),regist_info.getPassword(),req);
        }
        JSONObject json = JsonUtil.toJsonObject(modelAndView.getModelMap());
        JsonUtil.returnJsonClient(resp, json);
    }
    /*用户实现注册成功后的自动登录*/
    private void authenticateUserAndSetSession(String username,String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

    public static void main(String[] args) {
//        try {
//            Socket socket=new Socket();
//            socket.connect(new InetSocketAddress("127.0.0.1",8888));
//            PrintWriter printWriter=new PrintWriter(socket.getOutputStream(),true);
//            String req="GET /user/addAdmin HTTP/1.1\n" +
//                    "Accept:application/json, text/plain, */*\n" +
//                    "Accept-Language:zh,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6\n" +
//                    "Connection:Keep-Alive\n" +
//                    "Host:127.0.0.1:8888\n" +
//                    "User-Agent:Mozila/4.0(compatible;MSIE5.01;Window NT5.0)\n" +
//                    "Accept-Encoding:gzip,deflate\n" +
//                    "token:eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzNDQ3NTk5MGY5OGU0MGU1OWY5NDlkOWE4OGViNjFiZiIsInN1YiI6IjA0MzkyMWU4ZjQ2ZjExZWM4Yzk2MDA1MDU2OGM0OWUwIiwiaXNzIjoic2ciLCJpYXQiOjE2NjIyMTUzNTAsImV4cCI6MTY2MjI1MTM1MH0.s-OlLMD20d2ko93KHo3PaRcd71tojiObI92t1SkH9FY";
//            System.out.println(req);
//            printWriter.print(req);
//            printWriter.flush();
//            InputStream inputStream = socket.getInputStream();
//            Scanner scanner=new Scanner(inputStream);
//            System.out.println("开始等待IO");
//            while (scanner.hasNextLine()){
//                System.out.println(scanner.nextLine());
//            }
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
