package com.bh.proprietor.core.tools.convert.impl;

import java.nio.charset.Charset;

import com.bh.proprietor.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.util.CharsetUtil;

/**
 * 编码对象转换器
 * 
 * @author Looly
 *
 */
public class CharsetConverter extends AbstractConverter<Charset> {

	@Override
	protected Charset convertInternal(Object value) {
		return CharsetUtil.charset(convertToStr(value));
	}

}
