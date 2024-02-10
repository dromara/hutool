/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.symmetric;

import org.dromara.hutool.core.codec.binary.HexUtil;
import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;

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
	default String encryptHex(final byte[] data) {
		return HexUtil.encodeStr(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data 数据
	 * @return 加密后的Base64
	 */
	default String encryptBase64(final byte[] data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(final String data, final Charset charset) {
		return encrypt(ByteUtil.toBytes(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Hex
	 */
	default String encryptHex(final String data, final Charset charset) {
		return HexUtil.encodeStr(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Base64
	 * @since 4.5.12
	 */
	default String encryptBase64(final String data, final Charset charset) {
		return Base64.encode(encrypt(data, charset));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(final String data) {
		return encrypt(ByteUtil.toBytes(data, CharsetUtil.UTF_8));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Hex
	 */
	default String encryptHex(final String data) {
		return HexUtil.encodeStr(encrypt(data));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 */
	default String encryptBase64(final String data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 加密，加密后关闭流
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] encrypt(final InputStream data) throws IORuntimeException {
		return encrypt(IoUtil.readBytes(data));
	}

	/**
	 * 加密
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Hex
	 */
	default String encryptHex(final InputStream data) {
		return HexUtil.encodeStr(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 */
	default String encryptBase64(final InputStream data) {
		return Base64.encode(encrypt(data));
	}
}
