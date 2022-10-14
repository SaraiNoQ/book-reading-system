package com.dms.guyiyao.dao;

import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.user.Users;

public interface LoginDao {
     Users getUserByusername(String username);
     Users getUserByEmail(String email);
    String getUserType(String username);

    User getUser(String username);
}
