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

package org.dromara.hutool.crypto;

/**
 * 模式
 *
 * <p>
 * 加密算法模式，是用来描述加密算法（此处特指分组密码，不包括流密码）在加密时对明文分组的模式，它代表了不同的分组方式
 *
 * @author Looly
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher"> Cipher章节</a>
 * @since 3.0.8
 */
public enum Mode {
	/**
	 * 无模式
	 */
	NONE,
	/**
	 * 密码分组连接模式（Cipher Block Chaining）
	 */
	CBC,
	/**
	 * 密文反馈模式（Cipher Feedback）
	 */
	CFB,
	/**
	 * 计数器模式（A simplification of OFB）
	 */
	CTR,
	/**
	 * Cipher Text Stealing
	 */
	CTS,
	/**
	 * 电子密码本模式（Electronic CodeBook）
	 */
	ECB,
	/**
	 * 输出反馈模式（Output Feedback）
	 */
	OFB,
	/**
	 * Propagating Cipher Block
	 */
	PCBC,
	/**
	 * GCM 全称为 Galois/Counter Mode。G是指GMAC，C是指CTR。
	 * 它在 CTR 加密的基础上增加 GMAC 的特性，解决了 CTR 不能对加密消息进行完整性校验的问题。
	 */
	GCM
}
