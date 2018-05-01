package com.bh.core.tools.crypto.symmetric;

import java.io.IOException;
import java.io.InputStream;
import java.security.spec.AlgorithmParameterSpec;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;

import com.bh.core.tools.crypto.CryptoException;
import com.bh.core.tools.crypto.SecureUtil;
import com.bh.proprietor.core.tools.crypto.CryptoException;
import com.bh.proprietor.core.tools.util.CharsetUtil;
import com.bh.proprietor.core.tools.util.HexUtil;
import com.bh.proprietor.core.tools.util.RandomUtil;
import com.bh.proprietor.core.tools.crypto.SecureUtil;
import com.bh.proprietor.core.tools.io.IoUtil;
import com.bh.proprietor.core.tools.util.StrUtil;

/**
 * 对称加密算法<br>
 * 
 * @author Looly
 *
 */
public class SymmetricCriptor {

	/** SecretKey 负责保存对称密钥 */
	private SecretKey secretKey;
	/** Cipher负责完成加密或解密工作 */
	private Cipher clipher;
	/** 加密解密参数 */
	private AlgorithmParameterSpec params;
	private Lock lock = new ReentrantLock();

	// ------------------------------------------------------------------
	// Constructor start
	/**
	 * 构造
	 * 
	 * @param algorithm
	 *            {@link SymmetricAlgorithm}
	 */
	public SymmetricCriptor(SymmetricAlgorithm algorithm) {
		this(algorithm, null);
	}

	/**
	 * 构造
	 * 
	 * @param algorithm
	 *            算法
	 */
	public SymmetricCriptor(String algorithm) {
		this(algorithm, null);
	}

	/**
	 * 构造
	 * 
	 * @param algorithm
	 *            {@link SymmetricAlgorithm}
	 * @param key
	 *            自定义KEY
	 */
	public SymmetricCriptor(SymmetricAlgorithm algorithm, byte[] key) {
		this(algorithm.getValue(), key);
	}

	public SymmetricCriptor(String algorithm, byte[] key) {
		init(algorithm, key);
	}
	// ------------------------------------------------------------------
	// Constructor end

	/**
	 * 初始化
	 * 
	 * @param algorithm
	 *            算法
	 * @param key
	 *            密钥，如果为<code>null</code>自动生成一个key
	 * @return {@link SymmetricCriptor}
	 */
	public SymmetricCriptor init(String algorithm, byte[] key) {
		this.secretKey = SecureUtil.generateKey(algorithm, key);
		if (algorithm.startsWith("PBE")) {
			// 对于PBE算法使用随机数加盐
			this.params = new PBEParameterSpec(RandomUtil.randomBytes(8), 100);
		}
		try {
			clipher = Cipher.getInstance(algorithm);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
		return this;
	}

	// ---------------------------------------------------------------------------------
	// Encrypt
	/**
	 * 加密
	 * 
	 * @param data
	 *            被加密的bytes
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(byte[] data) {
		lock.lock();
		try {
			if (null == this.params) {
				clipher.init(Cipher.ENCRYPT_MODE, secretKey);
			} else {
				clipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
			}
			return clipher.doFinal(data);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	public String encryptHex(byte[] data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            被加密的字符串
	 * @param charset
	 *            编码
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data, String charset) {
		return encrypt(StrUtil.bytes(data, charset));
	}

	public String encryptHex(String data, String charset) {
		return HexUtil.encodeHexStr(encrypt(data, charset));
	}

	/**
	 * 加密，使用UTF-8编码
	 * 
	 * @param data
	 *            被加密的字符串
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data) {
		return encrypt(StrUtil.bytes(data, CharsetUtil.CHARSET_UTF_8));
	}

	public String encryptHex(String data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            被加密的字符串
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(InputStream data) {
		try {
			return encrypt(IoUtil.readBytes(data));
		} catch (IOException e) {
			throw new CryptoException(e);
		}
	}

	public String encryptHex(InputStream data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	// ---------------------------------------------------------------------------------
	// Decrypt
	/**
	 * 解密
	 * 
	 * @param bytes
	 *            被解密的bytes
	 * @return 解密后的bytes
	 */
	public byte[] decrypt(byte[] bytes) {
		lock.lock();
		try {
			if (null == this.params) {
				clipher.init(Cipher.DECRYPT_MODE, secretKey);
			} else {
				clipher.init(Cipher.DECRYPT_MODE, secretKey, params);
			}
			return clipher.doFinal(bytes);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}
	
	public String decryptHex(String data) {
		byte[] decodeDatas = HexUtil.decodeHex(data.toCharArray());
		return new String(decrypt(decodeDatas));
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            被解密的bytes
	 * @return 解密后的bytes
	 */
	public byte[] decrypt(InputStream data) {
		try {
			return decrypt(IoUtil.readBytes(data));
		} catch (IOException e) {
			throw new CryptoException(e);
		}
	}

	// ---------------------------------------------------------------------------------
	// Getters
	/**
	 * 获得对称密钥
	 * 
	 * @return 获得对称密钥
	 */
	public SecretKey getSecretKey() {
		return secretKey;
	}

	/**
	 * 获得加密或解密器
	 * 
	 * @return 加密或解密
	 */
	public Cipher getClipher() {
		return clipher;
	}
}
