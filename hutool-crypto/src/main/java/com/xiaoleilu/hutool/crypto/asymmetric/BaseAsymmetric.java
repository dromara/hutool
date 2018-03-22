package com.xiaoleilu.hutool.crypto.asymmetric;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.xiaoleilu.hutool.codec.Base64;
import com.xiaoleilu.hutool.crypto.CryptoException;
import com.xiaoleilu.hutool.crypto.SecureUtil;

/**
 * 非对称基础，提供锁、私钥和公钥的持有
 * 
 * @author Looly
 * @since 3.3.0
 */
public class BaseAsymmetric<T extends BaseAsymmetric<T>> {

	/** 算法 */
	protected String algorithm;
	/** 公钥 */
	protected PublicKey publicKey;
	/** 私钥 */
	protected PrivateKey privateKey;
	/** 锁 */
	protected Lock lock = new ReentrantLock();

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，创建新的私钥公钥对
	 * 
	 * @param algorithm 算法
	 */
	public BaseAsymmetric(String algorithm) {
		this(algorithm, (byte[]) null, (byte[]) null);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param algorithm 非对称加密算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @since 3.1.1
	 */
	public BaseAsymmetric(AsymmetricAlgorithm algorithm, PrivateKey privateKey, PublicKey publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param algorithm 非对称加密算法
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64 公钥Base64
	 */
	public BaseAsymmetric(String algorithm, String privateKeyBase64, String publicKeyBase64) {
		this(algorithm, Base64.decode(privateKeyBase64), Base64.decode(publicKeyBase64));
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
	public BaseAsymmetric(String algorithm, byte[] privateKey, byte[] publicKey) {
		init(algorithm, privateKey, publicKey);
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
	 * @since 3.1.1
	 */
	public BaseAsymmetric(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		init(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 初始化<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密<br>
	 * 
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @return this
	 */
	public T init(String algorithm, byte[] privateKey, byte[] publicKey) {
		final PrivateKey privateKeyObj = (null == privateKey) ? null : SecureUtil.generatePrivateKey(algorithm, privateKey);
		final PublicKey publicKeyObj = (null == publicKey) ? null : SecureUtil.generatePublicKey(algorithm, publicKey);

		return init(algorithm, privateKeyObj, publicKeyObj);
	}

	/**
	 * 初始化<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密（签名）或者解密（校验）
	 * 
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T init(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		this.algorithm = algorithm;

		if (null == privateKey && null == publicKey) {
			initKeys();
		} else {
			if (null != privateKey) {
				this.privateKey = privateKey;
			}
			if (null != publicKey) {
				this.publicKey = publicKey;
			}
		}
		return (T) this;
	}

	/**
	 * 生成公钥和私钥
	 * 
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T initKeys() {
		KeyPair keyPair = SecureUtil.generateKeyPair(this.algorithm);
		this.publicKey = keyPair.getPublic();
		this.privateKey = keyPair.getPrivate();
		return (T) this;
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
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
		return (T) this;
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
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
		return (T) this;
	}

	/**
	 * 根据密钥类型获得相应密钥
	 * 
	 * @param type 类型 {@link KeyType}
	 * @return {@link Key}
	 */
	protected Key getKeyByType(KeyType type) {
		switch (type) {
		case PrivateKey:
			if (null == this.privateKey) {
				throw new NullPointerException("Private key must not null when use it !");
			}
			return this.privateKey;
		case PublicKey:
			if (null == this.publicKey) {
				throw new NullPointerException("Public key must not null when use it !");
			}
			return this.publicKey;
		}
		throw new CryptoException("Uknown key type: " + type);
	}
}
