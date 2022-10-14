package com.dms.guyiyao.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel("异文信息")
@Data
public class ContentDiff {
    @ApiModelProperty("异文正文ID")
private String contentIdFrom;
    @ApiModelProperty("异常Id")
private String contentIdTo;
    @ApiModelProperty("异文正文")
private String contentFrom;
    @ApiModelProperty("异文")
private String contentTo;
    @ApiModelProperty("异文来源图书名")
    private String bookName;
    @ApiModelProperty("异文来源章节名")
    private String chapterName;
    @ApiModelProperty("异文来源章节id")
    private  String chapterId;
    @ApiModelProperty("异文正文json")
    private  String jsonFrom;
    @ApiModelProperty("异文json")
    private String jsonTo;

}
