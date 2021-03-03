package cn.hutool.crypto.asymmetric;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.KeyUtil;

/**
 * 非对称基础，提供锁、私钥和公钥的持有
 *
 * @author Looly
 * @since 3.3.0
 */
public class BaseAsymmetric<T extends BaseAsymmetric<T>> {

	/**
	 * 算法
	 */
	protected String algorithm;
	/**
	 * 公钥
	 */
	protected PublicKey publicKey;
	/**
	 * 私钥
	 */
	protected PrivateKey privateKey;
	/**
	 * 锁
	 */
	protected final Lock lock = new ReentrantLock();

	// ------------------------------------------------------------------
	// Constructor start

	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  算法
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @since 3.1.1
	 */
	public BaseAsymmetric(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		init(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------
	// Constructor end

	/**
	 * 初始化<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密（签名）或者解密（校验）
	 *
	 * @param algorithm  算法
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	protected T init(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
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
		KeyPair keyPair = KeyUtil.generateKeyPair(this.algorithm);
		this.publicKey = keyPair.getPublic();
		this.privateKey = keyPair.getPrivate();
		return (T) this;
	}

	// ---------------------------------------------------------------------------------
	// Getters and Setters

	/**
	 * 获得公钥
	 *
	 * @return 获得公钥
	 */
	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	/**
	 * 获得公钥模数modulus
	 * 
	 * @return 公钥模数字节数组
	 */
	public byte[] getPublicKeyModulus() {
		return ((RSAPublicKey) this.publicKey).getModulus().toByteArray();
	}
	
	/**
	 * 获得公钥模数modulus
	 * 
	 * @return 公钥模数16进制
	 */
	public String getPublicKeyModulusHex() {
		return HexUtil.encodeHexStr(getPublicKeyModulus());
	}
	
	/**
	 * 获得公钥模数modulus
	 * 
	 * @return 公钥模数base64编码
	 */
	public String getPublicKeyModulusBase64() {
		return Base64.encode(getPublicKeyModulus());
	}

	/**
	 * 获得公钥指数exponent
	 * 
	 * @return 公钥指数字节数组
	 */
	public byte[] getPublicKeyExponent() {
		return ((RSAPublicKey) this.publicKey).getPublicExponent().toByteArray();
	}
	
	/**
	 * 获得公钥指数exponent
	 * 
	 * @return 公钥指数16进制
	 */
	public String getPublicKeyExponentHex() {
		return HexUtil.encodeHexStr(getPublicKeyExponent());
	}
	
	/**
	 * 获得公钥指数exponent
	 * 
	 * @return 公钥指数Base64
	 */
	public String getPublicKeyExponentBase64() {
		return Base64.encode(getPublicKeyExponent());
	}
	
	/**
	 * 获得私钥模数modulus
	 * 
	 * @return 私钥模数字节数组
	 */
	public byte[] getPrivateKeyModulus() {
		return ((RSAPrivateKey) this.privateKey).getModulus().toByteArray();
	}
	
	/**
	 * 获得私钥模数modulus
	 * 
	 * @return 私钥模数16进制
	 */
	public String getPrivateKeyModulusHex() {
		return HexUtil.encodeHexStr(getPrivateKeyModulus());
	}
	
	/**
	 * 获得私钥模数modulus
	 * 
	 * @return 私钥模数Base64
	 */
	public String getPrivateKeyModulusBase64() {
		return Base64.encode(getPrivateKeyModulus());
	}

	/**
	 * 获得私钥指数exponent
	 * 
	 * @return 私钥指数字节数组
	 */
	public byte[] getPrivateKeyExponent() {
		return ((RSAPrivateKey) this.privateKey).getPrivateExponent().toByteArray();
	}
	
	/**
	 * 获得私钥指数exponent
	 * 
	 * @return 私钥指数16进制
	 */
	public String getPrivateKeyExponentHex() {
		return HexUtil.encodeHexStr(getPrivateKeyExponent());
	}
	
	/**
	 * 获得私钥指数exponent
	 * 
	 * @return 私钥指数Base64
	 */
	public String getPrivateKeyExponentBase64() {
		return Base64.encode(getPrivateKeyExponent());
	}

	/**
	 * 获得公钥
	 *
	 * @return 获得公钥
	 */
	public String getPublicKeyBase64() {
		final PublicKey publicKey = getPublicKey();
		return (null == publicKey) ? null : Base64.encode(publicKey.getEncoded());
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
		final PrivateKey privateKey = getPrivateKey();
		return (null == privateKey) ? null : Base64.encode(privateKey.getEncoded());
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
	 * 设置密钥，可以是公钥{@link PublicKey}或者私钥{@link PrivateKey}
	 *
	 * @param key 密钥，可以是公钥{@link PublicKey}或者私钥{@link PrivateKey}
	 * @return this
	 * @since 5.2.0
	 */
	public T setKey(Key key) {
		Assert.notNull(key, "key must be not null !");

		if (key instanceof PublicKey) {
			return setPublicKey((PublicKey) key);
		} else if (key instanceof PrivateKey) {
			return setPrivateKey((PrivateKey) key);
		}
		throw new CryptoException("Unsupported key type: {}", key.getClass());
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
		throw new CryptoException("Unsupported key type: " + type);
	}
}
