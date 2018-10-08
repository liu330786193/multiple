package com.lyl.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/30 下午3:03
 */
@AllArgsConstructor
@ApiModel
@Data
public class User implements Serializable {

    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;

}
