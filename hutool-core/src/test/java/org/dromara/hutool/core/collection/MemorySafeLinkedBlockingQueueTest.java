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

package org.dromara.hutool.core.collection;

import org.dromara.hutool.core.collection.queue.MemorySafeLinkedBlockingQueue;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemorySafeLinkedBlockingQueueTest {

	@Test
	public void offerTest(){
		// 设置初始值达到最大，这样任何时候元素都无法加入队列
		final MemorySafeLinkedBlockingQueue<String> queue = new MemorySafeLinkedBlockingQueue<>(Long.MAX_VALUE);
		Assertions.assertFalse(queue.offer(RandomUtil.randomStringLower(RandomUtil.randomInt(100))));

		// 设定一个很小的值，可以成功加入
		queue.setMaxFreeMemory(10);
		Assertions.assertTrue(queue.offer(RandomUtil.randomStringLower(RandomUtil.randomInt(100))));
	}
}
