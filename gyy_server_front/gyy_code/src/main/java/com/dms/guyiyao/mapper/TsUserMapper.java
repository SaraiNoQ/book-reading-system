package com.dms.guyiyao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dms.guyiyao.pojo.TsUser;
import com.dms.guyiyao.pojo.UserInfo;
import com.dms.guyiyao.pojo.entity.User;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
public interface TsUserMapper extends BaseMapper<TsUser> {
    int containEmail(String email);
    int containUser(String username);

    UserInfo getUserInfo(String username);

    int updateIcon(String username, String url);

    int updateNickName(String username, String nickname);

    int updatePass(String username, String password);

    UserInfo getUserInfoByEmail(String username);

    User getUser(String username);
}
