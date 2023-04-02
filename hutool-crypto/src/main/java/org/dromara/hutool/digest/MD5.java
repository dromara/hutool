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

package org.dromara.hutool.digest;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * MD5算法
 *
 * @author looly
 * @since 4.4.3
 */
public class MD5 extends Digester {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建MD5实例
	 *
	 * @return MD5
	 * @since 4.6.0
	 */
	public static MD5 of() {
		return new MD5();
	}

	/**
	 * 构造
	 */
	public MD5() {
		super(DigestAlgorithm.MD5);
	}

	/**
	 * 构造
	 *
	 * @param salt 盐值
	 */
	public MD5(final byte[] salt) {
		this(salt, 0, 1);
	}

	/**
	 * 构造
	 *
	 * @param salt 盐值
	 * @param digestCount 摘要次数，当此值小于等于1,默认为1。
	 */
	public MD5(final byte[] salt, final int digestCount) {
		this(salt, 0, digestCount);
	}

	/**
	 * 构造
	 *
	 * @param salt 盐值
	 * @param saltPosition 加盐位置，即将盐值字符串放置在数据的index数，默认0
	 * @param digestCount 摘要次数，当此值小于等于1,默认为1。
	 */
	public MD5(final byte[] salt, final int saltPosition, final int digestCount) {
		this();
		this.salt = salt;
		this.saltPosition = saltPosition;
		this.digestCount = digestCount;
	}

	/**
	 * 生成16位MD5摘要
	 *
	 * @param data 数据
	 * @param charset 编码
	 * @return 16位MD5摘要
	 * @since 4.6.0
	 */
	public String digestHex16(final String data, final Charset charset) {
		return DigestUtil.md5HexTo16(digestHex(data, charset));
	}

	/**
	 * 生成16位MD5摘要
	 *
	 * @param data 数据
	 * @return 16位MD5摘要
	 * @since 4.5.1
	 */
	public String digestHex16(final String data) {
		return DigestUtil.md5HexTo16(digestHex(data));
	}

	/**
	 * 生成16位MD5摘要
	 *
	 * @param data 数据
	 * @return 16位MD5摘要
	 * @since 4.5.1
	 */
	public String digestHex16(final InputStream data) {
		return DigestUtil.md5HexTo16(digestHex(data));
	}

	/**
	 * 生成16位MD5摘要
	 *
	 * @param data 数据
	 * @return 16位MD5摘要
	 */
	public String digestHex16(final File data) {
		return DigestUtil.md5HexTo16(digestHex(data));
	}

	/**
	 * 生成16位MD5摘要
	 *
	 * @param data 数据
	 * @return 16位MD5摘要
	 * @since 4.5.1
	 */
	public String digestHex16(final byte[] data) {
		return DigestUtil.md5HexTo16(digestHex(data));
	}
}
