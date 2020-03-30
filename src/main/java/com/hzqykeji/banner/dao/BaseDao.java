package com.hzqykeji.banner.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by teruo on 2019/5/9.
 */
@NoRepositoryBean
public interface BaseDao<T,ID extends Serializable> extends CrudRepository<T,ID> {

  Page<T> findAll(Pageable pageable);

  Page<T> findAll(Specification<T> specification, Pageable pageable);

  List<T> findAll(Specification<T> specification);


  T   listOrPage(Specification<T> specification, Pageable pageable);
}
