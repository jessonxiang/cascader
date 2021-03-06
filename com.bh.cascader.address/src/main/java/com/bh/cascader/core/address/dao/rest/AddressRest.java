package com.bh.cascader.core.address.dao.rest;

import com.bh.cascader.core.address.dao.domain.AreaDO;
import com.bh.cascader.core.address.dao.domain.BaseDO;
import com.bh.cascader.core.address.dao.domain.CityDO;
import com.bh.cascader.core.address.dao.domain.ProvinceDO;
import com.bh.cascader.core.address.dao.server.*;
import lombok.extern.slf4j.Slf4j;
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

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "cityServiceImpl")
    private CityService cityService;

    @Resource(name = "provinceServiceImpl")
    private ProvinceService provinceService;

    @Resource(name = "streetServiceImpl")
    private StreetService streetService;

    @Resource(name = "villageServiceImpl")
    private VillageService villageService;

    /**
     * 根据code 查询对应下级城市
     *
     * @param code
     * @return
     */
    @GetMapping("/getData/{code}")
    public List getAllChildren(@PathVariable("code") String code) {
//        log.info("AddressRest > getAllChildren code={}",code);
        if (code == null || code.equals("null") || code.trim().length() == 0) {
            List<ProvinceDO> list = provinceService.findAll();
            return list;
        } else {
            BaseService baseService = getBaseService(code);
            if (baseService == null) {
                return new ArrayList();
            }
            List list = baseService.findByCode(code);
            return list;
        }
    }

    /**
     * 根据code 查询对应下级城市
     *
     * @param type 1 查询所有省份  2查询所有省份+城市  3 查询所有省份+城市+区县 4 查询所有省份+城市+区县+街道
     * @return
     */
    @GetMapping("/getAll/{type}")
    public List getAll(@PathVariable("type") String type) {
        if ("1".equals(type)) {
            List<ProvinceDO> all = provinceService.findAll();
            return all;
        }
        if ("2".equals(type)) {
            List<ProvinceDO> all = provinceService.findAll();
            for (ProvinceDO provinceDO : all) {
                String value = provinceDO.getValue();
                List byCode = cityService.findByCode(value);
                provinceDO.setCities(byCode);
            }
            return all;
        }
        if ("2".equals(type)) {
            List<ProvinceDO> all = provinceService.findAll();
            for (ProvinceDO provinceDO : all) {
                String value = provinceDO.getValue();
                List byCode = cityService.findByCode(value);
                provinceDO.setCities(byCode);
            }
            return all;
        }
        if ("3".equals(type)) {
            List<ProvinceDO> all = provinceService.findAll();
            for (ProvinceDO provinceDO : all) {
                String value = provinceDO.getValue();
                List citys = cityService.findByCode(value);
                for (Object city : citys) {
                    CityDO cityDO = (CityDO) city;
                    String cityCode = cityDO.getValue();
                    List areaList = areaService.findByCode(cityCode);
                    cityDO.setCities(areaList);
                }
                provinceDO.setCities(citys);
            }
            return all;
        }
        if ("4".equals(type)) {
            List<ProvinceDO> all = provinceService.findAll();
            for (ProvinceDO provinceDO : all) {
                String value = provinceDO.getValue();
                List citys = cityService.findByCode(value);
                for (Object city : citys) {
                    CityDO cityDO = (CityDO) city;
                    String cityCode = cityDO.getValue();
                    List areaList = areaService.findByCode(cityCode);
                    for (Object area : areaList) {
                        AreaDO areaDO = (AreaDO) area;
                        String areaCode = areaDO.getValue();
                        List streeList = streetService.findByCode(areaCode);
                        areaDO.setCities(streeList);
                    }
                    cityDO.setCities(areaList);
                }
                provinceDO.setCities(citys);
            }
            return all;
        }
        return new ArrayList();
    }


    /**
     * 根据code 查询对应下级城市，不返回下级节点
     *
     * @param code
     * @return
     */
    @GetMapping("/getDataNoNext/{code}")
    public List getAllChildrenNoNextChildren(@PathVariable("code") String code) {
//        log.info("AddressRest > getAllChildren code={}",code);
        if (code == null || code.equals("null") || code.trim().length() == 0) {
            List<ProvinceDO> list = provinceService.findAll();
            return list;
        } else {
            BaseService baseService = getBaseService(code);
            if (baseService == null) {
                return new ArrayList();
            }
            List list = baseService.findByCode(code);
            for (Object vo : list) {
                BaseDO baseDO = (BaseDO) vo;
                baseDO.setCities(null);
            }
            return list;
        }
    }

    private BaseService getBaseService(String code) {
        int length = code.length();
        BaseService baseService = null;
        switch (length) {
            case 2:
                baseService = cityService;
                break;
            case 4:
                baseService = areaService;
                break;
            case 6:
                baseService = streetService;
                break;
            case 9:
                baseService = villageService;
                break;
        }
        return baseService;
    }
}
