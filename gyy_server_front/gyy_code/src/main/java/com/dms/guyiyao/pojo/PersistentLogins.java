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
 * 
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("persistent_logins")
@ApiModel(value="PersistentLogins对象", description="")
public class PersistentLogins implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String series;

    private String token;

    private LocalDateTime last_used;


}
