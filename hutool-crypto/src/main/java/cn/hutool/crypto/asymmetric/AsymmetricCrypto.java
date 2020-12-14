package cn.hutool.crypto.asymmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;

/**
 * 非对称加密算法
 *
 * <pre>
 * 1、签名：使用私钥加密，公钥解密。
 * 用于让所有公钥所有者验证私钥所有者的身份并且用来防止私钥所有者发布的内容被篡改，但是不用来保证内容不被他人获得。
 *
 * 2、加密：用公钥加密，私钥解密。
 * 用于向公钥所有者发布信息,这个信息可能被他人篡改,但是无法被他人获得。
 * </pre>
 *
 * @author Looly
 */
public class AsymmetricCrypto extends AbstractAsymmetricCrypto<AsymmetricCrypto> {

	/**
	 * Cipher负责完成加密或解密工作
	 */
	protected Cipher cipher;

	/**
	 * 加密的块大小
	 */
	protected int encryptBlockSize = -1;
	/**
	 * 解密的块大小
	 */
	protected int decryptBlockSize = -1;

	/**
	 * 算法参数
	 */
	private AlgorithmParameterSpec algorithmParameterSpec;

	// ------------------------------------------------------------------ Constructor start

	/**
	 * 构造，创建新的私钥公钥对
	 *
	 * @param algorithm {@link SymmetricAlgorithm}
	 */
	@SuppressWarnings("RedundantCast")
	public AsymmetricCrypto(AsymmetricAlgorithm algorithm) {
		this(algorithm, (byte[]) null, (byte[]) null);
	}

	/**
	 * 构造，创建新的私钥公钥对
	 *
	 * @param algorithm 算法
	 */
	@SuppressWarnings("RedundantCast")
	public AsymmetricCrypto(String algorithm) {
		this(algorithm, (byte[]) null, (byte[]) null);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm     {@link SymmetricAlgorithm}
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr  公钥Hex或Base64表示
	 */
	public AsymmetricCrypto(AsymmetricAlgorithm algorithm, String privateKeyStr, String publicKeyStr) {
		this(algorithm.getValue(), SecureUtil.decode(privateKeyStr), SecureUtil.decode(publicKeyStr));
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  {@link SymmetricAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 */
	public AsymmetricCrypto(AsymmetricAlgorithm algorithm, byte[] privateKey, byte[] publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  {@link SymmetricAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @since 3.1.1
	 */
	public AsymmetricCrypto(AsymmetricAlgorithm algorithm, PrivateKey privateKey, PublicKey publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm        非对称加密算法
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64  公钥Base64
	 */
	public AsymmetricCrypto(String algorithm, String privateKeyBase64, String publicKeyBase64) {
		this(algorithm, Base64.decode(privateKeyBase64), Base64.decode(publicKeyBase64));
	}

	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  算法
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 */
	public AsymmetricCrypto(String algorithm, byte[] privateKey, byte[] publicKey) {
		this(algorithm, //
				KeyUtil.generatePrivateKey(algorithm, privateKey), //
				KeyUtil.generatePublicKey(algorithm, publicKey)//
		);
	}

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
	public AsymmetricCrypto(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		super(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 获取加密块大小
	 *
	 * @return 加密块大小
	 */
	public int getEncryptBlockSize() {
		return encryptBlockSize;
	}

	/**
	 * 设置加密块大小
	 *
	 * @param encryptBlockSize 加密块大小
	 */
	public void setEncryptBlockSize(int encryptBlockSize) {
		this.encryptBlockSize = encryptBlockSize;
	}

	/**
	 * 获取解密块大小
	 *
	 * @return 解密块大小
	 */
	public int getDecryptBlockSize() {
		return decryptBlockSize;
	}

	/**
	 * 设置解密块大小
	 *
	 * @param decryptBlockSize 解密块大小
	 */
	public void setDecryptBlockSize(int decryptBlockSize) {
		this.decryptBlockSize = decryptBlockSize;
	}

	/**
	 * 获取{@link AlgorithmParameterSpec}<br>
	 * 在某些算法中，需要特别的参数，例如在ECIES中，此处为IESParameterSpec
	 *
	 * @return {@link AlgorithmParameterSpec}
	 * @since 5.4.3
	 */
	public AlgorithmParameterSpec getAlgorithmParameterSpec() {
		return algorithmParameterSpec;
	}

	/**
	 * 设置{@link AlgorithmParameterSpec}<br>
	 * 在某些算法中，需要特别的参数，例如在ECIES中，此处为IESParameterSpec
	 *
	 * @param algorithmParameterSpec {@link AlgorithmParameterSpec}
	 * @since 5.4.3
	 */
	public void setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec) {
		this.algorithmParameterSpec = algorithmParameterSpec;
	}

	@Override
	public AsymmetricCrypto init(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		super.init(algorithm, privateKey, publicKey);
		initCipher();
		return this;
	}

	// --------------------------------------------------------------------------------- Encrypt

	/**
	 * 加密
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	@Override
	public byte[] encrypt(byte[] data, KeyType keyType) {
		final Key key = getKeyByType(keyType);
		lock.lock();
		try {
			initCipher(Cipher.ENCRYPT_MODE, key);

			if (this.encryptBlockSize < 0) {
				// 在引入BC库情况下，自动获取块大小
				final int blockSize = this.cipher.getBlockSize();
				if (blockSize > 0) {
					this.encryptBlockSize = blockSize;
				}
			}

			return doFinal(data, this.encryptBlockSize < 0 ? data.length : this.encryptBlockSize);
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
	 * @param data    被解密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 */
	@Override
	public byte[] decrypt(byte[] data, KeyType keyType) {
		final Key key = getKeyByType(keyType);
		lock.lock();
		try {
			initCipher(Cipher.DECRYPT_MODE, key);

			if (this.decryptBlockSize < 0) {
				// 在引入BC库情况下，自动获取块大小
				final int blockSize = this.cipher.getBlockSize();
				if (blockSize > 0) {
					this.decryptBlockSize = blockSize;
				}
			}

			return doFinal(data, this.decryptBlockSize < 0 ? data.length : this.decryptBlockSize);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	// --------------------------------------------------------------------------------- Getters and Setters

	/**
	 * 获得加密或解密器
	 *
	 * @return 加密或解密
	 * @deprecated 拼写错误，请使用{@link #getCipher()}
	 */
	@Deprecated
	public Cipher getClipher() {
		return cipher;
	}

	/**
	 * 获得加密或解密器
	 *
	 * @return 加密或解密
	 * @since 5.4.3
	 */
	public Cipher getCipher() {
		return cipher;
	}

	/**
	 * 初始化{@link Cipher}，默认尝试加载BC库
	 *
	 * @since 4.5.2
	 */
	protected void initCipher() {
		this.cipher = SecureUtil.createCipher(algorithm);
	}

	/**
	 * 加密或解密
	 *
	 * @param data         被加密或解密的内容数据
	 * @param maxBlockSize 最大块（分段）大小
	 * @return 加密或解密后的数据
	 * @throws IllegalBlockSizeException 分段异常
	 * @throws BadPaddingException       padding错误异常
	 * @throws IOException               IO异常，不会被触发
	 */
	private byte[] doFinal(byte[] data, int maxBlockSize) throws IllegalBlockSizeException, BadPaddingException, IOException {
		// 模长
		final int dataLength = data.length;

		// 不足分段
		if (dataLength <= maxBlockSize) {
			return this.cipher.doFinal(data, 0, dataLength);
		}

		// 分段解密
		return doFinalWithBlock(data, maxBlockSize);
	}

	/**
	 * 分段加密或解密
	 *
	 * @param data         数据
	 * @param maxBlockSize 最大分段的段大小，不能为小于1
	 * @return 加密或解密后的数据
	 * @throws IllegalBlockSizeException 分段异常
	 * @throws BadPaddingException       padding错误异常
	 * @throws IOException               IO异常，不会被触发
	 */
	private byte[] doFinalWithBlock(byte[] data, int maxBlockSize) throws IllegalBlockSizeException, BadPaddingException, IOException {
		final int dataLength = data.length;
		@SuppressWarnings("resource") final FastByteArrayOutputStream out = new FastByteArrayOutputStream();

		int offSet = 0;
		// 剩余长度
		int remainLength = dataLength;
		int blockSize;
		// 对数据分段处理
		while (remainLength > 0) {
			blockSize = Math.min(remainLength, maxBlockSize);
			out.write(cipher.doFinal(data, offSet, blockSize));

			offSet += blockSize;
			remainLength = dataLength - offSet;
		}

		return out.toByteArray();
	}

	/**
	 * 初始化{@link Cipher}
	 *
	 * @param mode 模式，可选{@link Cipher#ENCRYPT_MODE}或者{@link Cipher#DECRYPT_MODE}
	 * @param key  密钥
	 * @throws InvalidAlgorithmParameterException 异常算法错误
	 * @throws InvalidKeyException                异常KEY错误
	 */
	private void initCipher(int mode, Key key) throws InvalidAlgorithmParameterException, InvalidKeyException {
		if (null != this.algorithmParameterSpec) {
			cipher.init(mode, key, this.algorithmParameterSpec);
		} else {
			cipher.init(mode, key);
		}
	}
}
