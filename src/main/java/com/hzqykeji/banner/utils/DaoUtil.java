package com.hzqykeji.banner.utils;

import org.springframework.util.Assert;

import javax.persistence.TypedQuery;
import java.util.List;

public class DaoUtil {

  /**
   * Executes a count query and transparently sums up all values returned.
   *
   * @param query must not be {@literal null}.
   * @return
   */
  public static long executeCountQuery(TypedQuery<Long> query) {

    Assert.notNull(query, "TypedQuery must not be null!");

    List<Long> totals = query.getResultList();
    long total = 0L;

    for (Long element : totals) {
      total += element == null ? 0 : element;
    }

    return total;
  }
}
