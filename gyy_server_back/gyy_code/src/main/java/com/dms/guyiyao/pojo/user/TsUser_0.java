package com.dms.guyiyao.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
* 后台系统 用户管理模块pojo
* */
@Data
public class TsUser_0 {
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "email")
    private String email;
    @ApiModelProperty(value = "用户类型：0（默认）普通用户；1项目人员；2管理员")
    private String usertype;

}
