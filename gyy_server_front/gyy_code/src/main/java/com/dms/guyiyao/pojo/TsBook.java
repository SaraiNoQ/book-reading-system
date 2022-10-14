package com.dms.guyiyao.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 医药典籍总体概述表
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_book")
@ApiModel(value="TsBook对象", description="医药典籍总体概述表")
public class TsBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "典籍类型（备用），值来源于tc_code")
    private String book_type;

    @ApiModelProperty(value = "书名")
    private String bookname;

    @ApiModelProperty(value = "封面图片URL")
    private String imgurl;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "朝代")
    private String dynasty;

    @ApiModelProperty(value = "内容简介")
    private String introduction;

    @ApiModelProperty(value = "卷章节等信息")
    private String detailinfo;

    @ApiModelProperty(value = "状态，默认0")
    private String status;

    @ApiModelProperty(value = "录入人员")
    private String createrid;

    private LocalDateTime createtime;

    private String lastupdate_by_id;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime lastupdatetime;

    @ApiModelProperty(value = "备注")
    private String memo;

    private String memo2;

    private String memo3;

    private String memo4;

    private String memo5;

    private Integer sequence;
    @ApiModelProperty(value = "典籍版本")
    private String version;


}
