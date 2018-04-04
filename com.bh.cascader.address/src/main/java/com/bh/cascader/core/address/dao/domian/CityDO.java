package com.bh.cascader.core.address.dao.domian;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by xianghengyang on 2018/4/4 0004.
 */
@Table(name="t_bh_city")
@Getter
@Setter
public class CityDO extends BaseDO{

    @Column(name="provinceCode")
    private String provinceCode;
}
