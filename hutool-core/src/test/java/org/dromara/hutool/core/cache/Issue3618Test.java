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

package org.dromara.hutool.core.cache;

import org.dromara.hutool.core.cache.impl.FIFOCache;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue3618Test {
	@Test
	public void putTest() {
		final FIFOCache<Object, Object> cache = CacheUtil.newFIFOCache(3);
		cache.put(1, 1);
		cache.put(2, 1);
		cache.put(3, 1);

		assertEquals(3, cache.size());

		// issue#3618 对于替换的键值对，不做满队列检查和清除
		cache.put(3, 2);

		assertEquals(3, cache.size());
	}
}
