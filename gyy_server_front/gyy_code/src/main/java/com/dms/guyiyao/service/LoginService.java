package com.dms.guyiyao.service;

import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.resp.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    public void sendEmail(HttpServletResponse resp,String email);

    ResponseResult login(HttpServletRequest request,User user);

    ResponseResult logout();
}
