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

package org.dromara.hutool.core.comparator;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * https://gitee.com/dromara/hutool/pulls/1240
 */
public class IndexedComparatorTest {
	@Test
	public void sortTest() {
		final Object[] arr = {"a", "b", new User("9", null), "1", 3, null, "2"};
		final Collection<Object> set = new HashSet<>(Arrays.asList(arr));


		final List<Object> sortSet = CollUtil.sort(set, new ArrayIndexedComparator<>(arr));

		assertEquals("a", sortSet.get(0));
		assertEquals(new User("9", null), sortSet.get(2));
		assertEquals(3, sortSet.get(4));
		assertNull(sortSet.get(5));
	}

	@Test
	public void reversedTest() {
		final Object[] arr = {"a", "b", new User("9", null), "1", 3, null, "2"};
		final Collection<Object> set = new HashSet<>(Arrays.asList(arr));

		final List<Object> sortSet = CollUtil.sort(set, new ArrayIndexedComparator<>(arr).reversed());

		assertEquals("a", sortSet.get(6));
		assertNull(sortSet.get(1));
		assertEquals(new User("9", null), sortSet.get(4));
		assertEquals(3, sortSet.get(2));
	}

	@Test
	@Disabled
	public void benchmarkSortTest() {
		final Object[] arr = {"a", "b", new User("9", null), "1", 3, null, "2"};
		final Collection<Object> set = new HashSet<>(Arrays.asList(arr));

		final StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		for (int i = 0; i < 10_000_000; i++) {
			CollUtil.sort(set, new IndexedComparator<>(arr));
		}
		stopWatch.stop();


		stopWatch.start();
		for (int i = 0; i < 10_000_000; i++) {
			CollUtil.sort(set, new ArrayIndexedComparator<>(arr));
		}
		stopWatch.stop();
		Console.log(stopWatch.prettyPrint());
	}

	@Data
	@AllArgsConstructor
	static class User {
		private String a;
		private String b;
	}
}
