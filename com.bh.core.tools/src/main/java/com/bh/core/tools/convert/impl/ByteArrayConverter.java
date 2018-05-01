package com.bh.core.tools.convert.impl;

import com.bh.core.tools.convert.AbstractConverter;
import com.bh.core.tools.convert.Convert;
import com.bh.proprietor.core.tools.convert.Convert;
import com.bh.proprietor.core.tools.util.ArrayUtil;
import com.bh.proprietor.core.tools.convert.AbstractConverter;

/**
 * byte 类型数组转换器
 * 
 * @author Looly
 *
 */
public class ByteArrayConverter extends AbstractConverter<byte[]> {

	@Override
	protected byte[] convertInternal(Object value) {
		final Byte[] result = Convert.convert(Byte[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
