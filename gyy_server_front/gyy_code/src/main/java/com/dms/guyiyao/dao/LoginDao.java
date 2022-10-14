package com.dms.guyiyao.dao;

import com.dms.guyiyao.pojo.Users;
import com.dms.guyiyao.pojo.entity.User;

public interface LoginDao {
    public User getUserByusername(String username);
    public Users getUserByEmail(String email);
}
