package com.bh.core.tools.convert.impl;

import com.bh.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.convert.AbstractConverter;

/**
 * 字符串转换器
 * 
 * @author Looly
 *
 */
public class StringConverter extends AbstractConverter<String> {

	@Override
	protected String convertInternal(Object value) {
		return convertToStr(value);
	}

}
