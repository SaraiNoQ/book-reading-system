package com.dms.guyiyao.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
* 前台系统首页获取图像信息
* */
@Data
public class TsBook_0 {
    @ApiModelProperty(value = "书名")
    private String bookname;
    @ApiModelProperty(value = "作者")
    private String author;
    @ApiModelProperty(value = "朝代")
    private String dynasty;
    @ApiModelProperty(value = "典籍版本")
    private String version;
    @ApiModelProperty(value = "卷章节等信息")
    private String detailinfo;
    @ApiModelProperty(value = "封面图片URL")
    private String imgurl;
    private Integer sequence;
    private String introduction;
}
