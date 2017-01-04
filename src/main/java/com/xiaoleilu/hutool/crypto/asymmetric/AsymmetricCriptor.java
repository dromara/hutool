package com.xiaoleilu.hutool.crypto.asymmetric;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.Cipher;

import com.xiaoleilu.hutool.crypto.CryptoException;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 非对称加密算法<br>
 * 1、签名：使用私钥加密，公钥解密。用于让所有公钥所有者验证私钥所有者的身份并且用来防止私钥所有者发布的内容被篡改，但是不用来保证内容不被他人获得。<br>
 * 2、加密：用公钥加密，私钥解密。用于向公钥所有者发布信息,这个信息可能被他人篡改,但是无法被他人获得。
 * @author Looly
 *
 */
public class AsymmetricCriptor {

	/** 算法 */
	protected String algorithm;
	/** 公钥 */
	protected PublicKey publicKey;
	/** 私钥 */
	protected PrivateKey privateKey;
	/** Cipher负责完成加密或解密工作 */
	protected Cipher clipher;
	/** 签名，用于签名和验证 */
	protected Signature signature;
	
	protected Lock lock = new ReentrantLock();

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，创建新的私钥公钥对
	 * @param algorithm {@link SymmetricAlgorithm}
	 */
	public AsymmetricCriptor(AsymmetricAlgorithm algorithm) {
		this(algorithm, null, null);
	}
	
	/**
	 * 构造，创建新的私钥公钥对
	 * @param algorithm 算法
	 */
	public AsymmetricCriptor(String algorithm) {
		this(algorithm, null, null);
	}
	/**
	 * 构造
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * @param algorithm {@link SymmetricAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public AsymmetricCriptor(AsymmetricAlgorithm algorithm, byte[] privateKey, byte[] publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}

	/**
	 * 构造
	 * 
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public AsymmetricCriptor(String algorithm, byte[] privateKey, byte[] publicKey) {
		init(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 初始化<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密<br>
	 * 签名默认使用MD5摘要算法，如果需要自定义签名算法，调用 {@link AsymmetricCriptor#setSignature(Signature)}设置签名对象
	 * 
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @return {@link AsymmetricCriptor}
	 */
	public AsymmetricCriptor init(String algorithm, byte[] privateKey, byte[] publicKey) {
		this.algorithm = algorithm;
		try {
			this.clipher = Cipher.getInstance(algorithm);
			this.signature = Signature.getInstance("MD5with" + algorithm);//默认签名算法
		} catch (Exception e) {
			throw new CryptoException(e);
		}
		
		if(null ==privateKey && null == publicKey){
			initKeys();
		}else{
			if(null != privateKey){
				this.privateKey = SecureUtil.generatePrivateKey(algorithm, privateKey);
			}
			if(null != publicKey){
				this.publicKey = SecureUtil.generatePublicKey(algorithm, publicKey);
			}
		}
		return this;
	}

	/**
	 * 生成公钥和私钥
	 */
	public AsymmetricCriptor initKeys() {
		KeyPair keyPair = SecureUtil.generateKeyPair(this.algorithm);
		this.publicKey = keyPair.getPublic();
		this.privateKey = keyPair.getPrivate();
		return this;
	}

	// --------------------------------------------------------------------------------- Sign and Verify
	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data 加密数据
	 * @return 签名
	 */
	public byte[] sign(byte[] data) {
		try {
			signature.initSign(this.privateKey);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}
	
	/**
	 * 用公钥检验数字签名的合法性
	 * 
	 * @param data 数据
	 * @param sign 签名
	 * @return 是否验证通过
	 */
	public boolean verify(byte[] data, byte[] sign) {
		try {
			signature.initVerify(this.publicKey);
			signature.update(data);
			return signature.verify(sign);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}

	// --------------------------------------------------------------------------------- Encrypt
	/**
	 * 加密
	 * 
	 * @param data 被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(byte[] data, KeyType keyType) {
		lock.lock();
		try {
			clipher.init(Cipher.ENCRYPT_MODE, getKeyByType(keyType));
			return clipher.doFinal(data);

		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 加密
	 * 
	 * @param data 被加密的字符串
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data, String charset, KeyType keyType) {
		return encrypt(StrUtil.bytes(data, charset), keyType);
	}

	/**
	 * 加密，使用UTF-8编码
	 * 
	 * @param data 被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data, KeyType keyType) {
		return encrypt(StrUtil.bytes(data, CharsetUtil.CHARSET_UTF_8), keyType);
	}

	/**
	 * 加密
	 * 
	 * @param data 被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(InputStream data, KeyType keyType) {
		try {
			return encrypt(IoUtil.readBytes(data), keyType);
		} catch (IOException e) {
			throw new CryptoException(e);
		}
	}

	// --------------------------------------------------------------------------------- Decrypt
	/**
	 * 解密
	 * 
	 * @param bytes 被解密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 */
	public byte[] decrypt(byte[] bytes, KeyType keyType) {
		lock.lock();
		try {
			clipher.init(Cipher.DECRYPT_MODE, getKeyByType(keyType));
			return clipher.doFinal(bytes);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 解密
	 * 
	 * @param data 被解密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 */
	public byte[] decrypt(InputStream data, KeyType keyType) {
		try {
			return decrypt(IoUtil.readBytes(data), keyType);
		} catch (IOException e) {
			throw new CryptoException(e);
		}
	}

	// --------------------------------------------------------------------------------- Getters and Setters
	/**
	 * 获得公钥
	 * 
	 * @return 获得公钥
	 */
	public PublicKey getPublicKey() {
		return this.publicKey;
	}
	/**
	 * 获得公钥
	 * 
	 * @return 获得公钥
	 */
	public String getPublicKeyBase64() {
		return Base64.encode(getPublicKey().getEncoded());
	}
	/**
	 * 设置公钥
	 * 
	 * @param publicKey 公钥
	 * @return 自身 {@link AsymmetricCriptor}
	 */
	public AsymmetricCriptor setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
		return this;
	}

	/**
	 * 获得私钥
	 * 
	 * @return 获得私钥
	 */
	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}
	/**
	 * 获得私钥
	 * 
	 * @return 获得私钥
	 */
	public String getPrivateKeyBase64() {
		return Base64.encode(getPrivateKey().getEncoded());
	}
	/**
	 * 设置私钥
	 * 
	 * @param privateKey 私钥
	 * @return 自身 {@link AsymmetricCriptor}
	 */
	public AsymmetricCriptor setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
		return this;
	}
	
	/**
	 * 获得签名对象
	 * @return {@link Signature}
	 */
	public Signature getSignature() {
		return signature;
	}

	/**
	 * 设置签名
	 * @param signature 签名对象 {@link Signature}
	 * @return 自身 {@link AsymmetricCriptor}
	 */
	public AsymmetricCriptor setSignature(Signature signature) {
		this.signature = signature;
		return this;
	}

	/**
	 * 获得加密或解密器
	 * 
	 * @return 加密或解密
	 */
	public Cipher getClipher() {
		return clipher;
	}
	
	/**
	 * 根据密钥类型获得相应密钥
	 * @param type 类型 {@link KeyType}
	 * @return {@link Key}
	 */
	protected Key getKeyByType(KeyType type){
		switch (type) {
			case PrivateKey:
				if(null == this.privateKey){
					throw new NullPointerException("Private key must not null when use it !");
				}
				return this.privateKey;
			case PublicKey:
				if(null == this.privateKey){
					throw new NullPointerException("Public key must not null when use it !");
				}
				return this.publicKey;
		}
		throw new CryptoException("Uknown key type: " + type);
	}
}
