package com.dms.guyiyao.security.handler;


import com.dms.guyiyao.utils.JsonUtil;
import net.sf.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登入失败
 * @author zzg
 *
 */
@Component("securityAuthenticationFailHandler")
public class SecurityAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        Map<String,Object>map=new HashMap<>();
        if (exception.getMessage().equals("密码错误")) {
            map.put("status", "账号或密码错误");
            JSONObject success = JsonUtil.toJsonObject(map);
            JsonUtil.returnJsonClient(response, success);
        }else  if (exception.getMessage().equals("请用管理员账号登录后台系统"));{
            map.put("status", "请使用管理员账号登录");
            System.out.println(exception);
            JSONObject success = JsonUtil.toJsonObject(map);
            JsonUtil.returnJsonClient(response, success);
        }
    }


}
