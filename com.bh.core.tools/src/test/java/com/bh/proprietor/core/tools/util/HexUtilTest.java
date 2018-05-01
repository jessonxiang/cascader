/**
 * Project Name:com.wstro.srkc.core.tools
 * File Name:HexUtilTest.java
 * Package Name:com.wstro.srkc.core.tools.util
 * Date:2017年2月18日下午8:17:08
 * Copyright (c) 2017, winstrong All Rights Reserved.
 *
*/

package com.bh.proprietor.core.tools.util;

import static org.junit.Assert.*;

import java.nio.charset.Charset;

import org.junit.Test;

/**
 * ClassName:HexUtilTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017年2月18日 下午8:17:08 <br/>
 * @author   songs
 * @version  
 * @since    
 * @see 	 
 */
public class HexUtilTest {

	@Test
	public void testIsHexNumber() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeHexByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeHexStringCharset() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeHexByteArrayBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeHexStrByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeHexStrStringCharset() {
		String encodestr = HexUtil.encodeHexStr("123".getBytes());
		System.out.println(encodestr);
		String decodestr = HexUtil.decodeHexStr(encodestr, Charset.defaultCharset());
		System.out.println(decodestr);
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeHexStrByteArrayBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecodeHexStrStringCharset() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecodeHexStrCharArrayCharset() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecodeHex() {
		fail("Not yet implemented");
	}

}

