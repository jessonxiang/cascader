package com.bh.proprietor.core.tools.convert.impl;

import com.bh.proprietor.core.tools.util.ArrayUtil;
import com.bh.proprietor.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.convert.Convert;

/**
 * double 类型数组转换器
 * 
 * @author Looly
 *
 */
public class DoubleArrayConverter extends AbstractConverter<double[]> {

	@Override
	protected double[] convertInternal(Object value) {
		final Double[] result = Convert.convert(Double[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
