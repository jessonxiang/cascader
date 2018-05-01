package com.bh.core.tools.lang;

/**
 * 编辑器接口，常用于对于集合中的元素做统一编辑<br>
 * 此编辑器两个作用：<br>
 * 1、如果返回值为<code>null</code>，表示此值被抛弃
 * 2、对对象做修改
 * @author Looly
 *
 * @param <T>
 */
public interface Editor<T> {
	/**
	 * 修改过滤后的结果
	 * @param t 被过滤的对象
	 * @return 修改后的对象，如果被过滤返回<code>null</code>
	 */
	public T edit(T t);
}
