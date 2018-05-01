/**
 * Project Name:com.wstro.wsplus.core.tools
 * File Name:WebUtil.java
 * Package Name:com.wstro.wsplus.core.tools.util
 * Date:2017年4月19日下午8:07:00
 * Copyright (c) 2017, winstrong All Rights Reserved.
 *
*/

package com.bh.proprietor.core.tools.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName:WebUtil <br/>
 * Function: WEB工具类. <br/>
 * Reason: . <br/>
 * Date: 2017年4月19日 下午8:07:00 <br/>
 * 
 * @author songs
 * @version
 * @since
 * @see
 */
public class WebUtil {

	private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

	/**
	 * 
	 * isAjax:(判定是否为AJAX请求). <br/>
	 *
	 * @author songs@wstro
	 * @param request
	 * @return
	 * @since
	 */
	public static boolean isAjax(HttpServletRequest request) {
		return (request.getHeader("X-Requested-With") != null
				&& "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
	}

	/**
	 * 
	 * sendJson:(输出JSON格式内容到浏览器). <br/>
	 *
	 * @author songs
	 * @param response
	 * @param jsonStr
	 * @throws IOException
	 * @since
	 */
	public static void sendJson(HttpServletResponse response, String jsonStr) throws IOException {
		PrintWriter pw = null;
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			pw = response.getWriter();
			pw.write(jsonStr);
		} catch (Exception e) {
			logger.error("sendJson fail", e);
			throw new RuntimeException(e);
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}
	/**
	 *
	 * buildParamstr:(构建参数). <br/>
	 * 通用处理params=key/value
	 *
	 * @author jesson.xiang@blackhills.com.cn
	 * @param paramstr
	 * @return
	 * @since
	 */
	public static Map<String, Object> buildParamstr(String paramstr) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if (StringUtils.isNoneBlank(paramstr)) {
			String[] params = paramstr.split("/");
			String key = null;
			for (int i = 0; i < params.length; i++) {
				if (i % 2 == 0) {
					key = params[i];
					paramsMap.put(key, null);
				} else {
					String value = params[i];
					if (StringUtils.isNoneBlank(value)) {
						String[] values = value.split("\\|");
						if (values.length > 1) {
							List<String> valueList = new ArrayList<String>();
							for (String value_ : values) {
								valueList.add(value_);
							}
							paramsMap.put(key, valueList);
						} else {
							paramsMap.put(key, value);
						}
					}
				}
			}
		}
		return paramsMap;
	}

	/**
	 * 汉字转换成拼音 默认每个汉字拼音中以空格隔开
	 * @param china 中文字
	 * @return
	 */
	public String toPinyin(String china){
		try {
			String pinyin = PinyinHelper.convertToPinyinString(china, " ", PinyinFormat.WITHOUT_TONE);
			return pinyin;
		} catch (PinyinException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param china
	 * @param split 汉字间分隔符
	 * @return
	 */
	public String toPinyin(String china,String split){
		try {
			String pinyin = PinyinHelper.convertToPinyinString(china, split, PinyinFormat.WITHOUT_TONE);
			return pinyin;
		} catch (PinyinException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 *将中文转换成首字符小写方式
	 * @param china
	 * @param isUpperCas 是否为大写
	 * @return
	 */
	public String shortPinyin(String china,boolean isUpperCas){
		try {
			String shortPinyin = PinyinHelper.getShortPinyin(china);
			if(isUpperCas && shortPinyin != null){
				shortPinyin = shortPinyin.toUpperCase();
			}
			return shortPinyin;
		} catch (PinyinException e) {
			e.printStackTrace();
			return null;
		}
	}
}
