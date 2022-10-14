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
 * 医书内容与原文关联表
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_book_content_orginal")
@ApiModel(value="TsBookContentOrginal对象", description="医书内容与原文关联表")
public class TsBookContentOrginal implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "PK")
    private String id;

    @ApiModelProperty(value = "表ts_book_content的ID，关联医书内容信息")
    private String ts_book_content_id;

    @ApiModelProperty(value = "表ts_book_orginal的ID，关联医书原文信息")
    private String ts_book_orginal_id;

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
