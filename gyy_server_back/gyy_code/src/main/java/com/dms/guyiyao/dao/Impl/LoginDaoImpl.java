package com.dms.guyiyao.dao.Impl;

import com.dms.guyiyao.dao.LoginDao;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.user.Users;
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
    private TsUserMapper userMapper;
    public Users getUserByusername(String username) {
        String sql="select id, username,password from ts_user where username=? and status=0";
        List<Users> query=jdbcTemplate.query(sql,new RowMapper<Users>(){
            @Override
            public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
                Users user=new Users();
                user.setId(rs.getString("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return  user;
            }
        },username);
        if(query.isEmpty())return null;
        return query.get(0);
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

    @Override
    public String getUserType(String username) {
        String sql="SELECT usertype from ts_user WHERE username =?";
        List<String> usertypes=jdbcTemplate.query(sql,new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return   rs.getString("usertype");
            }
        },username);
        if (usertypes.isEmpty()){
             sql="SELECT usertype from ts_user WHERE email =?";
             usertypes=jdbcTemplate.query(sql,new RowMapper<String>(){
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return   rs.getString("usertype");
                }
            },username);
        }
        if (usertypes.isEmpty())return  null;
        return  usertypes.get(0);
    }

    @Override
    public User getUser(String username) {
      User users= userMapper.getUser(username);
        return users;
    }
}
