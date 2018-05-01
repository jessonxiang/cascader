package com.bh.core.tools.convert.impl;

import com.bh.core.tools.convert.AbstractConverter;
import com.bh.core.tools.convert.Convert;
import com.bh.proprietor.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.convert.Convert;
import com.bh.proprietor.core.tools.util.ArrayUtil;

/**
 * int 类型数组转换器
 * 
 * @author Looly
 *
 */
public class IntArrayConverter extends AbstractConverter<int[]> {

	@Override
	protected int[] convertInternal(Object value) {
		final Integer[] result = Convert.convert(Integer[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
