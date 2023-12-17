/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.pool;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.pool.partition.PartitionObjectPool;
import org.dromara.hutool.core.pool.partition.PartitionPoolConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PartitionObjectPoolTest {

	public ObjectPool<StringBuilder> init() {
		final PartitionPoolConfig config = (PartitionPoolConfig) PartitionPoolConfig.of()
			.setPartitionSize(2)
			.setMinSize(2)
			.setMaxSize(20)
			.setMaxIdle(5000)
			.setMaxWait(100);

		final ObjectFactory<StringBuilder> factory = new ObjectFactory<StringBuilder>() {
			@Override
			public StringBuilder create() {
				return new StringBuilder();
			}

			@Override
			public void destroy(final StringBuilder o) {
			}

			@Override
			public boolean validate(final StringBuilder o) {
				return true;
			}
		};
		return new PartitionObjectPool<>(config, factory);
	}

	@Test
	void borrowObjectTest() {
		final ObjectPool<StringBuilder> pool = init();
		// 初始4个对象
		Assertions.assertEquals(4, pool.getTotal());

		for (int i = 0; i < 20; i++) {
			final Poolable<StringBuilder> obj = pool.borrowObject();
			obj.getRaw().append("hutool");
		}
		// 池1的2个+池2借出的20个，合计22个
		Assertions.assertEquals(22, pool.getTotal());
		Assertions.assertEquals(20, pool.getActiveCount());
		Assertions.assertEquals(2, pool.getIdleCount());

		IoUtil.closeQuietly(pool);
	}

	@Test
	void borrowAndReturnObjectTest() {
		final ObjectPool<StringBuilder> pool = init();
		// 初始4个对象
		Assertions.assertEquals(4, pool.getTotal());

		final ArrayList<Poolable<StringBuilder>> borrowed = ListUtil.of();
		for (int i = 0; i < 10; i++) {
			final Poolable<StringBuilder> obj = pool.borrowObject();
			obj.getRaw().append("hutool");
			borrowed.add(obj);
		}
		// 池1的2个+池2借出的10个，合计12个
		Assertions.assertEquals(12, pool.getTotal());
		Assertions.assertEquals(10, pool.getActiveCount());
		Assertions.assertEquals(2, pool.getIdleCount());

		// 全部归还
		for (final Poolable<StringBuilder> obj : borrowed) {
			pool.returnObject(obj);
		}

		// 池1的2个+池2的10个，合计12个
		Assertions.assertEquals(12, pool.getTotal());
		Assertions.assertEquals(0, pool.getActiveCount());
		// 全部空闲
		Assertions.assertEquals(12, pool.getIdleCount());

		IoUtil.closeQuietly(pool);
	}
}
