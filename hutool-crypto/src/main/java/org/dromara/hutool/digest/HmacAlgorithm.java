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

/**
 * HMAC算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Mac
 *
 * @author Looly
 */
public enum HmacAlgorithm {
	HmacMD5("HmacMD5"),
	HmacSHA1("HmacSHA1"),
	HmacSHA256("HmacSHA256"),
	HmacSHA384("HmacSHA384"),
	HmacSHA512("HmacSHA512"),
	/** HmacSM3算法实现，需要BouncyCastle库支持 */
	HmacSM3("HmacSM3"),
	/** SM4 CMAC模式实现，需要BouncyCastle库支持 */
	SM4CMAC("SM4CMAC");

	private final String value;

	HmacAlgorithm(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
