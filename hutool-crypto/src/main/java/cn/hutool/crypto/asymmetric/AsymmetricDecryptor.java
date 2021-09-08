package cn.hutool.crypto.asymmetric;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 非对称解密器接口，提供：
 * <ul>
 *     <li>从bytes解密</li>
 *     <li>从Hex(16进制)解密</li>
 *     <li>从Base64解密</li>
 *     <li>从BCD解密</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface AsymmetricDecryptor {

	/**
	 * 解密
	 *
	 * @param bytes   被解密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 */
	byte[] decrypt(byte[] bytes, KeyType keyType);

	/**
	 * 解密
	 *
	 * @param data    被解密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] decrypt(InputStream data, KeyType keyType) throws IORuntimeException {
		return decrypt(IoUtil.readBytes(data), keyType);
	}

	/**
	 * 从Hex或Base64字符串解密，编码为UTF-8格式
	 *
	 * @param data    Hex（16进制）或Base64字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 * @since 4.5.2
	 */
	default byte[] decrypt(String data, KeyType keyType) {
		return decrypt(SecureUtil.decode(data), keyType);
	}

	/**
	 * 解密为字符串，密文需为Hex（16进制）或Base64字符串
	 *
	 * @param data    数据，Hex（16进制）或Base64字符串
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 * @since 4.5.2
	 */
	default String decryptStr(String data, KeyType keyType, Charset charset) {
		return StrUtil.str(decrypt(data, keyType), charset);
	}

	/**
	 * 解密为字符串，密文需为Hex（16进制）或Base64字符串
	 *
	 * @param data    数据，Hex（16进制）或Base64字符串
	 * @param keyType 密钥类型
	 * @return 解密后的密文
	 * @since 4.5.2
	 */
	default String decryptStr(String data, KeyType keyType) {
		return decryptStr(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 解密BCD
	 *
	 * @param data    数据
	 * @param keyType 密钥类型
	 * @return 解密后的密文
	 * @since 4.1.0
	 */
	default byte[] decryptFromBcd(String data, KeyType keyType) {
		return decryptFromBcd(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组解密
	 *
	 * @param data    数据
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 * @since 4.1.0
	 */
	default byte[] decryptFromBcd(String data, KeyType keyType, Charset charset) {
		Assert.notNull(data, "Bcd string must be not null!");
		final byte[] dataBytes = BCD.ascToBcd(StrUtil.bytes(data, charset));
		return decrypt(dataBytes, keyType);
	}

	/**
	 * 解密为字符串，密文需为BCD格式
	 *
	 * @param data    数据，BCD格式
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 * @since 4.5.2
	 */
	default String decryptStrFromBcd(String data, KeyType keyType, Charset charset) {
		return StrUtil.str(decryptFromBcd(data, keyType, charset), charset);
	}

	/**
	 * 解密为字符串，密文需为BCD格式，编码为UTF-8格式
	 *
	 * @param data    数据，BCD格式
	 * @param keyType 密钥类型
	 * @return 解密后的密文
	 * @since 4.5.2
	 */
	default String decryptStrFromBcd(String data, KeyType keyType) {
		return decryptStrFromBcd(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}
}
