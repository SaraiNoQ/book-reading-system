package com.dms.guyiyao.security.filter;

import com.alibaba.fastjson.JSON;
import com.dms.guyiyao.pojo.resp.ResponseResult;
import com.dms.guyiyao.security.userdetail.UserdetailImpl;
import com.dms.guyiyao.utils.JwtUtil;
import com.dms.guyiyao.utils.Loggers;
import com.dms.guyiyao.utils.RedisCache;
import com.dms.guyiyao.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        String header = request.getHeader("Accept-Language");
//        System.out.println(header+"dd");
//        System.out.println("token:"+token);
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userid="";
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            Loggers.error("token:【"+token+"】非法");
            ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(),"用户认证失败请查询登录");
            String json = JSON.toJSONString(result);
            //处理异常
//            TODO 为啥这response了之后还会指 向下面的句子欸
            WebUtils.renderString(response,json);
            return;
        }
        //Token合法进一步验证
        Loggers.info("token格式合法开始获取redis中的用户信息");
        String redisKey = "login_back:" + userid;
//        TODO redis序列化还存在问题。获取的用户信息缺失。暂时默认所有后台用户都有权限
        UserdetailImpl loginUser = JSON.toJavaObject(redisCache.getCacheObject(redisKey), UserdetailImpl.class);
        if(Objects.isNull(loginUser)){
            Loggers.error("用户处于未登录状态或token对应账户不存在");
            ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(),"用户认证失败请查询登录");
            String json = JSON.toJSONString(result);
            WebUtils.renderString(response,json);
            return;
        }else{
            Loggers.info("Redis中命中到对应用户");
//        存入SecurityContextHolder
            ArrayList<SimpleGrantedAuthority> tmpRole = new ArrayList<>();
            tmpRole.add(new SimpleGrantedAuthority(loginUser.getRole().get(0)));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser,null,tmpRole);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            Loggers.info("放行信息放入过滤链成功");
//        放行
            filterChain.doFilter(request, response);
        }
    }
}
