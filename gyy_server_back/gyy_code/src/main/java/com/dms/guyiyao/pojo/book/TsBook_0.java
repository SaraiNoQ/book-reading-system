package com.dms.guyiyao.pojo.book;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/*
* 后台系统图书管理基本信息pojo
* */
@Data
public class TsBook_0 {

    private String bookId;

    @ApiModelProperty(value = "书名")
    private String bookname;
    @ApiModelProperty(value = "作者")
    private String author;
    @ApiModelProperty(value = "朝代")
    private String dynasty;
    private LocalDateTime createtime;
    @ApiModelProperty(value = "录入人员")
    private String creater;
    private  String imgurl;
    private  String introduction;
    private Integer sequence;
}
