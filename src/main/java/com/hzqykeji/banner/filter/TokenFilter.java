package com.hzqykeji.banner.filter;


import com.hzqykeji.banner.redis.RedisService;

import com.hzqykeji.banner.utils.RequestUtil;
import com.hzqykeji.banner.utils.ResponseUtil;
import com.hzqykeji.banner.utils.SpringUtils;
import org.apache.axis.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebFilter(urlPatterns = {"/*"},filterName = "TokenFilter")
public class TokenFilter implements Filter {
  private Set<String> excludesPattern;
  protected PathMatcher pathMatcher = new AntPathMatcher();



  @Autowired
  RedisService redisService;


  @Override
  public void init(FilterConfig filterConfig) {
    excludesPattern = new HashSet<>();
    excludesPattern.add("/banner");


  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpResponse.addHeader("Access-Control-Allow-Origin", "*");
    httpResponse.addHeader("Access-Control-Expose-Headers", "Authorization, DNT, X-CustomHeader, Keep-Alive, User-Agent, X-Requested-With, If-Modified-Since, Cache-Control, Content-Type, X-Request-Token, Date");
    if (CorsUtils.isPreFlightRequest(httpRequest)) {
      httpResponse.addHeader("Access-Control-Allow-Credentials", "false");
      httpResponse.addHeader("Access-Control-Allow-Headers", "Authorization, DNT, X-CustomHeader, Keep-Alive, User-Agent, X-Requested-With, If-Modified-Since, Cache-Control, Content-Type, X-Request-Token, Date");
      httpResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, OPTIONS, DELETE");
      httpResponse.addHeader("Access-Control-Max-Age", "3600");
      return;
    }
    if (SpringUtils.isExclusion(pathMatcher, excludesPattern, httpRequest)) {
      chain.doFilter(request, response);
      return;
    } else {
      String token = httpRequest.getHeader("X-Request-Token");
      if (!StringUtils.isEmpty(token)) {
        String userId = redisService.get(token);
        if (userId != null) {
          httpRequest.setAttribute(RequestUtil.USER, userId);
          chain.doFilter(request, response);
          return;
        }
      }
    }
    ResponseUtil.noLoginResponse(httpResponse);
  }

  @Override
  public void destroy() {

  }
}
