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

import org.bouncycastle.crypto.BufferedBlockCipher;

import java.util.Arrays;

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
	 * @return 块大小，-1表示非块加密
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
	 * 根据输入长度，获取输出长度，输出长度与算法相关<br>
	 * 输出长度只针对本次输入关联，即len长度的数据对应输出长度加doFinal的长度
	 *
	 * @param len 输入长度
	 * @return 输出长度，-1表示非块加密
	 */
	int getOutputSize(int len);

	/**
	 * 执行运算，可以是加密运算或解密运算
	 *
	 * @param in     输入数据
	 * @param inOff  输入数据开始位置
	 * @param len    被处理数据长度
	 * @param out    输出数据
	 * @param outOff 输出数据开始位置
	 * @return 处理长度
	 */
	int process(byte[] in, int inOff, int len, byte[] out, int outOff);

	/**
	 * 处理最后一块数据<br>
	 * 当{@link #process(byte[], int, int, byte[], int)}处理完数据后非完整块数据，此方法用于处理块中剩余的bytes<br>
	 * 如加密数据要求128bit，即16byes的整数，单数处理数据后为15bytes，此时根据padding方式不同，可填充剩余1byte为指定值（如填充0）<br>
	 * 当对数据进行分段加密时，需要首先多次执行process方法，在最后一块数据处理完后执行此方法。
	 *
	 * @param out    经过process执行过运算的结果数据
	 * @param outOff 数据处理开始位置
	 * @return 处理长度
	 */
	int doFinal(byte[] out, int outOff);

	/**
	 * 处理数据，并返回最终结果
	 *
	 * @param in 输入数据
	 * @return 结果数据
	 */
	default byte[] processFinal(final byte[] in) {
		final byte[] buf = new byte[getOutputSize(in.length)];
		int len = process(in, 0, in.length, buf, 0);
		len += doFinal(buf, len);

		if (len == buf.length) {
			return buf;
		}
		return Arrays.copyOfRange(buf, 0, len);
	}

	/**
	 * Cipher所需参数，包括Key、Random、IV等信息
	 */
	interface Parameters {
	}
}
