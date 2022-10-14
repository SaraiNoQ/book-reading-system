package com.dms.guyiyao.security.handler;

import com.dms.guyiyao.utils.JsonUtil;
import net.sf.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String,Object> map=new HashMap<>();
        map.put("status","success");
        JSONObject success= JsonUtil.toJsonObject(map);
        JsonUtil.returnJsonClient(response,success);
    }
}
