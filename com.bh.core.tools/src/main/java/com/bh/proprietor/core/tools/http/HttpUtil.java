package com.bh.proprietor.core.tools.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.bh.proprietor.core.tools.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bh.proprietor.core.tools.convert.Convert;
import com.bh.proprietor.core.tools.io.FastByteArrayOutputStream;
import com.bh.proprietor.core.tools.io.FileUtil;
import com.bh.proprietor.core.tools.io.IoUtil;
import com.bh.proprietor.core.tools.io.StreamProgress;
import com.bh.proprietor.core.tools.util.CharsetUtil;
import com.bh.proprietor.core.tools.util.CollectionUtil;
import com.bh.proprietor.core.tools.util.ReUtil;
import com.bh.proprietor.core.tools.util.StrUtil;

/**
 * Http请求工具类
 * 
 * @author xiaoleilu
 */
public class HttpUtil {

	private static Logger Log = LoggerFactory.getLogger(HttpUtil.class);

	public static final Pattern CHARSET_PATTERN = Pattern.compile("charset=(.*?)\"");

	/**
	 * 编码字符为 application/x-www-form-urlencoded
	 * 
	 * @param content
	 *            被编码内容
	 * @param charset
	 *            编码
	 * @return 编码后的字符
	 */
	public static String encode(String content, Charset charset) {
		return encode(content, charset.name());
	}

	/**
	 * 编码字符为 application/x-www-form-urlencoded
	 * 
	 * @param content
	 *            被编码内容
	 * @param charsetStr
	 *            编码
	 * @return 编码后的字符
	 */
	public static String encode(String content, String charsetStr) {
		if (StrUtil.isBlank(content))
			return content;

		String encodeContent = null;
		try {
			encodeContent = URLEncoder.encode(content, charsetStr);
		} catch (UnsupportedEncodingException e) {
			throw new HttpException(StrUtil.format("Unsupported encoding: [{}]", charsetStr), e);
		}
		return encodeContent;
	}

	/**
	 * 解码application/x-www-form-urlencoded字符
	 * 
	 * @param content
	 *            被解码内容
	 * @param charset
	 *            编码
	 * @return 编码后的字符
	 */
	public static String decode(String content, Charset charset) {
		return decode(content, charset.name());
	}

	/**
	 * 解码application/x-www-form-urlencoded字符
	 * 
	 * @param content
	 *            被解码内容
	 * @param charsetStr
	 *            编码
	 * @return 编码后的字符
	 */
	public static String decode(String content, String charsetStr) {
		if (StrUtil.isBlank(content))
			return content;
		String encodeContnt = null;
		try {
			encodeContnt = URLDecoder.decode(content, charsetStr);
		} catch (UnsupportedEncodingException e) {
			throw new HttpException(StrUtil.format("Unsupported encoding: [{}]", charsetStr), e);
		}
		return encodeContnt;
	}

	/**
	 * 获取客户端IP<br>
	 * 默认检测的Header：<br>
	 * 1、X-Forwarded-For<br>
	 * 2、X-Real-IP<br>
	 * 3、Proxy-Client-IP<br>
	 * 4、WL-Proxy-Client-IP<br>
	 * otherHeaderNames参数用于自定义检测的Header
	 * 
	 * @param request
	 *            请求对象
	 * @param otherHeaderNames
	 *            其他自定义头文件
	 * @return IP地址
	 */
	public static String getClientIP(javax.servlet.http.HttpServletRequest request, String... otherHeaderNames) {
		String[] headers = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
				"HTTP_X_FORWARDED_FOR" };
		if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
			headers = ArrayUtil.addAll(headers, otherHeaderNames);
		}

		String ip;
		for (String header : headers) {
			ip = request.getHeader(header);
			if (false == isUnknow(ip)) {
				return getMultistageReverseProxyIp(ip);
			}
		}

		ip = request.getRemoteAddr();
		return getMultistageReverseProxyIp(ip);
	}

	/**
	 * 检测是否https
	 * 
	 * @param url
	 *            URL
	 * @return 是否https
	 */
	public static boolean isHttps(String url) {
		return url.toLowerCase().startsWith("https");
	}

	/**
	 * 发送get请求
	 * 
	 * @param urlString
	 *            网址
	 * @param customCharset
	 *            自定义请求字符集，如果字符集获取不到，使用此字符集
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 * @throws IOException
	 */
	public static String get(String urlString, String customCharset) {
		return HttpRequest.get(urlString).charset(customCharset).execute().body();
	}

	/**
	 * 发送get请求
	 * 
	 * @param urlString
	 *            网址
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 * @throws IOException
	 */
	public static String get(String urlString) {
		return HttpRequest.get(urlString).execute().body();
	}

	/**
	 * 发送get请求
	 * 
	 * @param urlString
	 *            网址
	 * @param paramMap
	 *            post表单数据
	 * @return 返回数据
	 * @throws IOException
	 */
	public static String get(String urlString, Map<String, Object> paramMap) {
		return HttpRequest.get(urlString).form(paramMap).execute().body();
	}

	/**
	 * 发送post请求
	 * 
	 * @param urlString
	 *            网址
	 * @param paramMap
	 *            post表单数据
	 * @return 返回数据
	 * @throws IOException
	 */
	public static String post(String urlString, Map<String, Object> paramMap) {
		return HttpRequest.post(urlString).form(paramMap).execute().body();
	}

	/**
	 * 发送post请求
	 * 
	 * @param urlString
	 *            网址
	 * @param params
	 *            post表单数据
	 * @return 返回数据
	 * @throws IOException
	 */
	public static String post(String urlString, String params) {
		return HttpRequest.post(urlString).body(params).execute().body();
	}

	/**
	 * 发送post请求
	 *
	 * @param urlString
	 *            网址
	 * @param params
	 *            post表单数据
	 * @return 返回数据
	 * @throws IOException
	 */
	public static String postJson(String urlString, String params) {
		return HttpRequest.post(urlString).header("Content-Type","application/json;charset=UTF-8").body(params).execute().body();
	}

	// ----------------------------------------------------------------------------------------
	// download
	/**
	 * 下载远程文本
	 * 
	 * @param url
	 *            请求的url
	 * @param customCharsetName
	 *            自定义的字符集
	 * @return 文本
	 * @throws IOException
	 */
	public static String downloadString(String url, String customCharsetName) {
		return downloadString(url, CharsetUtil.charset(customCharsetName), null);
	}

	/**
	 * 下载远程文本
	 * 
	 * @param url
	 *            请求的url
	 * @param customCharset
	 *            自定义的字符集，可以使用{@link CharsetUtil#charset} 方法转换
	 * @return 文本
	 * @throws IOException
	 */
	public static String downloadString(String url, Charset customCharset) {
		return downloadString(url, customCharset, null);
	}

	/**
	 * 下载远程文本
	 * 
	 * @param url
	 *            请求的url
	 * @param customCharset
	 *            自定义的字符集，可以使用{@link CharsetUtil#charset} 方法转换
	 * @param streamPress
	 *            进度条 {@link StreamProgress}
	 * @return 文本
	 * @throws IOException
	 */
	public static String downloadString(String url, Charset customCharset, StreamProgress streamPress) {
		if (StrUtil.isBlank(url)) {
			throw new NullPointerException("[url] is null!");
		}

		FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		download(url, out, true, streamPress);
		return null == customCharset ? out.toString() : out.toString(customCharset);
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url
	 *            请求的url
	 * @param destFile
	 *            目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @return 文件大小
	 * @throws IOException
	 */
	public static long downloadFile(String url, File destFile) {
		return downloadFile(url, destFile, null);
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url
	 *            请求的url
	 * @param destFile
	 *            目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param streamProgress
	 *            进度条
	 * @return 文件大小
	 * @throws IOException
	 */
	public static long downloadFile(String url, File destFile, StreamProgress streamProgress) {
		if (StrUtil.isBlank(url)) {
			throw new NullPointerException("[url] is null!");
		}
		if (null == destFile) {
			throw new NullPointerException("[destFile] is null!");
		}
		if (destFile.isDirectory()) {
			String fileName = StrUtil.subSuf(url, url.lastIndexOf('/') + 1);
			Log.debug("FileName: {}", fileName);
			if (StrUtil.isBlank(fileName)) {
				fileName = HttpUtil.encode(url, CharsetUtil.CHARSET_UTF_8);
			}
			destFile = FileUtil.file(destFile, fileName);
		}

		OutputStream out = null;
		try {
			out = FileUtil.getOutputStream(destFile);
			return download(url, out, true, streamProgress);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url
	 *            请求的url
	 * @param out
	 *            将下载内容写到输出流中 {@link OutputStream}
	 * @param isCloseOut
	 *            是否关闭输出流
	 * @return 文件大小
	 * @throws IOException
	 */
	public static long download(String url, OutputStream out, boolean isCloseOut) {
		return download(url, out, isCloseOut, null);
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url
	 *            请求的url
	 * @param out
	 *            将下载内容写到输出流中 {@link OutputStream}
	 * @param isCloseOut
	 *            是否关闭输出流
	 * @param streamProgress
	 *            进度条
	 * @return 文件大小
	 * @throws IOException
	 */
	public static long download(String url, OutputStream out, boolean isCloseOut, StreamProgress streamProgress) {
		if (StrUtil.isBlank(url)) {
			throw new NullPointerException("[url] is null!");
		}
		if (null == out) {
			throw new NullPointerException("[out] is null!");
		}

		InputStream in = null;
		try {
			in = new URL(url).openStream();
			return IoUtil.copyByNIO(in, out, IoUtil.DEFAULT_BUFFER_SIZE, streamProgress);
		} catch (IOException e) {
			throw new HttpException(e);
		} finally {
			IoUtil.close(in);
			if (isCloseOut) {
				IoUtil.close(out);
			}
		}
	}

	/**
	 * 将Map形式的Form表单数据转换为Url参数形式，不做编码
	 * 
	 * @param paramMap
	 *            表单数据
	 * @return url参数
	 */
	public static String toParams(Map<String, Object> paramMap) {
		return toParams(paramMap, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将Map形式的Form表单数据转换为Url参数形式<br>
	 * 编码键和值对
	 * 
	 * @param paramMap
	 *            表单数据
	 * @param charsetName
	 *            编码
	 * @return url参数
	 */
	public static String toParams(Map<String, Object> paramMap, String charsetName) {
		return toParams(paramMap, CharsetUtil.charset(charsetName));
	}

	/**
	 * 将Map形式的Form表单数据转换为Url参数形式<br>
	 * 编码键和值对
	 * 
	 * @param paramMap
	 *            表单数据
	 * @param charset
	 *            编码
	 * @return url参数
	 */
	public static String toParams(Map<String, Object> paramMap, Charset charset) {
		if (CollectionUtil.isEmpty(paramMap)) {
			return StrUtil.EMPTY;
		}
		if (null == charset) {// 默认编码为系统编码
			charset = CharsetUtil.CHARSET_UTF_8;
		}

		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (Entry<String, Object> item : paramMap.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append("&");
			}
			sb.append(encode(item.getKey(), charset)).append("=")
					.append(encode(Convert.toStr(item.getValue()), charset));
		}
		return sb.toString();
	}

	/**
	 * 将URL参数解析为Map（也可以解析Post中的键值对参数）
	 * 
	 * @param paramsStr
	 *            参数字符串（或者带参数的Path）
	 * @param charset
	 *            字符集
	 * @return 参数Map
	 */
	public static Map<String, List<String>> decodeParams(String paramsStr, String charset) {
		if (StrUtil.isBlank(paramsStr)) {
			return Collections.emptyMap();
		}

		// 去掉Path部分
		int pathEndPos = paramsStr.indexOf('?');
		if (pathEndPos > 0) {
			paramsStr = StrUtil.subSuf(paramsStr, pathEndPos + 1);
		}
		paramsStr = decode(paramsStr, charset);

		final Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		String name = null;
		int pos = 0; // 未处理字符开始位置
		int i; // 未处理字符结束位置
		char c; // 当前字符
		for (i = 0; i < paramsStr.length(); i++) {
			c = paramsStr.charAt(i);
			if (c == '=' && name == null) { // 键值对的分界点
				if (pos != i) {
					name = paramsStr.substring(pos, i);
				}
				pos = i + 1;
			} else if (c == '&' || c == ';') { // 参数对的分界点
				if (name == null && pos != i) {
					// 对于像&a&这类无参数值的字符串，我们将name为a的值设为""
					addParam(params, paramsStr.substring(pos, i), StrUtil.EMPTY);
				} else if (name != null) {
					addParam(params, name, paramsStr.substring(pos, i));
					name = null;
				}
				pos = i + 1;
			}
		}

		if (pos != i) {
			if (name == null) {
				addParam(params, paramsStr.substring(pos, i), StrUtil.EMPTY);
			} else {
				addParam(params, name, paramsStr.substring(pos, i));
			}
		} else if (name != null) {
			addParam(params, name, StrUtil.EMPTY);
		}

		return params;
	}

	/**
	 * 将表单数据加到URL中（用于GET表单提交）
	 * 
	 * @param url
	 *            URL
	 * @param form
	 *            表单数据
	 * @return 合成后的URL
	 */
	public static String urlWithForm(String url, Map<String, Object> form) {
		final String queryString = toParams(form, CharsetUtil.UTF_8);
		return urlWithForm(url, queryString);
	}

	/**
	 * 将表单数据字符串加到URL中（用于GET表单提交）
	 * 
	 * @param url
	 *            URL
	 * @param queryString
	 *            表单数据字符串
	 * @return 拼接后的字符串
	 */
	public static String urlWithForm(String url, String queryString) {
		if (StrUtil.isNotBlank(queryString)) {
			if (url.contains("?")) {
				// 原URL已经带参数
				url += "&" + queryString;
			}
			url += url.endsWith("?") ? queryString : "?" + queryString;
		}

		return url;
	}

	/**
	 * 从Http连接的头信息中获得字符集<br>
	 * 从ContentType中获取
	 * 
	 * @param conn
	 *            HTTP连接对象
	 * @return 字符集
	 */
	public static String getCharset(HttpURLConnection conn) {
		if (conn == null) {
			return null;
		}

		String charset = ReUtil.get(CHARSET_PATTERN, conn.getContentType(), 1);
		return charset;
	}

	/**
	 * 从多级反向代理中获得第一个非unknown IP地址
	 * 
	 * @param ip
	 *            获得的IP地址
	 * @return 第一个非unknown IP地址
	 */
	public static String getMultistageReverseProxyIp(String ip) {
		// 多级反向代理检测
		if (ip != null && ip.indexOf(",") > 0) {
			final String[] ips = ip.trim().split(",");
			for (String subIp : ips) {
				if (false == isUnknow(subIp)) {
					ip = subIp;
					break;
				}
			}
		}
		return ip;
	}

	/**
	 * 检测给定字符串是否为未知，多用于检测HTTP请求相关<br/>
	 * 
	 * @param checkString
	 *            被检测的字符串
	 * @return 是否未知
	 */
	public static boolean isUnknow(String checkString) {
		return StrUtil.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);
	}

	/**
	 * 从流中读取内容
	 * 
	 * @param in
	 *            输入流
	 * @param charset
	 *            字符集
	 * @return 内容
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String getString(InputStream in, String charset, boolean isGetCharsetFromContent) throws IOException {
		if (StrUtil.isBlank(charset)) {
			charset = CharsetUtil.UTF_8;
		}
		if (false == isGetCharsetFromContent) {
			return IoUtil.read(in, charset);
		}

		StringBuilder content = new StringBuilder(); // 存储返回的内容

		// 从返回的内容中读取所需内容
		BufferedReader reader = IoUtil.getReader(in, charset);
		String line = null;
		while ((line = reader.readLine()) != null) {
			content.append(line).append(StrUtil.LF);
			if (isGetCharsetFromContent) {
				String charsetInContent = ReUtil.get(CHARSET_PATTERN, line, 1);
				if (StrUtil.isNotBlank(charsetInContent)) {
					Log.debug("Http content charset：{}", charsetInContent);
					charset = charsetInContent;
					reader = IoUtil.getReader(in, charset);
					isGetCharsetFromContent = false;
				}
			}
		}

		return content.toString();
	}

	/**
	 * 根据文件扩展名获得MimeType
	 * 
	 * @param filePath
	 *            文件路径或文件名
	 * @return MimeType
	 */
	public static String getMimeType(String filePath) {
		return URLConnection.getFileNameMap().getContentTypeFor(filePath);
	}
	// -----------------------------------------------------------------------------------------
	// Private method start

	/**
	 * 将键值对加入到值为List类型的Map中
	 * 
	 * @param params
	 *            参数
	 * @param name
	 *            key
	 * @param value
	 *            value
	 * @return 是否成功
	 */
	private static boolean addParam(Map<String, List<String>> params, String name, String value) {
		List<String> values = params.get(name);
		if (values == null) {
			values = new ArrayList<String>(1); // 一般是一个参数
			params.put(name, values);
		}
		values.add(value);
		return true;
	}

	// -----------------------------------------------------------------------------------------
	// Private method start end
}
