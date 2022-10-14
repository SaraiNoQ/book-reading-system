package com.dms.guyiyao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dms.guyiyao.pojo.Regist_Info;
import com.dms.guyiyao.pojo.page.PageV0;
import com.dms.guyiyao.pojo.user.TsUser;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */

public interface ITsUserService extends IService<TsUser> {
 public final String RESET_PASSWORD="mima12345";
 PageV0 getUserList(Integer page, Integer size);
 boolean resetUserPassword(String username);
 boolean deleteUser(String id);
 PageV0 searchForUser(Integer page,Integer size,@Nullable String username, @Nullable String nickname, @Nullable String email);
 ModelAndView commonUserAdd(HttpServletRequest req, HttpServletResponse resp, Regist_Info regist_info,String userId) throws Exception;
}
