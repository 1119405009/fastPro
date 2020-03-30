package com.hzqykeji.banner.controller;


import com.hzqykeji.banner.exception.FormException;
import com.hzqykeji.banner.redis.RedisService;
import com.hzqykeji.banner.utils.Extractor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class BaseController<T> {
  @Autowired
  protected RedisService redis;
  Logger logger = LogManager.getLogger(getClass());

  @InitBinder
  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
    binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(Integer.class, null, true));
    binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, null, true));
    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    binder.registerCustomEditor(Date.class, new DateEditor());


  }
  private class DateEditor  extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = null;
      try {
        date = format.parse(text);
      } catch (ParseException e) {
        format = new SimpleDateFormat("yyyy-MM-dd");
        try {
          date = format.parse(text);
        } catch (ParseException e1) {

        }
      }
      setValue(date);
    }
  }

  protected void hasErrors(BindingResult result) {
    if (result.hasErrors()) {
      throw new FormException(Arrays.toString(Extractor.extractErrorMsg(result)));
    }
  }

  @ExceptionHandler(
      {
          FormException.class,
          HttpMessageNotReadableException.class,
          IllegalArgumentException.class,
          WxErrorException.class
      }
  )
  public ResponseEntity formExceptionHandler(Exception e) {
    logger.warn(e.getMessage());
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity sqlExceptionHandler(DataAccessException e) {
    logger.warn(e.getCause().getCause().getMessage());
    return ResponseEntity.badRequest().body(e.getCause().getCause().getMessage());
  }


}
