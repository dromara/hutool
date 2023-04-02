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

package org.dromara.hutool.codec.hash.metro;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Apache 发布的MetroHash算法接口，是一组用于非加密用例的最先进的哈希函数。
 * 除了卓越的性能外，他们还以算法生成而著称。
 *
 * <p>
 * 官方实现：https://github.com/jandrewrogers/MetroHash
 * 官方文档：http://www.jandrewrogers.com/2015/05/27/metrohash/
 * 来自：https://github.com/postamar/java-metrohash/
 *
 * @param <R> 返回值类型，为this类型
 * @author Marius Posta
 */
public interface MetroHash<R extends MetroHash<R>> {

	/**
	 * 创建 {@code MetroHash}对象
	 *
	 * @param seed  种子
	 * @param is128 是否128位
	 * @return {@code MetroHash}对象
	 */
	static MetroHash<?> of(final long seed, final boolean is128) {
		return is128 ? new MetroHash128(seed) : new MetroHash64(seed);
	}

	/**
	 * 将给定的{@link ByteBuffer}中的数据追加计算hash值<br>
	 * 此方法会更新hash值状态
	 *
	 * @param input 内容
	 * @return this
	 */
	R apply(final ByteBuffer input);

	/**
	 * 将结果hash值写出到{@link ByteBuffer}中，可选端序
	 *
	 * @param output    输出
	 * @param byteOrder 端序
	 * @return this
	 */
	R write(ByteBuffer output, final ByteOrder byteOrder);

	/**
	 * 重置，重置后可复用对象开启新的计算
	 *
	 * @return this
	 */
	R reset();
}
