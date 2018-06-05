package com.bh.cascader.core.address.dao.server;

import com.bh.cascader.core.address.dao.domain.ProvinceDO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by xianghengyang on 2018/4/4 0004.
 */
public interface ProvinceService {

    @Cacheable(value="address")
    public List<ProvinceDO> findAll();

    public ProvinceDO findVOByCode(String code);
}
