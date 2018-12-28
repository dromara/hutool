package cn.hutool.crypto.asymmetric;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;

import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.SecureUtil;

/**
 * 国密SM2算法实现，基于BC库<br>
 * SM2算法只支持公钥加密，私钥解密
 * 
 * @author looly
 * @since 4.3.2
 */
public class SM2 extends AbstractAsymmetricCrypto<SM2> {

	/** 算法EC */
	private static final String ALGORITHM_SM2 = "SM2";

	protected SM2Engine engine;
	protected SM2Signer signer;

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，生成新的私钥公钥对
	 */
	public SM2() {
		this((byte[]) null, (byte[]) null);
	}

	/**
	 * 构造<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr 公钥Hex或Base64表示
	 */
	public SM2(String privateKeyStr, String publicKeyStr) {
		this(SecureUtil.decodeKey(privateKeyStr), SecureUtil.decodeKey(publicKeyStr));
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public SM2(byte[] privateKey, byte[] publicKey) {
		this(//
				SecureUtil.generatePrivateKey(ALGORITHM_SM2, privateKey), //
				SecureUtil.generatePublicKey(ALGORITHM_SM2, publicKey)//
		);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public SM2(PrivateKey privateKey, PublicKey publicKey) {
		super(ALGORITHM_SM2, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 初始化<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密（签名）或者解密（校验）
	 * 
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @return this
	 */
	public SM2 init(PrivateKey privateKey, PublicKey publicKey) {
		return this.init(ALGORITHM_SM2, privateKey, publicKey);
	}

	// --------------------------------------------------------------------------------- Encrypt
	/**
	 * 加密
	 * 
	 * @param data 被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 * @throws CryptoException 包括InvalidKeyException和InvalidCipherTextException的包装异常
	 */
	@Override
	public byte[] encrypt(byte[] data, KeyType keyType) throws CryptoException {
		lock.lock();
		if (null == this.engine) {
			this.engine = new SM2Engine();
		}
		final SM2Engine engine = this.engine;
		try {
			engine.init(true, new ParametersWithRandom(generateCipherParameters(keyType)));
			return engine.processBlock(data, 0, data.length);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	// --------------------------------------------------------------------------------- Decrypt
	/**
	 * 解密
	 * 
	 * @param data SM2密文，实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 * @throws CryptoException 包括InvalidKeyException和InvalidCipherTextException的包装异常
	 */
	@Override
	public byte[] decrypt(byte[] data, KeyType keyType) throws CryptoException {
		lock.lock();
		if (null == this.engine) {
			this.engine = new SM2Engine();
		}
		final SM2Engine engine = this.engine;
		try {
			engine.init(false, generateCipherParameters(keyType));
			return engine.processBlock(data, 0, data.length);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	// --------------------------------------------------------------------------------- Sign and Verify
	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data 加密数据
	 * @return 签名
	 */
	public byte[] sign(byte[] data) {
		return sign(data, null);
	}
	
	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data 加密数据
	 * @param id 可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
	 * @return 签名
	 */
	public byte[] sign(byte[] data, byte[] id) {
		lock.lock();
		if (null == this.signer) {
			this.signer = new SM2Signer();
		}
		final SM2Signer signer = this.signer;
		try {
			CipherParameters param = new ParametersWithRandom(generateCipherParameters(KeyType.PrivateKey));
			if (id != null) {
				param = new ParametersWithID(param, id);
			}
			signer.init(true, param);
			signer.update(data, 0, data.length);
			return signer.generateSignature();
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
		return verify(data, sign, null);
	}

	/**
	 * 用公钥检验数字签名的合法性
	 * 
	 * @param data 数据
	 * @param sign 签名
	 * @param id 可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
	 * @return 是否验证通过
	 */
	public boolean verify(byte[] data, byte[] sign, byte[] id) {
		lock.lock();
		if (null == this.signer) {
			this.signer = new SM2Signer();
		}
		final SM2Signer signer = this.signer;
		try {
			CipherParameters param = generateCipherParameters(KeyType.PublicKey);
			if (id != null) {
				param = new ParametersWithID(param, id);
			}
			signer.init(false, param);
			signer.update(data, 0, data.length);
			return signer.verifySignature(sign);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 生成{@link CipherParameters}
	 * 
	 * @param keyType Key类型枚举，包括私钥或公钥
	 * @return {@link CipherParameters}
	 */
	private CipherParameters generateCipherParameters(KeyType keyType) {
		try {
			switch (keyType) {
			case PublicKey:
				return ECUtil.generatePublicKeyParameter(this.publicKey);
			case PrivateKey:
				return ECUtil.generatePrivateKeyParameter(this.privateKey);
			}
		} catch (InvalidKeyException e) {
			throw new CryptoException(e);
		}
		return null;
	}
	// ------------------------------------------------------------------------------------------------------------------------- Private method end
}
