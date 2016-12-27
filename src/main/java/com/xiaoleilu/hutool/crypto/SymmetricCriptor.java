package com.xiaoleilu.hutool.crypto;

import java.io.IOException;
import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 对称加密算法<br>
 * 注意：此对象实例化后为非线程安全！
 * 
 * @author Looly
 *
 */
public class SymmetricCriptor {

	/** KeyGenerator 提供对称密钥生成器的功能，支持各种算法 */
	private KeyGenerator keygen;
	/** SecretKey 负责保存对称密钥 */
	private SecretKey secretKey;
	/** Cipher负责完成加密或解密工作 */
	private Cipher clipher;

	//------------------------------------------------------------------ Constructor start
	public SymmetricCriptor(SymmetricAlgorithm algorithm) {
		init(algorithm.value);
	}
	
	public SymmetricCriptor(String algorithm) {
		init(algorithm);
	}
	public SymmetricCriptor(SymmetricAlgorithm algorithm, byte[] key) {
		init(algorithm.value, key);
	}
	
	public SymmetricCriptor(String algorithm, byte[] key) {
		init(algorithm, key);
	}
	//------------------------------------------------------------------ Constructor end
	
	/**
	 * 初始化
	 * @param algorithm 算法
	 * @return {@link SymmetricCriptor}
	 */
	public SymmetricCriptor init(String algorithm) {
		return init(algorithm, null);
	}

	/**
	 * 初始化
	 * @param algorithm 算法
	 * @param key 密钥，如果为<code>null</code>自动生成一个key
	 * @return {@link SymmetricCriptor}
	 */
	public SymmetricCriptor init(String algorithm, byte[] key) {
		try {
			keygen = KeyGenerator.getInstance(algorithm);
			if(null != key){
				secretKey = new SecretKeySpec(key, algorithm);
			}else{
				secretKey = keygen.generateKey();
			}
			clipher = Cipher.getInstance(algorithm);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
		return this;
	}
	
	//--------------------------------------------------------------------------------- Encrypt
	/**
	 * 加密
	 * @param data 被加密的bytes
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(byte[] data){
		try {
			clipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return clipher.doFinal(data);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}
	
	/**
	 * 加密
	 * @param data 被加密的字符串
	 * @param charset 编码
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data, String charset){
		return encrypt(StrUtil.bytes(data, charset));
	}
	
	/**
	 * 加密，使用UTF-8编码
	 * @param data 被加密的字符串
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data){
		return encrypt(StrUtil.bytes(data, CharsetUtil.CHARSET_UTF_8));
	}
	
	/**
	 * 加密，使用UTF-8编码
	 * @param data 被加密的字符串
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(InputStream data){
		try {
			return encrypt(IoUtil.readBytes(data));
		} catch (IOException e) {
			throw new CryptoException(e);
		}
	}
	
	//--------------------------------------------------------------------------------- Decrypt
	/**
	 * 解密
	 * @param bytes 被解密的bytes
	 * @return 解密后的bytes
	 */
	public byte[] decrypt(byte[] bytes){
		try {
			clipher.init(Cipher.DECRYPT_MODE, secretKey);
			return clipher.doFinal(bytes);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}
	
	/**
	 * 解密
	 * @param data 被解密的bytes
	 * @return 解密后的bytes
	 */
	public byte[] decrypt(InputStream data){
		try {
			return decrypt(IoUtil.readBytes(data));
		} catch (IOException e) {
			throw new CryptoException(e);
		}
	}
	
	//--------------------------------------------------------------------------------- Getters
	/**
	 * 获得 对称密钥生成器的功能，支持各种算法
	 * @return 对称密钥生成器的功能，支持各种算法
	 */
	public KeyGenerator getKeygen() {
		return keygen;
	}

	/**
	 * 获得对称密钥
	 * @return 获得对称密钥
	 */
	public SecretKey getSecretKey() {
		return secretKey;
	}

	/**
	 * 获得加密或解密器
	 * @return 加密或解密
	 */
	public Cipher getClipher() {
		return clipher;
	}

	/**
	 * 对称算法类型
	 * 
	 * @author Looly
	 *
	 */
	public static enum SymmetricAlgorithm {
		AES("AES"), 
		ARCFOUR("ARCFOUR"), 
		Blowfish("Blowfish"), 
		DES("DES"), 
		DESede("DESede"), 
		HmacMD5("HmacMD5"), 
		HmacSHA1("HmacSHA1"), 
		HmacSHA256("HmacSHA256");

		private String value;

		private SymmetricAlgorithm(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
}
