package com.bh.cascader.core.address.dao.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianghengyang on 2018/4/4 0004.
 */
@Getter
@Setter
public class BaseDO implements Serializable{

    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name="code")
    private String value;
    @Column(name="name")
    private String label;

    @Transient
    private List<Object> cities =new ArrayList<>();

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Object> getCities() {
        return cities;
    }

    public void setCities(List<Object> cities) {
        this.cities = cities;
    }
}
