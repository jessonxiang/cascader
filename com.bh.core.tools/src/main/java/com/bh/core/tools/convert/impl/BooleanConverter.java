package com.bh.core.tools.convert.impl;

import com.bh.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.convert.AbstractConverter;

/**
 * 波尔转换器
 * 
 * @author Looly
 *
 */
public class BooleanConverter extends AbstractConverter<Boolean> {

	@Override
	protected Boolean convertInternal(Object value) {
		if (boolean.class == value.getClass()) {
			return Boolean.valueOf((boolean) value);
		}
		String valueStr = convertToStr(value);
		return Boolean.valueOf(PrimitiveConverter.parseBoolean(valueStr));
	}

}
