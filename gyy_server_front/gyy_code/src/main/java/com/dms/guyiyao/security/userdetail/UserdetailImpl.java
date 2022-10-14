package com.dms.guyiyao.security.userdetail;

import com.alibaba.fastjson.annotation.JSONField;
import com.dms.guyiyao.pojo.Role;
import com.dms.guyiyao.pojo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class UserdetailImpl implements UserDetails {
    private   User user;

    private List<String> role;
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;
public UserdetailImpl(User user, List<String> role){
   this.user=user;
    this.role=role;
}
    public UserdetailImpl(){
//        this.username= "NA";
//        this.password = "NA";
//        this.role = Collections.EMPTY_LIST;
    }
    /*将普通的List<Role>封装成框架需要的Role*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        //把permissions中String类型的权限信息封装成SimpleGrantedAuthority对象
//       authorities = new ArrayList<>();
//        for (String permission : permissions) {
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
//            authorities.add(authority);
//        }
        authorities = role.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "UserdetailImpl{" +
                "user=" + user +
                ", role=" + role +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }


}
