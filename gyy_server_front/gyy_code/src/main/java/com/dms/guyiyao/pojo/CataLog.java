package com.dms.guyiyao.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;
@ApiModel("目录树结构")
@Data
public class CataLog {
    @ApiModelProperty("章节id")
    private     String  id;
   @ApiModelProperty("章节层级")
    private  int grade;
   @ApiModelProperty("章节名")
    private  String chapter;
   @ApiModelProperty("目录树递归结构")
    List<CataLog>cataLog;


}
