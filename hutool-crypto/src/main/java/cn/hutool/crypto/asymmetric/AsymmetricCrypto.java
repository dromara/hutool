package cn.hutool.crypto.asymmetric;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

/**
 * 非对称加密算法<br>
 * 1、签名：使用私钥加密，公钥解密。用于让所有公钥所有者验证私钥所有者的身份并且用来防止私钥所有者发布的内容被篡改，但是不用来保证内容不被他人获得。<br>
 * 2、加密：用公钥加密，私钥解密。用于向公钥所有者发布信息,这个信息可能被他人篡改,但是无法被他人获得。
 * 
 * @author Looly
 *
 */
public class AsymmetricCrypto extends BaseAsymmetric<AsymmetricCrypto> {

	/** Cipher负责完成加密或解密工作 */
	protected Cipher clipher;

	/** 加密的块大小 */
	protected int encryptBlockSize = -1;
	/** 解密的块大小 */
	protected int decryptBlockSize = -1;

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，创建新的私钥公钥对
	 * 
	 * @param algorithm {@link SymmetricAlgorithm}
	 */
	public AsymmetricCrypto(AsymmetricAlgorithm algorithm) {
		this(algorithm, (byte[]) null, (byte[]) null);
	}

	/**
	 * 构造，创建新的私钥公钥对
	 * 
	 * @param algorithm 算法
	 */
	public AsymmetricCrypto(String algorithm) {
		this(algorithm, (byte[]) null, (byte[]) null);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param algorithm {@link SymmetricAlgorithm}
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64 公钥Base64
	 */
	public AsymmetricCrypto(AsymmetricAlgorithm algorithm, String privateKeyBase64, String publicKeyBase64) {
		this(algorithm.getValue(), Base64.decode(privateKeyBase64), Base64.decode(publicKeyBase64));
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param algorithm {@link SymmetricAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public AsymmetricCrypto(AsymmetricAlgorithm algorithm, byte[] privateKey, byte[] publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param algorithm {@link SymmetricAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @since 3.1.1
	 */
	public AsymmetricCrypto(AsymmetricAlgorithm algorithm, PrivateKey privateKey, PublicKey publicKey) {
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
	public AsymmetricCrypto(String algorithm, String privateKeyBase64, String publicKeyBase64) {
		super(algorithm, privateKeyBase64, publicKeyBase64);
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
	public AsymmetricCrypto(String algorithm, byte[] privateKey, byte[] publicKey) {
		super(algorithm, privateKey, publicKey);
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
	 * 初始化<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @return {@link AsymmetricCrypto}
	 */
	@Override
	public AsymmetricCrypto init(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		try {
			this.clipher = Cipher.getInstance(algorithm);
		} catch (Exception e) {
			throw new CryptoException(e);
		}

		super.init(algorithm, privateKey, publicKey);
		return this;
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
		final Key key = getKeyByType(keyType);
		final int inputLen = data.length;
		final int maxBlockSize = this.encryptBlockSize < 0 ? inputLen : this.encryptBlockSize;

		lock.lock();
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			clipher.init(Cipher.ENCRYPT_MODE, key);
			int offSet = 0;
			byte[] cache;
			// 剩余长度
			int remainLength = inputLen;
			// 对数据分段加密
			while (remainLength > 0) {
				cache = clipher.doFinal(data, offSet, Math.min(remainLength, maxBlockSize));
				out.write(cache, 0, cache.length);

				offSet += maxBlockSize;
				remainLength = inputLen - offSet;
			}
			return out.toByteArray();
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 编码为Hex字符串
	 * 
	 * @param data 被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 */
	public String encryptHex(byte[] data, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 * 
	 * @param data 被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	public String encryptBase64(byte[] data, KeyType keyType) {
		return Base64.encode(encrypt(data, keyType));
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
	 * 加密
	 * 
	 * @param data 被加密的字符串
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data, Charset charset, KeyType keyType) {
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
	 * 编码为Hex字符串
	 * 
	 * @param data 被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 * @since 4.0.1
	 */
	public String encryptHex(String data, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Hex字符串
	 * 
	 * @param data 被加密的bytes
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 * @since 4.0.1
	 */
	public String encryptHex(String data, Charset charset, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, charset, keyType));
	}

	/**
	 * 编码为Base64字符串
	 * 
	 * @param data 被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	public String encryptBase64(String data, KeyType keyType) {
		return Base64.encode(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 * 
	 * @param data 被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	public String encryptBase64(String data, Charset charset, KeyType keyType) {
		return Base64.encode(encrypt(data, charset, keyType));
	}

	/**
	 * 加密
	 * 
	 * @param data 被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	public byte[] encrypt(InputStream data, KeyType keyType) throws IORuntimeException {
		return encrypt(IoUtil.readBytes(data), keyType);
	}

	/**
	 * 编码为Hex字符串
	 * 
	 * @param data 被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 * @since 4.0.1
	 */
	public String encryptHex(InputStream data, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 * 
	 * @param data 被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	public String encryptBase64(InputStream data, KeyType keyType) {
		return Base64.encode(encrypt(data, keyType));
	}

	/**
	 * 分组加密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @return 加密后的密文
	 * @throws CryptoException 加密异常
	 * @since 4.1.0
	 */
	public String encryptBcd(String data, KeyType keyType) {
		return encryptBcd(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组加密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 加密后的密文
	 * @throws CryptoException 加密异常
	 * @since 4.1.0
	 */
	public String encryptBcd(String data, KeyType keyType, Charset charset) {
		return BCD.bcdToStr(encrypt(data, charset, keyType));
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
		final Key key = getKeyByType(keyType);
		// 模长
		final int inputLen = bytes.length;
		final int maxBlockSize = this.decryptBlockSize < 0 ? inputLen : this.decryptBlockSize;

		lock.lock();
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			clipher.init(Cipher.DECRYPT_MODE, key);
			int offSet = 0;
			byte[] cache;
			// 剩余长度
			int remainLength = inputLen;
			// 对数据分段解密
			while (remainLength > 0) {
				cache = clipher.doFinal(bytes, offSet, Math.min(remainLength, maxBlockSize));
				out.write(cache, 0, cache.length);

				offSet += maxBlockSize;
				remainLength = inputLen - offSet;
			}
			return out.toByteArray();
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
	 * @throws IORuntimeException IO异常
	 */
	public byte[] decrypt(InputStream data, KeyType keyType) throws IORuntimeException {
		return decrypt(IoUtil.readBytes(data), keyType);
	}

	/**
	 * 从Hex字符串解密
	 * 
	 * @param hexStr Hex字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 * @since 4.0.1
	 */
	public byte[] decryptFromHex(String hexStr, KeyType keyType) {
		return decrypt(HexUtil.decodeHex(hexStr), keyType);
	}

	/**
	 * 从Base64字符串解密
	 * 
	 * @param base64Str Base64字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 * @since 4.0.1
	 */
	public byte[] decryptFromBase64(String base64Str, KeyType keyType) {
		return decrypt(Base64.decode(base64Str, CharsetUtil.CHARSET_UTF_8), keyType);
	}

	/**
	 * 解密BCD
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @return 解密后的密文
	 * @since 4.1.0
	 */
	public byte[] decryptFromBcd(String data, KeyType keyType) {
		return decryptFromBcd(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组解密
	 * 
	 * @param data 数据
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 * @since 4.1.0
	 */
	public byte[] decryptFromBcd(String data, KeyType keyType, Charset charset) {
		final byte[] dataBytes = BCD.ascToBcd(StrUtil.bytes(data, charset));
		return decrypt(dataBytes, keyType);
	}

	// --------------------------------------------------------------------------------- Getters and Setters

	/**
	 * 获得加密或解密器
	 * 
	 * @return 加密或解密
	 */
	public Cipher getClipher() {
		return clipher;
	}
}
