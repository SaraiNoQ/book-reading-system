package com.dms.guyiyao.security.userdetail;

import com.dms.guyiyao.dao.LoginDao;
import com.dms.guyiyao.pojo.Role;
import com.dms.guyiyao.pojo.Users;
import com.dms.guyiyao.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserdetailServiceImpl implements UserDetailsService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    LoginDao loginDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=null;
        username=username.trim();
        user=loginDao.getUserByusername(username);
        if (user==null)return null;
//        加入用户权限
         List<String> role=new ArrayList<>();
        return new UserdetailImpl(user, role);//返回的密码是密文密码交给底层代码进行验证/User 实现了Userdetail接口
    }


}

