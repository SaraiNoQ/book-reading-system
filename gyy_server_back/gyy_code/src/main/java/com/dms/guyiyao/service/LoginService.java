package com.dms.guyiyao.service;

import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.resp.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    ResponseResult login(User user, HttpServletRequest request);

    ResponseResult logout();
}
