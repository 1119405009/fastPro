package com.hzqykeji.banner.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzqykeji.travel.exception.FormException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

  public static final long EXPIRE_TIME = 604800;

  //一小时过期
  public static final long EXPIRE_HOUR_TIME = 3600;
  @Autowired
  ObjectMapper mapper;
  @Autowired
  RedisTemplate<String, String> redis;

  public String get(String key) {
    return redis.opsForValue().get(key);
  }

  public Long getExpire(String key) {
    return redis.getExpire(key, TimeUnit.SECONDS);
  }

  public <T> T get(String key, Class<T> clazz) {
    if (clazz == null) throw new IllegalArgumentException("类型不能为空");
    String value = redis.opsForValue().get(key);
    if (value != null) {
      try {
        return mapper.readValue(value, clazz);
      } catch (IOException e) {
        throw new FormException(String.format("反序列化%s失败", clazz.getName()));
      }
    }
    return null;
  }

  public List<String> getByPrefix(String prefix) {
    return redis.opsForValue().multiGet(redis.keys(prefix + "*"));
  }

  public boolean hasKey(String key) {
    return redis.hasKey(key);
  }

  public void set(String key, String value) {
    redis.opsForValue().set(key, value);
  }

  public void set(String key, String value, long expire) {
    redis.opsForValue().set(key, value);
    redis.expire(key, expire, TimeUnit.SECONDS);
  }

  public void set(String key, String value, Date expire) {
    redis.opsForValue().set(key, value);
    redis.expireAt(key, expire);
  }

  public void setList(String key, String value){
    redis.opsForList().rightPush(key, value);
  }

  public List<String> getList(String key){
    ListOperations<String, String> ops = redis.opsForList();
    return ops.range(key, 0, ops.size(key));
  }

  public long getListSize(String key){
    return redis.opsForList().size(key);
  }

  public void set(Cacheable cacheable) {
    try {
      redis.opsForValue().set(cacheable.getCacheKey(), mapper.writeValueAsString(cacheable));
    } catch (JsonProcessingException e) {
      throw new FormException(String.format("缓存%s序列化失败", cacheable.getCacheKey()));
    }
  }

  public void set(Cacheable cacheable, long expire) {
    set(cacheable);
    redis.expire(cacheable.getCacheKey(), expire, TimeUnit.SECONDS);
  }
  public void pattenRemove(String patten) {
    Set<String> keys = redis.keys(patten);
    redis.delete(keys);
  }

  public void remove(String key) {
    redis.delete(key);
  }

  public void remove(Cacheable cacheable) {
    redis.delete(cacheable.getCacheKey());
  }
}
