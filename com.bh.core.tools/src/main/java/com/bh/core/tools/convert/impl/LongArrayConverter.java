package com.bh.core.tools.convert.impl;

import com.bh.core.tools.convert.AbstractConverter;
import com.bh.core.tools.convert.Convert;
import com.bh.proprietor.core.tools.convert.Convert;
import com.bh.proprietor.core.tools.util.ArrayUtil;
import com.bh.proprietor.core.tools.convert.AbstractConverter;

/**
 * long 类型数组转换器
 * 
 * @author Looly
 *
 */
public class LongArrayConverter extends AbstractConverter<long[]> {

	@Override
	protected long[] convertInternal(Object value) {
		final Long[] result = Convert.convert(Long[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
