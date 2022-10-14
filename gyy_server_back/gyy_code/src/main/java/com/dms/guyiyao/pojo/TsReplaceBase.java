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
 * 异体字库表
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_replace_base")
@ApiModel(value="TsReplaceBase对象", description="异体字库表")
public class TsReplaceBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "简体字")
    private String modern;

    @ApiModelProperty(value = "古文字")
    private String orginal;

    @ApiModelProperty(value = "状态，默认值0")
    private String status;

    @ApiModelProperty(value = "录入人id,表ts_user中ID")
    private String create_by_id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime lastupdatetime;

    @ApiModelProperty(value = "最新修改人id（有可能是管理员重置密码）表ts_user中ID")
    private String lastupdate_by_id;

    private String memo;

    private String memo2;

    private String memo3;

    private String memo4;

    private String memo5;


}
