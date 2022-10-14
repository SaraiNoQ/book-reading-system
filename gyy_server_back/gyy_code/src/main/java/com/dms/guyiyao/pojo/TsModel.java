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
 * 系统模块表
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_model")
@ApiModel(value="TsModel对象", description="系统模块表")
public class TsModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "PK")
    private String id;

    @ApiModelProperty(value = "模块名称")
    private String model_name;

    @ApiModelProperty(value = "模块相对地址")
    private String model_url;

    @ApiModelProperty(value = "父ID")
    private String parentid;

    @ApiModelProperty(value = "属于")
    private String belongto;

    @ApiModelProperty(value = "所属层级（根树0，依次加1）")
    private Integer grade;

    @ApiModelProperty(value = "同层级排序")
    private Integer sequence;

    @ApiModelProperty(value = "默认0")
    private String status;

    @ApiModelProperty(value = "模块类型，0（默认）通用，1用户，2管理")
    private String model_type;

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
