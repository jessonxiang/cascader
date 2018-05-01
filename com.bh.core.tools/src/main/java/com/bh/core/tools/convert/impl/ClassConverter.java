package com.bh.core.tools.convert.impl;

import com.bh.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.util.ClassUtil;

/**
 * 类转换器<br>
 * 将类名转换为类
 * 
 * @author Looly
 *
 */
public class ClassConverter extends AbstractConverter<Class<?>> {

	@Override
	protected Class<?> convertInternal(Object value) {
		String valueStr = convertToStr(value);
		try {
			return ClassUtil.getClassLoader().loadClass(valueStr);
		} catch (Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
