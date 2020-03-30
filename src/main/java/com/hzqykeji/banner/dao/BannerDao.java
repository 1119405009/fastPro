package com.hzqykeji.banner.dao;


import com.hzqykeji.banner.model.Banner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface BannerDao extends BaseDao<Banner, Serializable> {

  @Query(value = "update banner set position=position+1 where position>=?", nativeQuery = true)
  @Modifying
  @Transactional
  void positionAutoIncrement(Integer position);

  Banner findFristByModule(int module);
}
