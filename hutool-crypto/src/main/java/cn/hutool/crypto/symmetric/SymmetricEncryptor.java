package cn.hutool.crypto.symmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 对称加密器接口，提供：
 * <ul>
 *     <li>加密为bytes</li>
 *     <li>加密为Hex(16进制)</li>
 *     <li>加密为Base64</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface SymmetricEncryptor {

	/**
	 * 加密
	 *
	 * @param data 被加密的bytes
	 * @return 加密后的bytes
	 */
	byte[] encrypt(byte[] data);

	/**
	 * 加密，针对大数据量，可选结束后是否关闭流
	 *
	 * @param data    被加密的字符串
	 * @param out     输出流，可以是文件或网络位置
	 * @param isClose 是否关闭流
	 * @throws IORuntimeException IO异常
	 */
	void encrypt(InputStream data, OutputStream out, boolean isClose);

	/**
	 * 加密
	 *
	 * @param data 数据
	 * @return 加密后的Hex
	 */
	default String encryptHex(byte[] data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data 数据
	 * @return 加密后的Base64
	 */
	default String encryptBase64(byte[] data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(String data, String charset) {
		return encrypt(StrUtil.bytes(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(String data, Charset charset) {
		return encrypt(StrUtil.bytes(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Hex
	 */
	default String encryptHex(String data, String charset) {
		return HexUtil.encodeHexStr(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Hex
	 */
	default String encryptHex(String data, Charset charset) {
		return HexUtil.encodeHexStr(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Base64
	 */
	default String encryptBase64(String data, String charset) {
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
	default String encryptBase64(String data, Charset charset) {
		return Base64.encode(encrypt(data, charset));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(String data) {
		return encrypt(StrUtil.bytes(data, CharsetUtil.CHARSET_UTF_8));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Hex
	 */
	default String encryptHex(String data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 */
	default String encryptBase64(String data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 加密，加密后关闭流
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] encrypt(InputStream data) throws IORuntimeException {
		return encrypt(IoUtil.readBytes(data));
	}

	/**
	 * 加密
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Hex
	 */
	default String encryptHex(InputStream data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 */
	default String encryptBase64(InputStream data) {
		return Base64.encode(encrypt(data));
	}
}
