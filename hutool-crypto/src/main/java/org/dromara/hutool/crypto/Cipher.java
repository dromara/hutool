/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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
 * 密码接口，提供统一的API，用于兼容和统一JCE和BouncyCastle等库的操作
 *
 * @author Looly
 * @since 6.0.0
 */
public interface Cipher {

	/**
	 * 获取算法名称
	 *
	 * @return 算法名称
	 */
	String getAlgorithmName();

	/**
	 * 获取块大小，当为Stream方式加密时返回0
	 *
	 * @return 块大小
	 */
	int getBlockSize();

	/**
	 * 初始化模式和参数
	 *
	 * @param mode       模式，如加密模式或解密模式
	 * @param parameters Cipher所需参数，包括Key、Random、IV等信息
	 */
	void init(CipherMode mode, Parameters parameters);

	/**
	 * 执行运算，可以是加密运算或解密运算
	 *
	 * @param data 被处理的数据
	 * @return 运算结果
	 */
	byte[] process(byte[] data);

	/**
	 * Cipher所需参数，包括Key、Random、IV等信息
	 */
	interface Parameters { }
}
