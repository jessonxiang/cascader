package com.bh.cascader.core.address.dao.server.impl;

import com.bh.cascader.core.address.dao.dao.CityDAO;
import com.bh.cascader.core.address.dao.domain.CityDO;
import com.bh.cascader.core.address.dao.server.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xianghengyang on 2018/4/4 0004.
 */
@Service
@Slf4j
public class CityServiceImpl implements CityService {

    @Resource
    private CityDAO cityDAO;
    @Override
    public List<CityDO> findByCode(String code) {
        Example example = new Example(CityDO.class);
        example.createCriteria().andEqualTo("provinceCode",code);
        List<CityDO> list = cityDAO.selectByExample(example);
        return list;
    }

    @Override
    public CityDO findVOByCode(String code) {
        Example example = new Example(CityDO.class);
        example.createCriteria().andEqualTo("value",code);
        List<CityDO> list = cityDAO.selectByExample(example);
        return list.isEmpty() ? null:list.get(0);
    }
}
