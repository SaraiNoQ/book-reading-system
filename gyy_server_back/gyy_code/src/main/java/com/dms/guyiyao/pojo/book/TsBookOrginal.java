package com.dms.guyiyao.pojo.book;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 医书原文表
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_book_orginal")
@ApiModel(value="TsBookOrginal对象", description="医书原文表")
public class TsBookOrginal implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "PK")
    private String id;

    @ApiModelProperty(value = "表ts_book_chapter的ID，关联医书卷章节信息")
    private String ts_book_chapter_id;

    @ApiModelProperty(value = "原文图片名称")
    private String imgname;

    @ApiModelProperty(value = "原文图片信息")
    private String imginfo;

    @ApiModelProperty(value = "原文URL地址")
    private String imgurl;

    @ApiModelProperty(value = "排序")
    private Integer sequence;

    @ApiModelProperty(value = "默认0")
    private String status;

    private String create_by_id;

    @ApiModelProperty(value = "插入时系统时间")
    private LocalDateTime createtime;

    private String lastupdate_by_id;

    private LocalDateTime lastupdatetime;

    @ApiModelProperty(value = "备注")
    private String memo;

    private String memo2;

    private String memo3;

    private String memo4;

    private String memo5;


}
