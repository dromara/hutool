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

package org.dromara.hutool.core.collection.iter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * test for {@link CopiedIter}
 */
public class CopiedIterTest {

	@Test
	public void copyOf() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Iterator<Integer> iter = list.iterator();
		Assertions.assertEquals((Integer)1, iter.next());

		Assertions.assertEquals((Integer)2, CopiedIter.copyOf(iter).next());
	}

	@Test
	public void hasNext() {
		Assertions.assertTrue(CopiedIter.copyOf(Arrays.asList(1, 2, 3).iterator()).hasNext());
		Assertions.assertFalse(CopiedIter.copyOf(Collections.emptyIterator()).hasNext());
	}

	@Test
	public void next() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Iterator<Integer> iter = CopiedIter.copyOf(list.iterator());
		Assertions.assertEquals((Integer)1, iter.next());
		Assertions.assertEquals((Integer)2, iter.next());
		Assertions.assertEquals((Integer)3, iter.next());
	}

	@Test
	public void remove() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Iterator<Integer> iter = CopiedIter.copyOf(list.iterator());
		Assertions.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

}
