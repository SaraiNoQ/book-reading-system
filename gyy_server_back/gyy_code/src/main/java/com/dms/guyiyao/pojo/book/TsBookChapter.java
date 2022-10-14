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
 * 医书序卷章节表
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_book_chapter")
@ApiModel(value="TsBookChapter对象", description="医书序卷章节表")
public class TsBookChapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "PK")
    private String id;

    @ApiModelProperty(value = "表ts_book的ID，关联医书概括信息")
    private String ts_book_id;

    @ApiModelProperty(value = "父ID")
    private String parentid;

    @ApiModelProperty(value = "序卷章节名称")
    private String chapter_name;

    @ApiModelProperty(value = "属于")
    private String belongto;

    @ApiModelProperty(value = "所属层级（根树0，依次加1）")
    private Integer grade;

    @ApiModelProperty(value = "同层级排序")
    private Integer sequence;

    @ApiModelProperty(value = "默认0")
    private String status;

    @ApiModelProperty(value = "书中节的类型，0书名，1序，2卷，3章（默认），4节")
    private String book_type;

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
