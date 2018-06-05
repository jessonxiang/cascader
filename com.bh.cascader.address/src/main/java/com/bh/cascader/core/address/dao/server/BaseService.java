package com.bh.cascader.core.address.dao.server;

import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by xianghengyang on 2018/4/4 0004.
 */
public interface BaseService<T> {
    /**
     * 根据上一级code查询code对应下的所有子级
     * @param code
     * @return
     */
    @Cacheable(value="address")
    public List<T> findByCode(String code);

    @Cacheable(value="addressVO")
    public T findVOByCode(String code);
}
