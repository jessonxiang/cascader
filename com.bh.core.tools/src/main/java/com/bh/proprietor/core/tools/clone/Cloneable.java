package com.bh.proprietor.core.tools.clone;

/**
 * 克隆支持接口
 *
 * @param <T>
 * @author Looly
 */
public interface Cloneable<T> extends java.lang.Cloneable {
    T clone();
}
