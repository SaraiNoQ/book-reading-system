package com.dms.guyiyao.dao;

import com.dms.guyiyao.pojo.Regist_Info;
import com.dms.guyiyao.pojo.Users;

public interface RegistDao {
     String getUserNameByName(String username);
     boolean addUserInfo(String username,String password,String email);
    String getUserNameByEmail(String email);
}
