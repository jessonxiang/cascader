package com.bh.cascader.core.address.dao.rest;

import com.bh.cascader.core.address.dao.domian.ProvinceDO;
import com.bh.cascader.core.address.dao.server.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianghengyang on 2018/4/4 0004.
 */

@RestController
@RequestMapping("/ws/cascader/address")
@Slf4j
public class AddressRest {

    @Resource(name="areaServiceImpl")
    private AreaService areaService;

    @Resource(name="cityServiceImpl")
    private CityService cityService;

    @Resource(name="provinceServiceImpl")
    private ProvinceService provinceService;

    @Resource(name="streetServiceImpl")
    private StreetService streetService;

    @Resource(name="villageServiceImpl")
    private VillageService villageService;

    @GetMapping("/getAllChildren/{code}")
    public List getAllChildren(@PathVariable("code") String code){
//        log.info("AddressRest > getAllChildren code={}",code);
        if(code == null || code.equals("null") || code.trim().length() == 0){
            List<ProvinceDO> list = provinceService.findAll();
            return list;
        }else{
            BaseService baseService = getBaseService(code);
            if(baseService == null){
                return new ArrayList();
            }
            List list = baseService.findByCode(code);
            return list;
        }
    }

    private BaseService getBaseService(String code){
        int length = code.length();
        BaseService baseService = null;
        switch (length){
            case 2:
                baseService = cityService;break;
            case 4:
                baseService = areaService;break;
            case 6:
                baseService = streetService;break;
            case 9:
                baseService = villageService;break;
        }
        return  baseService;
    }
}
