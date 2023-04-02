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

package org.dromara.hutool.symmetric;

import org.dromara.hutool.codec.HexUtil;
import org.dromara.hutool.KeyUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;

/**
 * PBKDF2应用一个伪随机函数以导出密钥，PBKDF2简单而言就是将salted hash进行多次重复计算。
 * 参考：https://blog.csdn.net/huoji555/article/details/83659687
 *
 * @author looly
 */
public class PBKDF2 {

	private String algorithm = "PBKDF2WithHmacSHA1";
	//生成密文的长度
	private int keyLength = 512;

	//迭代次数
	private int iterationCount = 1000;

	/**
	 * 构造，算法PBKDF2WithHmacSHA1，盐长度16，密文长度512，迭代次数1000
	 */
	public PBKDF2() {
	}

	/**
	 * 构造
	 *
	 * @param algorithm      算法，一般为PBKDF2WithXXX
	 * @param keyLength      生成密钥长度，默认512
	 * @param iterationCount 迭代次数，默认1000
	 */
	public PBKDF2(final String algorithm, final int keyLength, final int iterationCount) {
		this.algorithm = algorithm;
		this.keyLength = keyLength;
		this.iterationCount = iterationCount;
	}

	/**
	 * 加密
	 *
	 * @param password 密码
	 * @param salt     盐
	 * @return 加密后的密码
	 */
	public byte[] encrypt(final char[] password, final byte[] salt) {
		final PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
		final SecretKey secretKey = KeyUtil.generateKey(algorithm, pbeKeySpec);
		return secretKey.getEncoded();
	}

	/**
	 * 加密
	 *
	 * @param password 密码
	 * @param salt     盐
	 * @return 加密后的密码
	 */
	public String encryptHex(final char[] password, final byte[] salt) {
		return HexUtil.encodeHexStr(encrypt(password, salt));
	}
}
