package com.hzqykeji.banner.model.query;
/*
 * @Author felix
 * @Description
 * @Date 20:04
 */


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class PageQuery implements Serializable {
    //状态
    private Integer status;
    @ApiModelProperty("页数")
    private int pageNum = 0;
    @ApiModelProperty("每页记录数")
    private int pageSize = 6;



}
