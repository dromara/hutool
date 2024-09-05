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

/**
 * test for {@link ArrayIter}
 */
public class ArrayIterTest {

	@Test
	public void testHasNext() {
		final Integer[] arr = new Integer[]{ 1, 2, 3 };
		final ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertTrue(iter.hasNext());
	}

	@Test
	public void testNext() {
		final Integer[] arr = new Integer[]{ 1, 2, 3 };
		final ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertEquals((Integer)1, iter.next());
		Assertions.assertEquals((Integer)2, iter.next());
		Assertions.assertEquals((Integer)3, iter.next());
	}

	@Test
	public void testRemove() {
		final Integer[] arr = new Integer[]{ 1, 2, 3 };
		final ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

	@Test
	public void testGetArray() {
		final Integer[] arr = new Integer[]{ 1, 2, 3 };
		final ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertEquals(arr, iter.getArray());
	}

	@Test
	public void testReset() {
		final Integer[] arr = new Integer[]{ 1, 2, 3 };
		final ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertEquals((Integer)1, iter.next());
		iter.reset();
		Assertions.assertEquals((Integer)1, iter.next());
	}

}
