package com.dms.guyiyao.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class SendEmailUtil {
    /*问题:
             使用spring容器时的时候无法直接声明static
    * 解决方案:
    在初始化方法中进行传递
    * */
    @Autowired
    private  JavaMailSender mailSender_pre;
    private static JavaMailSender mailSender;

    /*初始化方法传值*/
    @PostConstruct
    public void init(){
        mailSender=mailSender_pre;
    }
    /*不带附件的邮件方法*/
    public  static void sendSimpleMail(String maker_email,String receive_email,String title,String content){
        SimpleMailMessage  message = new SimpleMailMessage();
        message.setFrom(maker_email);
        message.setTo(receive_email);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }


}
