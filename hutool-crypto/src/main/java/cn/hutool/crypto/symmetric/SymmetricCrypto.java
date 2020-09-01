package cn.hutool.crypto.symmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.*;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.spec.AlgorithmParameterSpec;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对称加密算法<br>
 * 在对称加密算法中，数据发信方将明文（原始数据）和加密密钥一起经过特殊加密算法处理后，使其变成复杂的加密密文发送出去。<br>
 * 收信方收到密文后，若想解读原文，则需要使用加密用过的密钥及相同算法的逆算法对密文进行解密，才能使其恢复成可读明文。<br>
 * 在对称加密算法中，使用的密钥只有一个，发收信双方都使用这个密钥对数据进行加密和解密，这就要求解密方事先必须知道加密密钥。<br>
 *
 * @author Looly
 */
public class SymmetricCrypto implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * SecretKey 负责保存对称密钥
	 */
	private SecretKey secretKey;
	/**
	 * Cipher负责完成加密或解密工作
	 */
	private Cipher cipher;
	/**
	 * 加密解密参数
	 */
	private AlgorithmParameterSpec params;
	/**
	 * 是否0填充
	 */
	private boolean isZeroPadding;
	private final Lock lock = new ReentrantLock();

	// ------------------------------------------------------------------ Constructor start

	/**
	 * 构造，使用随机密钥
	 *
	 * @param algorithm {@link SymmetricAlgorithm}
	 */
	public SymmetricCrypto(SymmetricAlgorithm algorithm) {
		this(algorithm, (byte[]) null);
	}

	/**
	 * 构造，使用随机密钥
	 *
	 * @param algorithm 算法，可以是"algorithm/mode/padding"或者"algorithm"
	 */
	public SymmetricCrypto(String algorithm) {
		this(algorithm, (byte[]) null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法 {@link SymmetricAlgorithm}
	 * @param key       自定义KEY
	 */
	public SymmetricCrypto(SymmetricAlgorithm algorithm, byte[] key) {
		this(algorithm.getValue(), key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法 {@link SymmetricAlgorithm}
	 * @param key       自定义KEY
	 * @since 3.1.2
	 */
	public SymmetricCrypto(SymmetricAlgorithm algorithm, SecretKey key) {
		this(algorithm.getValue(), key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 */
	public SymmetricCrypto(String algorithm, byte[] key) {
		this(algorithm, KeyUtil.generateKey(algorithm, key));
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 3.1.2
	 */
	public SymmetricCrypto(String algorithm, SecretKey key) {
		this(algorithm, key, null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm  算法
	 * @param key        密钥
	 * @param paramsSpec 算法参数，例如加盐等
	 * @since 3.3.0
	 */
	public SymmetricCrypto(String algorithm, SecretKey key, AlgorithmParameterSpec paramsSpec) {
		init(algorithm, key);
		if (null != paramsSpec) {
			setParams(paramsSpec);
		}
	}

	// ------------------------------------------------------------------ Constructor end

	/**
	 * 初始化
	 *
	 * @param algorithm 算法
	 * @param key       密钥，如果为<code>null</code>自动生成一个key
	 * @return {@link SymmetricCrypto}的子对象，即子对象自身
	 */
	public SymmetricCrypto init(String algorithm, SecretKey key) {
		Assert.notBlank(algorithm, "'algorithm' must be not blank !");
		this.secretKey = key;

		// 对于PBE算法使用随机数加盐
		if (algorithm.startsWith("PBE")) {
			this.params = new PBEParameterSpec(RandomUtil.randomBytes(8), 100);
		}

		// 检查是否为ZeroPadding，是则替换为NoPadding，并标记以便单独处理
		if (algorithm.contains(Padding.ZeroPadding.name())) {
			algorithm = StrUtil.replace(algorithm, Padding.ZeroPadding.name(), Padding.NoPadding.name());
			this.isZeroPadding = true;
		}

		this.cipher = SecureUtil.createCipher(algorithm);
		return this;
	}

	/**
	 * 设置 {@link AlgorithmParameterSpec}，通常用于加盐或偏移向量
	 *
	 * @param params {@link AlgorithmParameterSpec}
	 * @return 自身
	 */
	public SymmetricCrypto setParams(AlgorithmParameterSpec params) {
		this.params = params;
		return this;
	}

	/**
	 * 设置偏移向量
	 *
	 * @param iv {@link IvParameterSpec}偏移向量
	 * @return 自身
	 */
	public SymmetricCrypto setIv(IvParameterSpec iv) {
		setParams(iv);
		return this;
	}

	/**
	 * 设置偏移向量
	 *
	 * @param iv 偏移向量，加盐
	 * @return 自身
	 */
	public SymmetricCrypto setIv(byte[] iv) {
		setIv(new IvParameterSpec(iv));
		return this;
	}

	// --------------------------------------------------------------------------------- Encrypt

	/**
	 * 加密
	 *
	 * @param data 被加密的bytes
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(byte[] data) {
		lock.lock();
		try {
			if (null == this.params) {
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			} else {
				cipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
			}
				return cipher.doFinal(paddingDataWithZero(data, cipher.getBlockSize()));
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 加密
	 *
	 * @param data 数据
	 * @return 加密后的Hex
	 */
	public String encryptHex(byte[] data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data 数据
	 * @return 加密后的Base64
	 * @since 4.0.1
	 */
	public String encryptBase64(byte[] data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data, String charset) {
		return encrypt(StrUtil.bytes(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data, Charset charset) {
		return encrypt(StrUtil.bytes(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Hex
	 * @since 4.5.12
	 */
	public String encryptHex(String data, String charset) {
		return HexUtil.encodeHexStr(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Hex
	 * @since 4.5.12
	 */
	public String encryptHex(String data, Charset charset) {
		return HexUtil.encodeHexStr(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Base64
	 */
	public String encryptBase64(String data, String charset) {
		return Base64.encode(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Base64
	 * @since 4.5.12
	 */
	public String encryptBase64(String data, Charset charset) {
		return Base64.encode(encrypt(data, charset));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data) {
		return encrypt(StrUtil.bytes(data, CharsetUtil.CHARSET_UTF_8));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Hex
	 */
	public String encryptHex(String data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 */
	public String encryptBase64(String data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	public byte[] encrypt(InputStream data) throws IORuntimeException {
		return encrypt(IoUtil.readBytes(data));
	}

	/**
	 * 加密
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Hex
	 */
	public String encryptHex(InputStream data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 */
	public String encryptBase64(InputStream data) {
		return Base64.encode(encrypt(data));
	}

	// --------------------------------------------------------------------------------- Decrypt

	/**
	 * 解密
	 *
	 * @param bytes 被解密的bytes
	 * @return 解密后的bytes
	 */
	public byte[] decrypt(byte[] bytes) {
		final int blockSize;
		final byte[] decryptData;

		lock.lock();
		try {
			if (null == this.params) {
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
			}
			blockSize = cipher.getBlockSize();
			decryptData = cipher.doFinal(bytes);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}

		return removePadding(decryptData, blockSize);
	}

	/**
	 * 解密为字符串
	 *
	 * @param bytes   被解密的bytes
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	public String decryptStr(byte[] bytes, Charset charset) {
		return StrUtil.str(decrypt(bytes), charset);
	}

	/**
	 * 解密为字符串，默认UTF-8编码
	 *
	 * @param bytes 被解密的bytes
	 * @return 解密后的String
	 */
	public String decryptStr(byte[] bytes) {
		return decryptStr(bytes, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串
	 *
	 * @param data 被解密的String，必须为16进制字符串或Base64表示形式
	 * @return 解密后的bytes
	 */
	public byte[] decrypt(String data) {
		return decrypt(SecureUtil.decode(data));
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串
	 *
	 * @param data    被解密的String
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	public String decryptStr(String data, Charset charset) {
		return StrUtil.str(decrypt(data), charset);
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串，默认UTF-8编码
	 *
	 * @param data 被解密的String
	 * @return 解密后的String
	 */
	public String decryptStr(String data) {
		return decryptStr(data, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 解密，不会关闭流
	 *
	 * @param data 被解密的bytes
	 * @return 解密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	public byte[] decrypt(InputStream data) throws IORuntimeException {
		return decrypt(IoUtil.readBytes(data));
	}

	/**
	 * 解密，不会关闭流
	 *
	 * @param data    被解密的InputStream
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	public String decryptStr(InputStream data, Charset charset) {
		return StrUtil.str(decrypt(data), charset);
	}

	/**
	 * 解密
	 *
	 * @param data 被解密的InputStream
	 * @return 解密后的String
	 */
	public String decryptStr(InputStream data) {
		return decryptStr(data, CharsetUtil.CHARSET_UTF_8);
	}

	// --------------------------------------------------------------------------------- Getters

	/**
	 * 获得对称密钥
	 *
	 * @return 获得对称密钥
	 */
	public SecretKey getSecretKey() {
		return secretKey;
	}

	/**
	 * 获得加密或解密器
	 *
	 * @return 加密或解密
	 */
	public Cipher getCipher() {
		return cipher;
	}

	// --------------------------------------------------------------------------------- Private method start

	/**
	 * 数据按照blockSize的整数倍长度填充填充0
	 *
	 * <p>
	 * 在{@link Padding#ZeroPadding} 模式下，且数据长度不是blockSize的整数倍才有效，否则返回原数据
	 *
	 * <p>
	 * 见：https://blog.csdn.net/OrangeJack/article/details/82913804
	 *
	 * @param data      数据
	 * @param blockSize 块大小
	 * @return 填充后的数据，如果isZeroPadding为false或长度刚好，返回原数据
	 * @since 4.6.7
	 */
	private byte[] paddingDataWithZero(byte[] data, int blockSize) {
		if (this.isZeroPadding) {
			final int length = data.length;
			// 按照块拆分后的数据中多余的数据
			final int remainLength = length % blockSize;
			if (remainLength > 0) {
				// 新长度为blockSize的整数倍，多余部分填充0
				return ArrayUtil.resize(data, length + blockSize - remainLength);
			}
		}
		return data;
	}

	/**
	 * 数据按照blockSize去除填充部分，用于解密
	 *
	 * <p>
	 * 在{@link Padding#ZeroPadding} 模式下，且数据长度不是blockSize的整数倍才有效，否则返回原数据
	 *
	 * @param data      数据
	 * @param blockSize 块大小
	 * @return 去除填充后的数据，如果isZeroPadding为false或长度刚好，返回原数据
	 * @since 4.6.7
	 */
	private byte[] removePadding(byte[] data, int blockSize) {
		if (this.isZeroPadding) {
			final int length = data.length;
			final int remainLength = length % blockSize;
			if (remainLength == 0) {
				// 解码后的数据正好是块大小的整数倍，说明可能存在补0的情况，去掉末尾所有的0
				int i = length - 1;
				while (i >= 0 && 0 == data[i]) {
					i--;
				}
				return ArrayUtil.resize(data, i + 1);
			}
		}
		return data;
	}
	// --------------------------------------------------------------------------------- Private method end
}
