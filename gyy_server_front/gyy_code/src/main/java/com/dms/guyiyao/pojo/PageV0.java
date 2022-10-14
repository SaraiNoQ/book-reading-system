package com.dms.guyiyao.pojo;


import lombok.Data;

import java.util.List;
/*
* 后台系统 用户信息管理分页条pojo
* */
@Data
public class PageV0 {
    private List<TsUser_0> data;
    private Long total;
}
