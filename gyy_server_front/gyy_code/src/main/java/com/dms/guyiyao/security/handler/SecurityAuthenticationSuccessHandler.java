package com.dms.guyiyao.security.handler;


import com.dms.guyiyao.security.userdetail.UserdetailImpl;
import com.dms.guyiyao.utils.JsonUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登入成功
 * @author zzg
 *
 */
@Component("securityAuthenticationSuccessHandler")
public class SecurityAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//    @Autowired
//    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        Map<String,Object> map=new HashMap<>();
        map.put("status","success");
        WebAuthenticationDetails detailsInfo = (WebAuthenticationDetails) authentication.getDetails();
        UserdetailImpl principal = (UserdetailImpl) authentication.getPrincipal();
        logger.info("\n用户:【"+principal.getUsername()+"】\n在Ip:【"+detailsInfo.getRemoteAddress()+"】\n登录成功");
        JSONObject success= JsonUtil.toJsonObject(map);
        JsonUtil.returnJsonClient(response,success);
    }


}
