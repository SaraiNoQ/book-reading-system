package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.page.PageV0;
import com.dms.guyiyao.pojo.user.TsUser;
import com.dms.guyiyao.pojo.user.TsUser_0;
import com.dms.guyiyao.pojo.user.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Mapper
public interface TsUserMapper extends BaseMapper<TsUser> {
    List<TsUser_0> searchForUser(Integer page, Integer size, String username, String nickname, String email);
    Long searchForUser_count(String username, String nickname, String email);
    User getUser(String username);
    int containEmail(String email);
    int containUser(String username);


}
