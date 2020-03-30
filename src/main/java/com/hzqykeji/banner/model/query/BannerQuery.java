package com.hzqykeji.banner.model.query;
/*
 * @Author felix
 * @Description
 * @Date 20:04
 */


import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BannerQuery extends PageQuery {
    @ApiParam("广告名")
    private String name;
    @ApiParam(value = "状态",allowableValues = "10=待使用,20=使用中,30=已过期")
    private Integer status;
}
