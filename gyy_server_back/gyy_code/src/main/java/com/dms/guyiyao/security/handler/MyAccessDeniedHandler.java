package com.dms.guyiyao.security.handler;


import com.dms.guyiyao.pojo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
//@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse resp,
                       AccessDeniedException e) throws IOException {
        //1 设置响应状态码 >这里设置403
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        resp.setContentType("application/json:charset=utf-8");
        PrintWriter writer = resp.getWriter();
        writer.println("{\"code\":\"403\",\"msg\":\"无权限\"}");
        writer.flush();
        writer.close();
    }
}
