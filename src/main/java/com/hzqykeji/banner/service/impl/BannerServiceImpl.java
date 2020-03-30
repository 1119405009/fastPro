package com.hzqykeji.banner.service.impl;


import com.hzqykeji.banner.dao.BannerDao;
import com.hzqykeji.banner.exception.FormException;
import com.hzqykeji.banner.model.Banner;
import com.hzqykeji.banner.model.form.BannerForm;
import com.hzqykeji.banner.model.query.BannerQuery;
import com.hzqykeji.banner.service.BannerService;
import com.hzqykeji.banner.utils.Extractor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BannerServiceImpl extends BaseServiceImpl<Banner, Serializable> implements BannerService {
  BannerDao bannerDao;

  public BannerServiceImpl(BannerDao dao) {
    super(dao);
    bannerDao = dao;
  }

  @Override
  public Banner save(BannerForm form) {
    Banner banner = new Banner();
    if (form.getId() != null) {
      banner = bannerDao.findById(form.getId()).orElse(null);
      if (banner == null) throw new FormException("广告不存在");

    }
    //首页固定广告位1个
    if(form.getId()==null&&form.getModule()==6){
       if(bannerDao.findFristByModule(6)!=null) throw new FormException("首页固定广告已存在");
    }
    bannerDao.positionAutoIncrement(form.getPosition());
    BeanUtils.copyProperties(form, banner, Extractor.getNullPropertyNames(form));
    return bannerDao.save(banner);
  }

  @Override
  public Page<Banner> findAll(BannerQuery query) {
    return bannerDao.findAll((root, cq, cb) -> {
      List<Predicate> conditions = new ArrayList();
      if (StringUtils.isNotEmpty(query.getName())) {
        conditions.add(cb.like(root.get("name"), String.format("%%%s%%", query.getName())));
      }
      if (query.getStatus() != null) {
        switch (query.getStatus().intValue()) {
          //待使用
          case 10:
            conditions.add(cb.greaterThan(root.get("validAt").as(Date.class), new Date()));
            break;
          //使用中
          case 20:
            conditions.add(cb.lessThan(root.get("validAt").as(Date.class), new Date()));
            conditions.add(cb.greaterThan(root.get("expireAt").as(Date.class), new Date()));
            break;
          //已过期
          case 30:
          default:
            conditions.add(cb.lessThan(root.get("expireAt").as(Date.class), new Date()));
        }
      }
      return cq.where(conditions.toArray(new Predicate[conditions.size()])).getRestriction();
    }, PageRequest.of(query.getPageNum(), query.getPageSize(), Sort.Direction.DESC, "id"));
  }

}
