package cn.hutool.crypto.asymmetric;

import cn.hutool.core.codec.HexUtil;
import cn.hutool.core.codec.binary.Base64;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ByteUtil;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 非对称加密器接口，提供：
 * <ul>
 *     <li>加密为bytes</li>
 *     <li>加密为Hex(16进制)</li>
 *     <li>加密为Base64</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface AsymmetricEncryptor {

	/**
	 * 加密
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	byte[] encrypt(byte[] data, KeyType keyType);

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 */
	default String encryptHex(final byte[] data, final KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	default String encryptBase64(final byte[] data, final KeyType keyType) {
		return Base64.encode(encrypt(data, keyType));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(final String data, final Charset charset, final KeyType keyType) {
		return encrypt(ByteUtil.toBytes(data, charset), keyType);
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(final String data, final KeyType keyType) {
		return encrypt(ByteUtil.toUtf8Bytes(data), keyType);
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 * @since 4.0.1
	 */
	default String encryptHex(final String data, final KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的bytes
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 * @since 4.0.1
	 */
	default String encryptHex(final String data, final Charset charset, final KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, charset, keyType));
	}

	/**
	 * 编码为Base64字符串，使用UTF-8编码
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	default String encryptBase64(final String data, final KeyType keyType) {
		return Base64.encode(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	default String encryptBase64(final String data, final Charset charset, final KeyType keyType) {
		return Base64.encode(encrypt(data, charset, keyType));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] encrypt(final InputStream data, final KeyType keyType) throws IORuntimeException {
		return encrypt(IoUtil.readBytes(data), keyType);
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 * @since 4.0.1
	 */
	default String encryptHex(final InputStream data, final KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 *
	 * @param data    被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	default String encryptBase64(final InputStream data, final KeyType keyType) {
		return Base64.encode(encrypt(data, keyType));
	}
}
