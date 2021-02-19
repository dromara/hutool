package cn.hutool.crypto.asymmetric;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Set;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.SecureUtil;

/**
 * 签名包装，{@link Signature} 包装类
 * 
 * @author looly
 * @since 3.3.0
 */
public class Sign extends BaseAsymmetric<Sign> {

	/** 签名，用于签名和验证 */
	protected Signature signature;

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，创建新的私钥公钥对
	 * 
	 * @param algorithm {@link SignAlgorithm}
	 */
	public Sign(SignAlgorithm algorithm) {
		this(algorithm, null, (byte[]) null);
	}

	/**
	 * 构造，创建新的私钥公钥对
	 * 
	 * @param algorithm 算法
	 */
	public Sign(String algorithm) {
		this(algorithm, null, (byte[]) null);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * 
	 * @param algorithm {@link SignAlgorithm}
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr 公钥Hex或Base64表示
	 */
	public Sign(SignAlgorithm algorithm, String privateKeyStr, String publicKeyStr) {
		this(algorithm.getValue(), SecureUtil.decode(privateKeyStr), SecureUtil.decode(publicKeyStr));
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * 
	 * @param algorithm {@link SignAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public Sign(SignAlgorithm algorithm, byte[] privateKey, byte[] publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}
	
	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * 
	 * @param algorithm {@link SignAlgorithm}
	 * @param keyPair 密钥对（包括公钥和私钥）
	 */
	public Sign(SignAlgorithm algorithm, KeyPair keyPair) {
		this(algorithm.getValue(), keyPair);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * 
	 * @param algorithm {@link SignAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public Sign(SignAlgorithm algorithm, PrivateKey privateKey, PublicKey publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * 
	 * @param algorithm 非对称加密算法
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64 公钥Base64
	 */
	public Sign(String algorithm, String privateKeyBase64, String publicKeyBase64) {
		this(algorithm, Base64.decode(privateKeyBase64), Base64.decode(publicKeyBase64));
	}

	/**
	 * 构造
	 * 
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * 
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public Sign(String algorithm, byte[] privateKey, byte[] publicKey) {
		this(algorithm, //
				SecureUtil.generatePrivateKey(algorithm, privateKey), //
				SecureUtil.generatePublicKey(algorithm, publicKey)//
		);
	}
	
	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * 
	 * @param algorithm 算法，见{@link SignAlgorithm}
	 * @param keyPair 密钥对（包括公钥和私钥）
	 */
	public Sign(String algorithm, KeyPair keyPair) {
		this(algorithm, keyPair.getPrivate(), keyPair.getPublic());
	}

	/**
	 * 构造
	 * 
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * 
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public Sign(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		super(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end
	
	/**
	 * 初始化
	 * 
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @return this
	 */
	@Override
	public Sign init(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		try {
			signature = Signature.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
		super.init(algorithm, privateKey, publicKey);
		return this;
	}
	
	/**
	 * 设置签名的参数
	 * 
	 * @param params {@link AlgorithmParameterSpec}
	 * @return this
	 * @since 4.6.5
	 */
	public Sign setParameter(AlgorithmParameterSpec params) {
		try {
			this.signature.setParameter(params);
		} catch (InvalidAlgorithmParameterException e) {
			throw new CryptoException(e);
		}
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
		lock.lock();
		try {
			signature.initSign(this.privateKey);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
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
		lock.lock();
		try {
			signature.initVerify(this.publicKey);
			signature.update(data);
			return signature.verify(sign);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获得签名对象
	 * 
	 * @return {@link Signature}
	 */
	public Signature getSignature() {
		return signature;
	}

	/**
	 * 设置签名
	 * 
	 * @param signature 签名对象 {@link Signature}
	 * @return 自身 {@link AsymmetricCrypto}
	 */
	public Sign setSignature(Signature signature) {
		this.signature = signature;
		return this;
	}

	/**
	 * 设置{@link Certificate} 为PublicKey<br>
	 * 如果Certificate是X509Certificate，我们需要检查是否有密钥扩展
	 * 
	 * @param certificate {@link Certificate}
	 * @return this
	 */
	public Sign setCertificate(Certificate certificate) {
		// If the certificate is of type X509Certificate,
		// we should check whether it has a Key Usage
		// extension marked as critical.
		if (certificate instanceof X509Certificate) {
			// Check whether the cert has a key usage extension
			// marked as a critical extension.
			// The OID for KeyUsage extension is 2.5.29.15.
			final X509Certificate cert = (X509Certificate) certificate;
			final Set<String> critSet = cert.getCriticalExtensionOIDs();

			if (CollUtil.isNotEmpty(critSet) && critSet.contains("2.5.29.15")) {
				final boolean[] keyUsageInfo = cert.getKeyUsage();
				// keyUsageInfo[0] is for digitalSignature.
				if ((keyUsageInfo != null) && (keyUsageInfo[0] == false)) {
					throw new CryptoException("Wrong key usage");
				}
			}
		}
		this.publicKey = certificate.getPublicKey();
		return this;
	}
}
