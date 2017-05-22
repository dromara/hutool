package com.xiaoleilu.hutool.crypto.symmetric;

import com.xiaoleilu.hutool.crypto.CryptoException;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.HexUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.spec.AlgorithmParameterSpec;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对称加密算法<br>
 * @author Looly
 *
 */
public class SymmetricCrypto {

	/** SecretKey 负责保存对称密钥 */
	private SecretKey secretKey;
	/** Cipher负责完成加密或解密工作 */
	private Cipher clipher;
	/** 加密解密参数 */
	private AlgorithmParameterSpec params;
	private Lock lock = new ReentrantLock();

	//------------------------------------------------------------------ Constructor start
	/**
	 * 构造，使用随机密钥
	 * @param algorithm {@link SymmetricAlgorithm}
	 */
	public SymmetricCrypto(SymmetricAlgorithm algorithm) {
		this(algorithm, null);
	}
	
	/**
	 * 构造，使用随机密钥
	 * @param algorithm 算法
	 */
	public SymmetricCrypto(String algorithm) {
		this(algorithm, null);
	}
	
	/**
	 * 构造
	 * @param algorithm 算法 {@link SymmetricAlgorithm}
	 * @param key 自定义KEY
	 */
	public SymmetricCrypto(SymmetricAlgorithm algorithm, byte[] key) {
		this(algorithm.getValue(), key);
	}
	
	/**
	 * 构造
	 * @param algorithm 算法
	 * @param key 密钥
	 */
	public SymmetricCrypto(String algorithm, byte[] key) {
		init(algorithm, key);
	}
	//------------------------------------------------------------------ Constructor end
	
	/**
	 * 初始化
	 * @param algorithm 算法
	 * @param key 密钥，如果为<code>null</code>自动生成一个key
	 * @return {@link SymmetricCrypto}
	 */
	public SymmetricCrypto init(String algorithm, byte[] key) {
		return init(algorithm, SecureUtil.generateKey(algorithm, key));
	}
	
	/**
	 * 初始化
	 * @param algorithm 算法
	 * @param key 密钥，如果为<code>null</code>自动生成一个key
	 * @return {@link SymmetricCrypto}
	 */
	public SymmetricCrypto init(String algorithm, SecretKey key) {
		this.secretKey = key;
		if(algorithm.startsWith("PBE")){
			//对于PBE算法使用随机数加盐
			this.params = new PBEParameterSpec(RandomUtil.randomBytes(8), 100);
		}
		try {
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
		lock.lock();
		try {
			if(null == this.params){
				clipher.init(Cipher.ENCRYPT_MODE, secretKey);
			}else{
				clipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
			}
			return clipher.doFinal(data);
		} catch (Exception e) {
			throw new CryptoException(e);
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * 加密
	 * 
	 * @param data 数据
	 * @return 加密后的Hex
	 */
	public String encryptHex(byte[] data){
		return HexUtil.encodeHexStr(encrypt(data));
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
	 * 加密
	 * @param data 被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Hex
	 */
	public String encryptHex(String data, String charset){
		return HexUtil.encodeHexStr(encrypt(data, charset));
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
	 * @return 加密后的Hex
	 */
	public String encryptHex(String data){
		return HexUtil.encodeHexStr(encrypt(data));
	}
	
	/**
	 * 加密
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
	
	/**
	 * 加密
	 * @param data 被加密的字符串
	 * @return 加密后的Hex
	 */
	public String encryptHex(InputStream data){
		return HexUtil.encodeHexStr(encrypt(data));
	}
	//--------------------------------------------------------------------------------- Decrypt
	/**
	 * 解密
	 * @param bytes 被解密的bytes
	 * @return 解密后的bytes
	 */
	public byte[] decrypt(byte[] bytes){
		lock.lock();
		try {
			if(null == this.params){
				clipher.init(Cipher.DECRYPT_MODE, secretKey);
			}else{
				clipher.init(Cipher.DECRYPT_MODE, secretKey, params);
			}
			return clipher.doFinal(bytes);
		} catch (Exception e) {
			throw new CryptoException(e);
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * 解密
	 * @param bytes 被解密的bytes
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	public String decryptStr(byte[] bytes, Charset charset){
		return StrUtil.str(decrypt(bytes), charset);
	}
	
	/**
	 * 解密
	 * @param bytes 被解密的bytes
	 * @return 解密后的String
	 */
	public String decryptStr(byte[] bytes){
		return decryptStr(bytes, CharsetUtil.CHARSET_UTF_8);
	}
	
	/**
	 * 解密
	 * @param data 被解密的String
	 * @return 解密后的bytes
	 */
	public byte[] decrypt(String data) {
		return decrypt(HexUtil.decodeHex(data));
	}
	
	/**
	 * 解密
	 * @param data 被解密的String
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	public String decryptStr(String data, Charset charset){
		return StrUtil.str(decrypt(data), charset);
	}
	
	/**
	 * 解密
	 * @param data 被解密的String
	 * @return 解密后的String
	 */
	public String decryptStr(String data){
		return decryptStr(data, CharsetUtil.CHARSET_UTF_8);
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
	
	/**
	 * 解密
	 * @param data 被解密的InputStream
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	public String decryptStr(InputStream data, Charset charset){
		return StrUtil.str(decrypt(data), charset);
	}
	
	/**
	 * 解密
	 * @param data 被解密的InputStream
	 * @return 解密后的String
	 */
	public String decryptStr(InputStream data){
		return decryptStr(data, CharsetUtil.CHARSET_UTF_8);
	}
	
	//--------------------------------------------------------------------------------- Getters
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
}
