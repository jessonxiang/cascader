package com.bh.cascader.core.address.dao.server.impl;

import com.bh.cascader.core.address.dao.dao.ProvinceDAO;
import com.bh.cascader.core.address.dao.domian.ProvinceDO;
import com.bh.cascader.core.address.dao.server.ProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xianghengyang on 2018/4/4 0004.
 */
@Service
@Slf4j
public class ProvinceServiceImpl implements ProvinceService {

    @Resource
    private ProvinceDAO provinceDAO;

    @Override
    public List<ProvinceDO> findAll() {
        List<ProvinceDO> provinceDOS = provinceDAO.selectAll();
        return provinceDOS;
    }
}
