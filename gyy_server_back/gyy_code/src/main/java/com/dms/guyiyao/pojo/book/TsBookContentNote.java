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
 * 内容批注或标记表
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_book_content_note")
@ApiModel(value="TsBookContentNote对象", description="内容批注或标记表")
public class TsBookContentNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "PK")
    private String id;

    @ApiModelProperty(value = "表ts_book_content的ID，关联医书内容信息")
    private String ts_book_content_id;

    @ApiModelProperty(value = "批注内容信息")
    private String note_info;

    @ApiModelProperty(value = "排序，针对当前内容做的标记排序，针对当前用户可能做多条的情况")
    private Integer sequence;

    @ApiModelProperty(value = "当前信息类型：0(默认)批注，1标记")
    private String type;

    @ApiModelProperty(value = "默认0")
    private String status;

    @ApiModelProperty(value = "批注人员ID，源自表ts_user")
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
