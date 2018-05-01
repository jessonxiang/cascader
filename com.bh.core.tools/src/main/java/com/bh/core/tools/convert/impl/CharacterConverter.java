package com.bh.core.tools.convert.impl;

import com.bh.core.tools.convert.AbstractConverter;
import com.bh.proprietor.core.tools.util.StrUtil;
import com.bh.proprietor.core.tools.convert.AbstractConverter;

/**
 * 字符转换器
 * 
 * @author Looly
 *
 */
public class CharacterConverter extends AbstractConverter<Character> {

	@Override
	protected Character convertInternal(Object value) {
		if (char.class == value.getClass()) {
			return Character.valueOf((char) value);
		} else {
			final String valueStr = convertToStr(value);
			if (StrUtil.isNotBlank(valueStr)) {
				try {
					return Character.valueOf(valueStr.charAt(0));
				} catch (Exception e) {
					// Ignore Exception
				}
			}
		}
		return null;
	}

}
