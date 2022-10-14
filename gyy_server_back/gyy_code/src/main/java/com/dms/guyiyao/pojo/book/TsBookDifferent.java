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
 * 异文信息关联表
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_book_different")
@ApiModel(value="TsBookDifferent对象", description="异文信息关联表")
public class TsBookDifferent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "PK")
    private String id;

    @ApiModelProperty(value = "表ts_book_content的ID，from关联医书内容信息")
    private String ts_book_content_id_from;

    @ApiModelProperty(value = "表ts_book_content的ID，to关联医书内容信息")
    private String ts_book_content_id_to;

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
