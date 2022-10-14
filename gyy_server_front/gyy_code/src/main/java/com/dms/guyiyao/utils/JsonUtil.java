package com.dms.guyiyao.utils;



import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class JsonUtil {

 public  static JSONObject toJsonObject(Map map){
    JSONObject json=JSONObject.fromObject(map);
    return  json;
 }
 public  static  void returnJsonClient(HttpServletResponse resp, JSONObject json){
     PrintWriter out = null;
     try {
         //设定类容为json的格式
         resp.setContentType("application/json;charset=UTF-8");
         out = resp.getWriter();
         //写到客户端
         out.write(json.toString());
         out.flush();
     } catch (IOException e) {
         e.printStackTrace();
     }finally{
         if(out != null){
             out.close();
         }
     }

 }




}
