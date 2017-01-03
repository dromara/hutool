package com.xiaoleilu.hutool.crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.xiaoleilu.hutool.crypto.digest.DigestAlgorithm;
import com.xiaoleilu.hutool.crypto.digest.Digester;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricCriptor;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 安全相关工具类<br>
 * 加密分为三种：<br>
 * 1、对称加密（symmetric），例如：AES、DES等<br>
 * 2、非对称加密（asymmetric），例如：RSA、DSA等<br>
 * 3、摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等<br>
 * 
 * @author xiaoleilu
 *
 */
public class SecureUtil {
	
	/**
	 * 生成 {@link SecretKey}
	 * @param algorithm 算法，支持PBE算法
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(String algorithm){
		SecretKey secretKey;
		try {
			secretKey = KeyGenerator.getInstance(algorithm).generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
		return secretKey;
	}
	
	/**
	 * 生成 {@link SecretKey}
	 * @param algorithm 算法
	 * @param key 密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(String algorithm, byte[] key){
		Assert.notBlank(algorithm, "Algorithm is blank!");
		SecretKey secretKey = null;
		if(algorithm.startsWith("PBE")){
			//PBE密钥
			secretKey = generatePBEKey(algorithm, (null == key) ? null : StrUtil.str(key, CharsetUtil.CHARSET_UTF_8).toCharArray());
		}else if(algorithm.startsWith("DES")){
			//DES密钥
			secretKey = generateDESKey(algorithm, key);
		}else{
			//其它算法密钥
			secretKey = (null == key) ? generateKey(algorithm) : new SecretKeySpec(key, algorithm);
		}
		return secretKey;
	}
	
	/**
	 * 生成 {@link SecretKey}
	 * @param algorithm PBE算法
	 * @param key 密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateDESKey(String algorithm, byte[] key){
		if(StrUtil.isBlank(algorithm) || false == algorithm.startsWith("DES")){
			throw new CryptoException("Algorithm [{}] is not a DES algorithm!");
		}
		
		SecretKey secretKey = null;
		if(null == key){
			secretKey = generateKey(algorithm);
		}else{
			DESKeySpec keySpec;
			try {
				keySpec = new DESKeySpec(key);
			} catch (InvalidKeyException e) {
				throw new CryptoException(e);
			}
			secretKey = generateKey(algorithm, keySpec);
		}
		return secretKey;
	}
	
	/**
	 * 生成PBE {@link SecretKey}
	 * @param algorithm PBE算法
	 * @param key 密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generatePBEKey(String algorithm, char[] key){
		if(StrUtil.isBlank(algorithm) || false == algorithm.startsWith("PBE")){
			throw new CryptoException("Algorithm [{}] is not a PBE algorithm!");
		}
		
		if(null == key){
			key = RandomUtil.randomString(32).toCharArray();
		}
		PBEKeySpec keySpec = new PBEKeySpec(key);
		return generateKey(algorithm, keySpec);
	}
	
	/**
	 * 生成 {@link SecretKey}
	 * @param algorithm 算法
	 * @param keySpec {@link KeySpec}
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(String algorithm, KeySpec keySpec){
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
			return keyFactory.generateSecret(keySpec);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}
	
	/**
	 * 生成私钥
	 * @param algorithm 算法
	 * @param key 密钥
	 * @return 私钥 {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(String algorithm, byte[] key){
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		try {
			return KeyFactory.getInstance(algorithm).generatePrivate(pkcs8KeySpec);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}
	
	/**
	 * 生成公钥
	 * @param algorithm 算法
	 * @param key 密钥
	 * @return 公钥 {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(String algorithm, byte[] key){
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		try {
			return KeyFactory.getInstance(algorithm).generatePublic(x509KeySpec);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}
	
	/**
	 * 生成用于非对称加密的公钥和私钥
	 * @param algorithm 非对称加密算法
	 * @return {@link KeyPair}
	 */
	public static KeyPair generateKeyPair(String algorithm){
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
		keyPairGen.initialize(1024);
		return keyPairGen.generateKeyPair();
	}
	
	// ------------------------------------------------------------------- 对称加密算法

	/**
	 * AES加密
	 * 
	 * @return {@link SymmetricCriptor}
	 */
	public static SymmetricCriptor aes() {
		return new SymmetricCriptor(SymmetricAlgorithm.AES);
	}

	/**
	 * AES加密
	 * 
	 * @param key 密钥
	 * @return {@link SymmetricCriptor}
	 */
	public static SymmetricCriptor aes(byte[] key) {
		return new SymmetricCriptor(SymmetricAlgorithm.AES, key);
	}

	// ------------------------------------------------------------------- 摘要算法
	/**
	 * MD5加密
	 * 
	 * @return {@link Digester}
	 */
	public static Digester md5() {
		return new Digester(DigestAlgorithm.MD5);
	}

	/**
	 * SHA1加密
	 * 
	 * @return {@link Digester}
	 */
	public static Digester sha1() {
		return new Digester(DigestAlgorithm.SHA1);
	}

	// ------------------------------------------------------------------- UUID
	/**
	 * @return 简化的UUID，去掉了横线
	 */
	public static String simpleUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
