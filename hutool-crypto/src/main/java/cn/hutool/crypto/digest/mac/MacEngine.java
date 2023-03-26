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

package cn.hutool.crypto.digest.mac;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.CryptoException;

import java.io.IOException;
import java.io.InputStream;

/**
 * MAC（Message Authentication Code）算法引擎
 *
 * @author Looly
 * @since 4.5.13
 */
public interface MacEngine {

	/**
	 * 加入需要被摘要的内容
	 * @param in 内容
	 * @since 5.7.0
	 */
	default void update(final byte[] in){
		update(in, 0, in.length);
	}

	/**
	 * 加入需要被摘要的内容
	 * @param in 内容
	 * @param inOff 内容起始位置
	 * @param len 内容长度
	 * @since 5.7.0
	 */
	void update(byte[] in, int inOff, int len);

	/**
	 * 结束并生成摘要
	 *
	 * @return 摘要内容
	 * @since 5.7.0
	 */
	byte[] doFinal();

	/**
	 * 重置
	 * @since 5.7.0
	 */
	void reset();

	/**
	 * 生成摘要
	 *
	 * @param data {@link InputStream} 数据流
	 * @param bufferLength 缓存长度，不足1使用 {@link IoUtil#DEFAULT_BUFFER_SIZE} 做为默认值
	 * @return 摘要bytes
	 */
	default byte[] digest(final InputStream data, int bufferLength){
		if (bufferLength < 1) {
			bufferLength = IoUtil.DEFAULT_BUFFER_SIZE;
		}

		final byte[] buffer = new byte[bufferLength];

		byte[] result;
		try {
			int read = data.read(buffer, 0, bufferLength);

			while (read > -1) {
				update(buffer, 0, read);
				read = data.read(buffer, 0, bufferLength);
			}
			result = doFinal();
		} catch (final IOException e) {
			throw new CryptoException(e);
		} finally {
			reset();
		}
		return result;
	}

	/**
	 * 获取MAC算法块大小
	 *
	 * @return MAC算法块大小
	 */
	int getMacLength();

	/**
	 * 获取当前算法
	 *
	 * @return 算法
	 */
	String getAlgorithm();
}
