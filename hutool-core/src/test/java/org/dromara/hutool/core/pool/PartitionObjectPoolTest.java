/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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
			final StringBuilder obj = pool.borrowObject();
			obj.append("hutool");
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

		final ArrayList<StringBuilder> borrowed = ListUtil.of();
		for (int i = 0; i < 10; i++) {
			final StringBuilder obj = pool.borrowObject();
			obj.append("hutool");
			borrowed.add(obj);
		}
		// 池1的2个+池2借出的10个，合计12个
		Assertions.assertEquals(12, pool.getTotal());
		Assertions.assertEquals(10, pool.getActiveCount());
		Assertions.assertEquals(2, pool.getIdleCount());

		// 全部归还
		for (final StringBuilder obj : borrowed) {
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
