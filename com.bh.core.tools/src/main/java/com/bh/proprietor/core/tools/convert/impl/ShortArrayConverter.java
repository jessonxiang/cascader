package com.bh.proprietor.core.tools.convert.impl;

import com.bh.proprietor.core.tools.util.ArrayUtil;
import com.bh.proprietor.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.convert.Convert;

/**
 * short 类型数组转换器
 * 
 * @author Looly
 *
 */
public class ShortArrayConverter extends AbstractConverter<short[]> {

	@Override
	protected short[] convertInternal(Object value) {
		final Short[] result = Convert.convert(Short[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
