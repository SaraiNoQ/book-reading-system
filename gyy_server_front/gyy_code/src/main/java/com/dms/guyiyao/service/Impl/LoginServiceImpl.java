package com.dms.guyiyao.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.TsUser;
import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.resp.ResponseResult;
import com.dms.guyiyao.security.userdetail.UserdetailImpl;
import com.dms.guyiyao.service.LoginService;
import com.dms.guyiyao.utils.JsonUtil;
import com.dms.guyiyao.utils.JwtUtil;
import com.dms.guyiyao.utils.Loggers;
import com.dms.guyiyao.utils.RedisCache;
import net.sf.json.JSONObject;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    TsUserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void sendEmail(HttpServletResponse resp,String email) {

        QueryWrapper<TsUser> mapper=new QueryWrapper();
        mapper.eq("email",email);
        if (userMapper.selectOne(mapper)==null){
            Map map =new HashMap();
            map.put("meg","邮箱不存在");
           JSONObject json= JsonUtil.toJsonObject(map);
           JsonUtil.returnJsonClient(resp,json);
        }else {
            String title="请点击链接重置密码";
            String url="http://localhost/resetPassword?";
        }

    }

    @Override
    public ResponseResult login(HttpServletRequest request, User user) {
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
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
//        if(Objects.isNull(authenticate)){
//            throw new RuntimeException("登录失败");
//        }
        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        UserdetailImpl loginUser = (UserdetailImpl) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        //把完整的用户信息存入redis  userid作为key
        redisCache.setCacheObject("login_front:"+userid,loginUser);
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
        redisCache.deleteObject("login_front:"+userid);
        Loggers.info("用户:"+loginUser.getUsername()+"注销成功");
        return new ResponseResult(200,"注销成功");
    }
}


