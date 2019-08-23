package com.bh.proprietor.core.tools.convert.impl;

import com.bh.proprietor.core.tools.convert.Convert;
import com.bh.proprietor.core.tools.util.ArrayUtil;
import com.bh.proprietor.core.tools.convert.AbstractConverter;

/**
 * char类型数组转换器
 *
 * @author Looly
 */
public class CharArrayConverter extends AbstractConverter<char[]> {

    @Override
    protected char[] convertInternal(Object value) {
        final Character[] result = Convert.convert(Character[].class, value);
        return ArrayUtil.unWrap(result);
    }

}
