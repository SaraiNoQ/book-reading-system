package com.dms.guyiyao.dao.Impl;

import com.dms.guyiyao.dao.RegistDao;
import com.dms.guyiyao.pojo.Regist_Info;
import com.dms.guyiyao.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RegistDaoImpl implements RegistDao {

        @Autowired
        private JdbcTemplate jdbcTemplate;
    @Override
    public String getUserNameByName(String username) {
    String username_;
        try {
         username_=jdbcTemplate.queryForObject("select username from ts_user where  username=?",String.class,username);
    }catch (EmptyResultDataAccessException e){
        return "";
    }catch (IncorrectResultSizeDataAccessException e){
            return " ";
        }
        return  username_;
    }

    @Override
    public boolean addUserInfo(String username, String password, String email) {

            try {
            String sql="insert  into ts_user(id,username,password,`email`) values(REPLACE(UUID(),'-',''),?,?,?)";
                jdbcTemplate.update(sql, username, password, email);
            }catch (Exception e){
                return  false;
            }
            return  true;
    }

    @Override
    public String getUserNameByEmail(String email) {
        String username_;
        try {
            username_=jdbcTemplate.queryForObject("select username from ts_user where  email=?",String.class,email);
        }catch (EmptyResultDataAccessException e){
            return "";
        }catch (IncorrectResultSizeDataAccessException e){
            return " ";
        }
        return  username_;
    }
}
