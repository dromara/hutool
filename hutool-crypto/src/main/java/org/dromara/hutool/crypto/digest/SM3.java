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

package org.dromara.hutool.crypto.digest;

/**
 * 国密SM3杂凑（摘要）算法
 *
 * <p>
 * 国密算法包括：
 * <ol>
 *     <li>非对称加密和签名：SM2，asymmetric</li>
 *     <li>摘要签名算法：SM3，digest</li>
 *     <li>对称加密：SM4，symmetric</li>
 * </ol>
 *
 * @author looly
 * @since 4.6.8
 */
public class SM3 extends Digester {
	private static final long serialVersionUID = 1L;

	/**
	 * 算法名称：SM3
	 */
	public static final String ALGORITHM_NAME = "SM3";

	/**
	 * 创建SM3实例
	 *
	 * @return SM3
	 * @since 4.6.0
	 */
	public static SM3 of() {
		return new SM3();
	}

	/**
	 * 构造
	 */
	public SM3() {
		super(ALGORITHM_NAME);
	}

	/**
	 * 构造
	 *
	 * @param salt 盐值
	 */
	public SM3(final byte[] salt) {
		this(salt, 0, 1);
	}

	/**
	 * 构造
	 *
	 * @param salt        盐值
	 * @param digestCount 摘要次数，当此值小于等于1,默认为1。
	 */
	public SM3(final byte[] salt, final int digestCount) {
		this(salt, 0, digestCount);
	}

	/**
	 * 构造
	 *
	 * @param salt         盐值
	 * @param saltPosition 加盐位置，即将盐值字符串放置在数据的index数，默认0
	 * @param digestCount  摘要次数，当此值小于等于1,默认为1。
	 */
	public SM3(final byte[] salt, final int saltPosition, final int digestCount) {
		this();
		this.salt = salt;
		this.saltPosition = saltPosition;
		this.digestCount = digestCount;
	}
}
