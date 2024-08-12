/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.crypto.digest.mac;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.crypto.CryptoException;

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
