package com.hzqykeji.banner.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.hzqykeji.banner.exception.FormException;
import com.hzqykeji.banner.utils.RegexConstant;
import com.hzqykeji.banner.utils.RegexUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ApiModel("广告表单")
public class BannerForm implements Serializable {
  @JsonIgnore
  private Long id;
  @Min(value = 0,message = "排序字段不能小于0")
  @NotNull(message = "排序字段不能为空")
  @ApiModelProperty(value = "排序字段",required = true)
  private Integer position;
  //@Pattern(regexp = RegexConstant.REGEX_URL,message = "请输入合法的跳转链接")
  @ApiModelProperty(value = "跳转链接")
  private String link;
  @NotEmpty(message = "广告图不能为空")
  @Pattern(regexp = RegexConstant.REGEX_URL,message = "请输入合法的广告图链接")
  @ApiModelProperty(value = "广告图链接",required = true)
  private String photo;
  @ApiModelProperty(value = "描述")
  private String remark;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @ApiModelProperty(value = "生效时间",required = true, example = "1970-01-01 00:00:00")
  private Date validAt = new Date();
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @ApiModelProperty(value = "过期时间",required = true, example = "1970-01-01 00:00:00")
  private Date expireAt;

  //=======1.首页广告轮播 2 生活广告轮播  3新鲜广告轮播  4推荐广告轮播  5 旅游广告轮播=========//
  @ApiModelProperty("模块展示 1.首页广告轮播 2 生活广告轮播  3新鲜广告轮播  4推荐广告轮播  5 旅游广告轮播  6首页固定广告 ")
  @NotNull(message ="模块展示不能为空!")
  private Integer module;

  //=======跳转类型   1 默认链接  2 商品详情  3旅游详情 4专题=========//
  @ApiModelProperty("跳转类型  1 默认链接  2 商品详情  3旅游详情 4专题")
  @NotNull(message ="跳转类型 不能为空!")
  private Integer jumpType;

  public BannerForm(Integer position, String link, String photo, Date validAt, Date expireAt, Integer module, Integer jumpType) {
    this.position = position;
    this.link = link;
    this.photo = photo;
    this.validAt = validAt;
    this.expireAt = expireAt;
    this.module=module;
    this.jumpType=jumpType;
  }

  public BannerForm() {
  }

  public void valid() {
    if(expireAt != null){
      if(expireAt.before(validAt)) throw new FormException("过期时间必须大于生效时间");
    }
    if(jumpType!=1){
      if(!RegexUtil.isMatch("^[0-9]*$", link)) throw new FormException("2 商品详情  3旅游详情 4专题 请输入合法的id");
    }else {
       if(!RegexUtil.isMatch(RegexConstant.REGEX_URL, link)) throw new FormException("默认链接 请输入合法的广告图链接");
    }
  }

  public static void main(String[] args) {
    System.out.println(RegexUtil.isMatch("^[0-9]*$", "12"));
  }
}
