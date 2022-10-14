package com.dms.guyiyao.dao.Impl;

import com.dms.guyiyao.dao.LoginDao;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.Users;
import com.dms.guyiyao.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Repository
public class LoginDaoImpl implements LoginDao  {
    @Autowired
   private JdbcTemplate jdbcTemplate;
    @Autowired
    private  TsUserMapper userMapper;
    public User getUserByusername(String username) {
//        String sql="select id, username,password from ts_user where username=? and status=0";
//        List<Users> query=jdbcTemplate.query(sql,new RowMapper<Users>(){
//            @Override
//            public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Users user=new Users();
//                user.setId(rs.getString("id"));
//                user.setUsername(rs.getString("username"));
//                user.setPassword(rs.getString("password"));
//                return  user;
//            }
//        },username);
//        if(query.isEmpty())return null;
     return    userMapper.getUser(username);


    }

    @Override
    public Users getUserByEmail(String email) {
        String sql="select id, username,password from ts_user where email=? and status=0";
        List<Users> query=jdbcTemplate.query(sql,new RowMapper<Users>(){
            @Override
            public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
                Users user=new Users();
                user.setId(rs.getString("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return  user;
            }
        },email);
        if (query.isEmpty())return null;
        return query.get(0);
    }
}
