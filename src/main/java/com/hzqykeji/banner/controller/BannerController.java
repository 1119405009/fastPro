package com.hzqykeji.banner.controller;

import com.hzqykeji.banner.model.form.BannerForm;
import com.hzqykeji.banner.model.query.BannerQuery;
import com.hzqykeji.banner.service.BannerService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("banner")
@Api(tags = "广告接口服务")
public class BannerController extends BaseController {


  @Autowired
  BannerService bannerService;

  @PostMapping
  @ApiOperation(value = "添加广告", response = ResponseEntity.class,tags = "广告2.0")
  @ApiResponses({
      @ApiResponse(code = 200, message = "成功返回"),
      @ApiResponse(code = 400, message = "错误返回")
  })
  @ApiImplicitParams(@ApiImplicitParam(name = "X-Request-Token", paramType = "header", required = true))
  public ResponseEntity post(@RequestBody @Valid BannerForm form, BindingResult result) {
    hasErrors(result);
    form.valid();
    return ResponseEntity.ok(bannerService.save(form));
  }

  @PutMapping("{id}")
  @ApiOperation(value = "修改广告", response = ResponseEntity.class,tags = "广告2.0")
  @ApiResponses({
      @ApiResponse(code = 200, message = "成功返回"),
      @ApiResponse(code = 400, message = "错误返回")
  })
  @ApiImplicitParams(@ApiImplicitParam(name = "X-Request-Token", paramType = "header", required = true))
  public ResponseEntity put(@PathVariable Long id, @RequestBody @Valid BannerForm form, BindingResult result) {
    form.setId(id);
    return post(form, result);
  }

  @GetMapping
  @ApiOperation(value = "广告列表", response = ResponseEntity.class)
  @ApiImplicitParams(@ApiImplicitParam(name = "X-Request-Token", paramType = "header", required = true))
  public ResponseEntity list(BannerQuery query) {
    return ResponseEntity.ok(bannerService.findAll(query));
  }

  @GetMapping("{id}")
  @ApiOperation(value = "广告详情", response = ResponseEntity.class,tags = "广告2.0")
  @ApiImplicitParams(@ApiImplicitParam(name = "X-Request-Token", paramType = "header", required = true))
  public ResponseEntity list(@PathVariable Long id) {
    return ResponseEntity.ok(bannerService.findOne(id));
  }

}
