/**
 * Project Name:com.wstro.srkc.core.tools
 * File Name:SecureUtilTest.java
 * Package Name:com.wstro.srkc.core.tools.crypto
 * Date:2017年2月18日下午12:03:42
 * Copyright (c) 2017, winstrong All Rights Reserved.
 *
*/

package com.bh.proprietor.core.tools.crypto;

import static org.junit.Assert.fail;

import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName:SecureUtilTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年2月18日 下午12:03:42 <br/>
 * 
 * @author songs
 * @version
 * @since
 * @see
 */
public class SecureUtilTest {

	private static Logger Log = LoggerFactory.getLogger(SecureUtilTest.class);

	private static final String inputStr = "123456";

	private static final String key = "AABBCCDDEEFF11221";


	@Test
	public void druidencryptionTest(){
		try{
			String enpd = ConfigTools.encrypt("root");
			System.out.println(enpd);
		}catch(Exception ex){

		}

	}


	@Test
	public void testGenerateKeyString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateKeyStringByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateDESKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testGeneratePBEKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateKeyStringKeySpec() {
		fail("Not yet implemented");
	}

	@Test
	public void testGeneratePrivateKeyStringByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGeneratePrivateKeyKeyStoreStringCharArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGeneratePublicKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateKeyPairString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateKeyPairStringInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateKeyPairStringIntByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateSignature() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadJKSKeyStore() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadKeyStore() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadX509Certificate() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadCertificate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCertificate() {
		fail("Not yet implemented");
	}

	@Test
	public void testAes() {
		String key = "ys3R8pkG+MvU+ioj3YSAKQ==";
		String enstr = SecureUtil.aes(key).encryptHex(inputStr);
		String dnstr = SecureUtil.aes(key).decryptHex(enstr);
		Log.info(enstr + "->" + dnstr);
		Assert.assertEquals(dnstr, inputStr);
	}

	@Test
	public void testAesByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testDes() {
		String endata = SecureUtil.des(key).encryptHex(inputStr);
		String dndata = SecureUtil.des(key).decryptHex(endata);
		Log.info(endata + "->" + dndata);
		Assert.assertEquals(dndata, inputStr);
	}

	public String parseByteArray2HexStr(byte[] data) {
		if (data == null || data.length < 1) {
			return null;
		}

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int h = data[i] & 0XFF;
			if (h < 16) {
				hex.append("0");
			}
			hex.append(Integer.toHexString(h));
		}

		return hex.toString();
	}

	@Test
	public void testDesByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMd5() {
		Log.info(SecureUtil.md5("123456"));
		Assert.assertEquals(SecureUtil.md5("123456"), "e10adc3949ba59abbe56e057f20f883e");
	}

	@Test
	public void testMd5String() {
		fail("Not yet implemented");
	}

	@Test
	public void testMd5InputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testMd5File() {
		fail("Not yet implemented");
	}

	@Test
	public void testSha1() {
		fail("Not yet implemented");
	}

	@Test
	public void testSha1String() {
		Log.info(SecureUtil.sha1("123456"));
		Assert.assertEquals(SecureUtil.sha1("123456"), "7c4a8d09ca3762af61e59520943dc26494f8941b");
	}

	@Test
	public void testSha1InputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testSha1File() {
		fail("Not yet implemented");
	}

	@Test
	public void testHmacMd5ByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testHmacMd5() {
		fail("Not yet implemented");
	}

	@Test
	public void testHmacSha1ByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testHmacSha1() {
		fail("Not yet implemented");
	}

	@Test
	public void testSimpleUUID() {
		Log.info(SecureUtil.simpleUUID());
		fail("Not yet implemented");
	}

}
