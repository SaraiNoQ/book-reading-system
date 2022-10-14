package com.dms.guyiyao.service;

import com.dms.guyiyao.pojo.Regist_Info;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RegistService {
    public ModelAndView regist_sub(HttpServletRequest req, HttpServletResponse resp,Regist_Info regist_info) throws Exception;
}
