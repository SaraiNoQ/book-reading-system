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
//        String redirectUrl = "/loginPage.html" + URLEncoder.encode(exception.getMessage(),"UTF-8");
        Map<String,Object>map=new HashMap<>();
        map.put("status","false");
        JSONObject success=JsonUtil.toJsonObject(map);
        JsonUtil.returnJsonClient(response,success);
//        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }


}
