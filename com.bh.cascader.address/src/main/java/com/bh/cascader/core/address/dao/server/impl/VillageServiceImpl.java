package com.bh.cascader.core.address.dao.server.impl;

import com.bh.cascader.core.address.dao.dao.StreetDAO;
import com.bh.cascader.core.address.dao.dao.VillageDAO;
import com.bh.cascader.core.address.dao.domian.StreetDO;
import com.bh.cascader.core.address.dao.domian.VillageDO;
import com.bh.cascader.core.address.dao.server.StreetService;
import com.bh.cascader.core.address.dao.server.VillageService;
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
public class VillageServiceImpl implements VillageService {
    @Resource
    private VillageDAO villageDAO;
    @Override
    public List<VillageDO> findByCode(String code) {
        Example example = new Example(VillageDO.class);
        example.createCriteria().andEqualTo("streetCode",code);
        List<VillageDO> list = villageDAO.selectByExample(example);
        return list;
    }
}
