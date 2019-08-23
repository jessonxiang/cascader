package com.bh.cascader.core.address.dao.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by xianghengyang on 2018/4/4 0004.
 */
@Table(name = "t_bh_street")
@Getter
@Setter
public class StreetDO extends BaseDO {

    @Column(name = "cityCode")
    private String cityCode;

    @Column(name = "provinceCode")
    private String provinceCode;

    @Column(name = "areaCode")
    private String areaCode;

}
