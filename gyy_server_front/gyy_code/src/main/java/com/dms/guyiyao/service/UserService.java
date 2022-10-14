package com.dms.guyiyao.service;

import com.dms.guyiyao.pojo.ImgForm;
import com.dms.guyiyao.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    UserInfo getUserInfo(String username);
    String updateIcon(String username, ImgForm imgForm) throws Exception;

    String updateNickName(String username, String nickName);

    String updatePass(String username, String newPass);

    UserInfo getUserInfoByemail(String username);
}
