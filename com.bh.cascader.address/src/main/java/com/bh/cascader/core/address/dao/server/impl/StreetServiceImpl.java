package com.bh.cascader.core.address.dao.server.impl;

import com.bh.cascader.core.address.dao.dao.AreaDAO;
import com.bh.cascader.core.address.dao.dao.StreetDAO;
import com.bh.cascader.core.address.dao.domian.AreaDO;
import com.bh.cascader.core.address.dao.domian.StreetDO;
import com.bh.cascader.core.address.dao.server.AreaService;
import com.bh.cascader.core.address.dao.server.StreetService;
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
public class StreetServiceImpl implements StreetService {

    @Resource
    private StreetDAO streetDAO;

    @Override
    public List<StreetDO> findByCode(String code) {
        Example example = new Example(StreetDO.class);
        example.createCriteria().andEqualTo("areaCode",code);
        List<StreetDO> list = streetDAO.selectByExample(example);
        return list;
    }
}
