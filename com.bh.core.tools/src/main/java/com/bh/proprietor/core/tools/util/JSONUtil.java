/**
 * Project Name:com.wstro.srkc.core.tools
 * File Name:JSONUtils.java
 * Package Name:com.wstro.srkc.core.tools.util
 * Date:2017年2月18日上午1:49:23
 * Copyright (c) 2017, winstrong All Rights Reserved.
 *
*/

package com.bh.proprietor.core.tools.util;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * ClassName:JSONUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年2月18日 上午1:49:23 <br/>
 * 
 * @author songs
 * @version
 * @since
 * @see
 */
public class JSONUtil {

	public static String toJsonStr(Object object) {
		return JSON.toJSONString(object);
	}

	public static <T> T parse(String jsonStr,Class<T> clazz){
		try {
			T result = JSON.parseObject(jsonStr, clazz);
			return result;
		} catch (Exception e) {
			throw new RuntimeException("F_00010002	json string parse to Object fail.");
		}
	}

	public static <T> List<T> parseArr(String jsonStr,Class<T> clazz){
		try {
			List<T> result = JSON.parseArray(jsonStr, clazz);
			return result;
		} catch (Exception e) {
			throw new RuntimeException("F_00010002	json string parse to Object fail.");
		}
	}
}
