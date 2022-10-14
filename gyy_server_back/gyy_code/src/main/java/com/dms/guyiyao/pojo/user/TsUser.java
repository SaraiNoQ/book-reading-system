package com.dms.guyiyao.pojo.user;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_user")
@ApiModel(value="TsUser对象", description="用户表")
public class TsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "email")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户头像URL")
    private String icon;

    @ApiModelProperty(value = "状态，默认值0")
    private String status;

    @ApiModelProperty(value = "用户类型：0（默认）普通用户；1项目人员；2管理员")
    private String usertype;

    @ApiModelProperty(value = "组织机构ID,备用")
    private String depid;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime lastupdatetime;

    @ApiModelProperty(value = "最新修改人id（有可能是管理员重置密码）表ts_user中ID")
    private String lastupdate_by_id;

    private String memo;

    private String memo2;

    private String memo3;

    private String memo4;

    private String memo5;


}
