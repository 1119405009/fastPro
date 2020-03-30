package com.hzqykeji.banner.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信公众号相关配置
 *
 * @author teruo
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "business.config.storage.alibaba")
public class AliOssProperties {

  /**
   * oss访问凭据
   */
  private String accessKey;

  /**
   * oss秘钥
   */
  private String secretKey;

  /**
   * oss服务器地址
   */
  private String serverAddr;

  /**
   * 产品编号
   */
  private String bucket;

  /**
   * oss域名
   */
  private String domain;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
