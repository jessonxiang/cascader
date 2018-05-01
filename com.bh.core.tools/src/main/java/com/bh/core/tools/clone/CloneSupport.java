package com.bh.core.tools.clone;

import java.lang.*;
import java.lang.Cloneable;

/**
 * 克隆支持类，提供默认的克隆方法
 * @author Looly
 *
 * @param <T>
 */
public class CloneSupport<T> implements Cloneable<T> {
	
	@SuppressWarnings("unchecked")
	@Override
	public T clone() {
		try {
			return (T) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
