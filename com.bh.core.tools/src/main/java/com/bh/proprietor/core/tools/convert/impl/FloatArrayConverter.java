package com.bh.proprietor.core.tools.convert.impl;

import com.bh.proprietor.core.tools.util.ArrayUtil;
import com.bh.proprietor.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.convert.Convert;

/**
 * float 类型数组转换器
 *
 * @author Looly
 */
public class FloatArrayConverter extends AbstractConverter<float[]> {

    @Override
    protected float[] convertInternal(Object value) {
        final Float[] result = Convert.convert(Float[].class, value);
        return ArrayUtil.unWrap(result);
    }

}
