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
