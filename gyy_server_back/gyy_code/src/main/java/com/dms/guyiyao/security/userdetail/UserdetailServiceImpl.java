package com.dms.guyiyao.security.userdetail;

import com.dms.guyiyao.dao.LoginDao;
import com.dms.guyiyao.pojo.Role;
import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.AccessControlException;
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
        user=loginDao.getUser(username);
//        user=loginDao.getUserByusername(username);

//        if (user==null)user=loginDao.getUserByEmail(username);
        if (user==null)return null;
        if (!user.getUserType().equals("1")&&!user.getUserType().equals("2"))throw new AccessDeniedException("Access is denied");
//        String usertype=loginDao.getUserType(username);
//        加入用户权限
        List<String> role=new ArrayList<>();
        /*针对单一权限的控制*/
        role.add(user!=null?user.getUserType():null);
        return new UserdetailImpl(user, role);//返回的密码是密文密码交给底层代码进行验证/User 实现了Userdetail接口
    }
}

