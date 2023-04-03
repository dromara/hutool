/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.symmetric;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.crypto.SecureUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 对称解密器接口，提供：
 * <ul>
 *     <li>从bytes解密</li>
 *     <li>从Hex(16进制)解密</li>
 *     <li>从Base64解密</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface SymmetricDecryptor {
	/**
	 * 解密
	 *
	 * @param bytes 被解密的bytes
	 * @return 解密后的bytes
	 */
	byte[] decrypt(byte[] bytes);

	/**
	 * 解密，针对大数据量，结束后不关闭流
	 *
	 * @param data    加密的字符串
	 * @param out     输出流，可以是文件或网络位置
	 * @param isClose 是否关闭流，包括输入和输出流
	 * @throws IORuntimeException IO异常
	 */
	void decrypt(InputStream data, OutputStream out, boolean isClose);

	/**
	 * 解密为字符串
	 *
	 * @param bytes   被解密的bytes
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	default String decryptStr(final byte[] bytes, final Charset charset) {
		return StrUtil.str(decrypt(bytes), charset);
	}

	/**
	 * 解密为字符串，默认UTF-8编码
	 *
	 * @param bytes 被解密的bytes
	 * @return 解密后的String
	 */
	default String decryptStr(final byte[] bytes) {
		return decryptStr(bytes, CharsetUtil.UTF_8);
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串
	 *
	 * @param data 被解密的String，必须为16进制字符串或Base64表示形式
	 * @return 解密后的bytes
	 */
	default byte[] decrypt(final String data) {
		return decrypt(SecureUtil.decode(data));
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串
	 *
	 * @param data    被解密的String
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	default String decryptStr(final String data, final Charset charset) {
		return StrUtil.str(decrypt(data), charset);
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串，默认UTF-8编码
	 *
	 * @param data 被解密的String
	 * @return 解密后的String
	 */
	default String decryptStr(final String data) {
		return decryptStr(data, CharsetUtil.UTF_8);
	}

	/**
	 * 解密，会关闭流
	 *
	 * @param data 被解密的bytes
	 * @return 解密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] decrypt(final InputStream data) throws IORuntimeException {
		return decrypt(IoUtil.readBytes(data));
	}

	/**
	 * 解密，不会关闭流
	 *
	 * @param data    被解密的InputStream
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	default String decryptStr(final InputStream data, final Charset charset) {
		return StrUtil.str(decrypt(data), charset);
	}

	/**
	 * 解密
	 *
	 * @param data 被解密的InputStream
	 * @return 解密后的String
	 */
	default String decryptStr(final InputStream data) {
		return decryptStr(data, CharsetUtil.UTF_8);
	}
}
