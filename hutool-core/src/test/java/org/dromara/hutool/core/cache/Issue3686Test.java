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

public class Issue3686Test {
	public static void main(final String[] args) {
//		final LRUCache<Long, Integer> objects = CacheUtil.newLRUCache(20, TimeUnit.SECONDS.toMillis(30));
//		final List<Thread> list = new ArrayList<>();
//		for (int i = 0; i < 10; i++) {
//			final Thread thread = new Thread(() -> {
//				while (true) {
//					for (int i1 = 0; i1 < 100; i1++) {
//						final int finalI = i1;
//						objects.get((long) i1, () -> finalI);
//					}
//					ThreadUtil.sleep(500);
//				}
//			});
//			list.add(thread);
//		}
//		for (final Thread thread : list) {
//			thread.start();
//		}
	}
}
