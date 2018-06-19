package cn.hutool.crypto.asymmetric;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.SecureUtil;

/**
 * <p>
 * RSA公钥/私钥/签名加密解密
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 * 
 * @author Looly
 *
 */
public class RSA extends AsymmetricCrypto {

	private static final AsymmetricAlgorithm ALGORITHM_RSA = AsymmetricAlgorithm.RSA;

	// ------------------------------------------------------------------ Static method start
	/**
	 * 生成RSA私钥
	 * 
	 * @param modulus N特征值
	 * @param privateExponent d特征值
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(BigInteger modulus, BigInteger privateExponent) {
		return SecureUtil.generatePrivateKey(ALGORITHM_RSA.getValue(), new RSAPrivateKeySpec(modulus, privateExponent));
	}

	/**
	 * 生成RSA公钥
	 * 
	 * @param modulus N特征值
	 * @param publicExponent e特征值
	 * @return {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(BigInteger modulus, BigInteger publicExponent) {
		return SecureUtil.generatePublicKey(ALGORITHM_RSA.getValue(), new RSAPublicKeySpec(modulus, publicExponent));
	}
	// ------------------------------------------------------------------ Static method end

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，生成新的私钥公钥对
	 */
	public RSA() {
		super(ALGORITHM_RSA);
	}

	/**
	 * 构造<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64 公钥Base64
	 */
	public RSA(String privateKeyBase64, String publicKeyBase64) {
		super(ALGORITHM_RSA, privateKeyBase64, publicKeyBase64);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public RSA(byte[] privateKey, byte[] publicKey) {
		super(ALGORITHM_RSA, privateKey, publicKey);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param modulus N特征值
	 * @param privateExponent d特征值
	 * @param publicExponent e特征值
	 * @since 3.1.1
	 */
	public RSA(BigInteger modulus, BigInteger privateExponent, BigInteger publicExponent) {
		this(generatePrivateKey(modulus, privateExponent), generatePublicKey(modulus, publicExponent));
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @since 3.1.1
	 */
	public RSA(PrivateKey privateKey, PublicKey publicKey) {
		super(ALGORITHM_RSA, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 分组加密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @return 加密后的密文
	 * @throws CryptoException 加密异常
	 * @deprecated 请使用 {@link #encryptBcd(String, KeyType)}
	 */
	@Deprecated
	public String encryptStr(String data, KeyType keyType) {
		return encryptStr(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组加密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 加密后的密文
	 * @throws CryptoException 加密异常
	 * @since 3.1.1
	 * @deprecated 请使用 {@link #encryptBcd(String, KeyType, Charset)}
	 */
	@Deprecated
	public String encryptStr(String data, KeyType keyType, Charset charset) {
		return encryptBcd(data, keyType, charset);
	}

	@Override
	public byte[] encrypt(byte[] data, KeyType keyType) {
		if(this.encryptBlockSize < 0) {
			// 加密数据长度 <= 模长-11
			this.encryptBlockSize = ((RSAKey) getKeyByType(keyType)).getModulus().bitLength() / 8 - 11;
		}
		return super.encrypt(data, keyType);
	}

	/**
	 * 分组解密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @return 解密后的密文
	 * @deprecated 请使用 {@link #decryptFromBcd(String, KeyType)}
	 */
	@Deprecated
	public String decryptStr(String data, KeyType keyType) {
		return decryptStr(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组解密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 * @since 3.1.1
	 * @deprecated 请使用 {@link #decryptFromBcd(String, KeyType, Charset)}
	 */
	@Deprecated
	public String decryptStr(String data, KeyType keyType, Charset charset) {
		return StrUtil.str(decryptFromBcd(data, keyType, charset), charset);
	}

	@Override
	public byte[] decrypt(byte[] bytes, KeyType keyType) {
		if(this.decryptBlockSize < 0) {
			// 加密数据长度 <= 模长-11
			this.decryptBlockSize = ((RSAKey) getKeyByType(keyType)).getModulus().bitLength() / 8;
		}
		return super.decrypt(bytes, keyType);
	}
}
