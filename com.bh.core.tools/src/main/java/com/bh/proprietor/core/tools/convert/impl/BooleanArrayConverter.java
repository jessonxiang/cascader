package com.bh.proprietor.core.tools.convert.impl;

import com.bh.proprietor.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.convert.Convert;
import com.bh.proprietor.core.tools.util.ArrayUtil;

/**
 * boolean类型数组转换器
 * 
 * @author Looly
 *
 */
public class BooleanArrayConverter extends AbstractConverter<boolean[]> {

	@Override
	protected boolean[] convertInternal(Object value) {
		final Boolean[] result = Convert.convert(Boolean[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
