/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.codec.hash.metro;

import java.nio.ByteBuffer;

/**
 * Apache 发布的MetroHash算法抽象实现，是一组用于非加密用例的最先进的哈希函数。
 * 除了卓越的性能外，他们还以算法生成而著称。
 *
 * <p>
 * 官方实现：https://github.com/jandrewrogers/MetroHash
 * 官方文档：http://www.jandrewrogers.com/2015/05/27/metrohash/
 * 来自：https://github.com/postamar/java-metrohash/
 *
 * @author Marius Posta
 * @param <R> 返回值类型，为this类型
 */
public abstract class AbstractMetroHash<R extends AbstractMetroHash<R>> implements MetroHash<R> {

	final long seed;
	long v0, v1, v2, v3;
	long nChunks;

	/**
	 * 使用指定种子构造
	 *
	 * @param seed 种子
	 */
	public AbstractMetroHash(final long seed) {
		this.seed = seed;
		reset();
	}

	@Override
	public R apply(final ByteBuffer input) {
		reset();
		while (input.remaining() >= 32) {
			partialApply32ByteChunk(input);
		}
		return partialApplyRemaining(input);
	}

	/**
	 * 从byteBuffer中计算32-byte块并更新hash状态
	 *
	 * @param partialInput byte buffer，至少有32byte的数据
	 * @return this
	 */
	abstract R partialApply32ByteChunk(ByteBuffer partialInput);

	/**
	 * 从byteBuffer中计算剩余bytes并更新hash状态
	 *
	 * @param partialInput byte buffer，少于32byte的数据
	 * @return this
	 */
	abstract R partialApplyRemaining(ByteBuffer partialInput);

	static long grab(final ByteBuffer bb, final int length) {
		long result = bb.get() & 0xFFL;
		for (int i = 1; i < length; i++) {
			result |= (bb.get() & 0xFFL) << (i << 3);
		}
		return result;
	}

	static void writeLittleEndian(final long hash, final ByteBuffer output) {
		for (int i = 0; i < 8; i++) {
			output.put((byte) (hash >>> (i*8)));
		}
	}
}
