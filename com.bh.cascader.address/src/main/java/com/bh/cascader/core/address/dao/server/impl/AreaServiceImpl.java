package com.bh.cascader.core.address.dao.server.impl;

import com.bh.cascader.core.address.dao.dao.AreaDAO;
import com.bh.cascader.core.address.dao.domain.AreaDO;
import com.bh.cascader.core.address.dao.server.AreaService;
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
public class AreaServiceImpl implements AreaService {

    @Resource
    private AreaDAO areaDAO;
    @Override
    public List<AreaDO> findByCode(String code) {
        Example example = new Example(AreaDO.class);
        example.createCriteria().andEqualTo("cityCode",code);
        List<AreaDO> list = areaDAO.selectByExample(example);
        return list;
    }
    
}
