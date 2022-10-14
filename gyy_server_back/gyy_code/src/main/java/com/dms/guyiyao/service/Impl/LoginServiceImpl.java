package com.dms.guyiyao.service.Impl;

import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.resp.ResponseResult;
import com.dms.guyiyao.security.userdetail.UserdetailImpl;
import com.dms.guyiyao.service.LoginService;
import com.dms.guyiyao.utils.JwtUtil;
import com.dms.guyiyao.utils.Loggers;
import com.dms.guyiyao.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user, HttpServletRequest request) {
        String username = user.getUserName();
        String password = user.getPassword();
        username=username.trim();
        password=password.trim();
        String reg_username= "^[a-zA-Z0-9_-]{4,16}$";       //4-16位用户名(可以是数字，字母，下划线)
        String reg_password= "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{6,15}$";     //6-15位密码（字母数字特殊字符至少包含两种）
        String reg_email="^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";//邮箱正则
        boolean isMathch_username= Pattern.matches(reg_username,username);
        boolean isMathch_password= Pattern.matches(reg_password,password);
        boolean isMathch_email=Pattern.matches(reg_email,username);
        if (!isMathch_password) {
            Loggers.error("密码格式错误");
            throw new BadCredentialsException("密码格式错误");
        }
        if (!isMathch_username&&!isMathch_email){
            Loggers.error("账号格式错误");
            throw  new BadCredentialsException("账号格式错误");
        }

        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());

       /*
       * TODO 笔记
       *  登录接口其实过了两次过滤器链。
       *    第一次进入的时候被jwtFilter直接放行。虽然没有任何经过验证的authentication
       *    但是因为接口在适配器中允许通过。所以被放行了
       *    放行之后进入接口逻辑区。逻辑区中调用了验证管理器从而第二次进入验证。需要注意的是需要在进行验证之前在上下文
       *    中放入一个未被验证的令牌：
       *        因为在后面的provider验证逻辑中。如果需要抛出权限不足的异常。按照springsecurity的逻辑权限不住的前提是
       *        用户登录成功。但是权限不足的逻辑。而在springsecurity的异常处理链当中如果上下文中不存在令牌在匿名访问
       *        处理链当中会在上下文中加入一个匿名访问令牌。这个令牌在后面的异常处理链当中拿到这个匿名访问令牌会把provider
       *        中抛出的权限不足异常当作认证失败异常处理从而直接调用对应的认证异常处理器。无法触发权限不足异常器
       * */
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }

        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        UserdetailImpl loginUser = (UserdetailImpl) authenticate.getPrincipal();

        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        //把完整的用户信息存入redis  userid作为key
        redisCache.setCacheObject("login_back:"+userid,loginUser);
        Loggers.info("\nIp:【"+request.getRemoteAddr()+"】用户名:【"+user.getUserName()+"】,密码：【"+user.getPassword()+"】登录成功");
        return new ResponseResult(200,"登录成功",map);
    }

    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserdetailImpl loginUser = (UserdetailImpl) authentication.getPrincipal();
        String userid = loginUser.getUser().getId();
        //删除redis中的值
        redisCache.deleteObject("login_back:"+userid);
        Loggers.info("用户:"+loginUser.getUsername()+"注销成功");
        return new ResponseResult(200,"注销成功");
    }
}
