package cn.hutool.crypto.asymmetric;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;

public abstract class AbstractAsymmetricCrypto<T extends AbstractAsymmetricCrypto<T>> extends BaseAsymmetric<T> {
	// ------------------------------------------------------------------ Constructor start
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
	public AbstractAsymmetricCrypto(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		super(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	// --------------------------------------------------------------------------------- Encrypt
	/**
	 * 加密
	 * 
	 * @param data 被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	public abstract byte[] encrypt(byte[] data, KeyType keyType);

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
	public abstract byte[] decrypt(byte[] bytes, KeyType keyType);

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
}
