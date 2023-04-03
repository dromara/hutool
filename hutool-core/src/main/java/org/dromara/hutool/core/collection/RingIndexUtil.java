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

package org.dromara.hutool.core.collection;

import org.dromara.hutool.core.lang.Assert;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 集合索引环形获取工具类
 *
 * @author ZhouChuGang
 * @since 5.7.15
 */
public class RingIndexUtil {

	/**
	 * 通过cas操作 实现对指定值内的回环累加
	 *
	 * @param object        集合
	 *                      <ul>
	 *                      <li>Collection - the collection size
	 *                      <li>Map - the map size
	 *                      <li>Array - the array size
	 *                      <li>Iterator - the number of elements remaining in the iterator
	 *                      <li>Enumeration - the number of elements remaining in the enumeration
	 *                      </ul>
	 * @param atomicInteger 原子操作类
	 * @return 索引位置
	 */
	public static int ringNextIntByObj(final Object object, final AtomicInteger atomicInteger) {
		Assert.notNull(object);
		final int modulo = CollUtil.size(object);
		return ringNextInt(modulo, atomicInteger);
	}

	/**
	 * 通过cas操作 实现对指定值内的回环累加
	 *
	 * @param modulo        回环周期值
	 * @param atomicInteger 原子操作类
	 * @return 索引位置
	 */
	public static int ringNextInt(final int modulo, final AtomicInteger atomicInteger) {
		Assert.notNull(atomicInteger);
		Assert.isTrue(modulo > 0);
		if (modulo <= 1) {
			return 0;
		}
		for (; ; ) {
			final int current = atomicInteger.get();
			final int next = (current + 1) % modulo;
			if (atomicInteger.compareAndSet(current, next)) {
				return next;
			}
		}
	}

	/**
	 * 通过cas操作 实现对指定值内的回环累加<br>
	 * 此方法一般用于大量数据完成回环累加（如数据库中的值大于int最大值）
	 *
	 * @param modulo     回环周期值
	 * @param atomicLong 原子操作类
	 * @return 索引位置
	 */
	public static long ringNextLong(final long modulo, final AtomicLong atomicLong) {
		Assert.notNull(atomicLong);
		Assert.isTrue(modulo > 0);
		if (modulo <= 1) {
			return 0;
		}
		for (; ; ) {
			final long current = atomicLong.get();
			final long next = (current + 1) % modulo;
			if (atomicLong.compareAndSet(current, next)) {
				return next;
			}
		}
	}
}
