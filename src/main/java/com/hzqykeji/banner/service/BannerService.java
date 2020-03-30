
package com.hzqykeji.banner.service;

import com.hzqykeji.banner.model.Banner;
import com.hzqykeji.banner.model.form.BannerForm;
import com.hzqykeji.banner.model.query.BannerQuery;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public interface BannerService extends BaseService<Banner, Serializable> {

  Banner save(BannerForm form);

  Page<Banner> findAll(BannerQuery query);

}
