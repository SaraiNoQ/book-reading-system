package com.dms.guyiyao.security.handler;

import com.dms.guyiyao.utils.JsonUtil;
import net.sf.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
//@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        PrintWriter writer = response.getWriter();
        Map map=new HashMap<>();
        map.put("status","Unlogin");
        JSONObject jsonObject = JsonUtil.toJsonObject(map);
        JsonUtil.returnJsonClient(response,jsonObject);
    }
}
