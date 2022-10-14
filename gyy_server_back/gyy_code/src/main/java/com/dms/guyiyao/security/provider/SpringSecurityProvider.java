package com.dms.guyiyao.security.provider;

import com.dms.guyiyao.security.userdetail.UserdetailImpl;
import com.dms.guyiyao.security.userdetail.UserdetailServiceImpl;
import com.dms.guyiyao.utils.Loggers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class SpringSecurityProvider implements AuthenticationProvider {
    @Autowired
    private UserdetailServiceImpl userDetailService;

    @Override
    public Authentication authenticate(@RequestBody Authentication authentication) throws AuthenticationException {
        String userName=authentication.getName();
        String password= (String) authentication.getCredentials();

        // 数据库中的账号和密码
        UserdetailImpl userInfo = (UserdetailImpl) userDetailService.loadUserByUsername(userName);
        //抛出对应逻辑
        if (userInfo == null) {
            Loggers.error("用户不存在");
            throw new UsernameNotFoundException("当前用户不存在");
        }
        Boolean isPasswordRight=new BCryptPasswordEncoder().matches(password,userInfo.getPassword());
        if (!isPasswordRight){
            Loggers.error("\n用户名:【"+userName+"】,密码:【"+password+"】密码错误");
            throw new BadCredentialsException("密码错误");}

//        if (!userInfo.getRole().contains("1")&&!userInfo.getRole().contains("2")){
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo.getUsername(),userInfo.getPassword(),null);
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            throw  new AccessDeniedException("权限不足");
//        }


        return new UsernamePasswordAuthenticationToken(userInfo, userInfo.getPassword());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // TODO Auto-generated method stub
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

    // 拓展获取用户查询服务
    public UserDetailsService getUserDetailsService(){
        return this.userDetailService;
    }
}
