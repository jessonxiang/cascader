package com.bh.core.tools.io.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.bh.core.tools.io.FileUtil;
import com.bh.core.tools.io.IORuntimeException;
import com.bh.core.tools.lang.Assert;
import com.bh.core.tools.util.ClassUtil;
import com.bh.proprietor.core.tools.lang.Assert;
import com.bh.proprietor.core.tools.util.ClassUtil;
import com.bh.proprietor.core.tools.io.FileUtil;
import com.bh.proprietor.core.tools.io.IORuntimeException;

/**
 * ClassPath资源访问类
 * 
 * @author Looly
 *
 */
public class ClassPathResource {
	private String path;
	private ClassLoader classLoader;
	private Class<?> clazz;
	private URL url;

	// --------------------------------------------------------------------------------------
	// Constructor start
	/**
	 * 构造
	 * 
	 * @param path
	 *            相对于ClassPath的路径
	 */
	public ClassPathResource(String path) {
		this(path, null, null);
	}

	/**
	 * 构造
	 * 
	 * @param path
	 *            相对于ClassPath的路径
	 * @param classLoader
	 *            {@link ClassLoader}
	 */
	public ClassPathResource(String path, ClassLoader classLoader) {
		this(path, classLoader, null);
	}

	/**
	 * 构造
	 * 
	 * @param path
	 *            相对于给定Class的路径
	 * @param clazz
	 *            {@link Class} 用于定位路径
	 */
	public ClassPathResource(String path, Class<?> clazz) {
		this(path, null, clazz);
	}

	/**
	 * 构造
	 * 
	 * @param path
	 *            相对路劲
	 * @param classLoader
	 *            {@link ClassLoader}
	 * @param clazz
	 *            {@link Class} 用于定位路径
	 */
	public ClassPathResource(String path, ClassLoader classLoader, Class<?> clazz) {
		Assert.notNull(path, "Path must not be null");
		this.path = path;
		this.classLoader = (classLoader != null ? classLoader : ClassUtil.getClassLoader());
		this.clazz = clazz;
		initUrl();
	}
	// --------------------------------------------------------------------------------------
	// Constructor end

	/**
	 * 获得Path
	 * 
	 * @return path
	 */
	public final String getPath() {
		return this.path;
	}

	/**
	 * 获得 {@link ClassLoader}
	 * 
	 * @return {@link ClassLoader}
	 */
	public final ClassLoader getClassLoader() {
		return this.classLoader;
	}

	/**
	 * 获得解析后的{@link URL}
	 */
	public final URL getUrl() {
		return this.url;
	}

	/**
	 * 获得 {@link InputStream}
	 * 
	 * @return {@link InputStream}
	 */
	public InputStream getStream() {
		if (null == this.url) {
			throw new IORuntimeException("Resource [{}] not exist!", this.path);
		}
		try {
			return this.url.openStream();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得File
	 * 
	 * @return {@link File}
	 */
	public File getFile() {
		return FileUtil.file(this.url);
	}

	@Override
	public String toString() {
		return (null == this.url) ? "null" : this.url.toString();
	}

	/**
	 * 根据给定资源初始化URL
	 */
	private void initUrl() {
		if (null != this.clazz) {
			this.url = this.clazz.getResource(this.path);
		} else if (null != this.classLoader) {
			this.url = this.classLoader.getResource(this.path);
		} else {
			this.url = ClassLoader.getSystemResource(this.path);
		}
	}
}
