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
import java.util.Enumeration;

/**
 * @author huangchengxing
 */
public class EnumerationIterTest {

	@Test
	public void testHasNext() {
		Enumeration<Integer> enumeration = new IteratorEnumeration<>(Arrays.asList(1, 2, 3).iterator());
		EnumerationIter<Integer> iter = new EnumerationIter<>(enumeration);
		Assertions.assertTrue(iter.hasNext());
		Assertions.assertFalse(new EnumerationIter<>(new IteratorEnumeration<>(Collections.emptyIterator())).hasNext());
	}

	@Test
	public void testNext() {
		Enumeration<Integer> enumeration = new IteratorEnumeration<>(Arrays.asList(1, 2, 3).iterator());
		EnumerationIter<Integer> iter = new EnumerationIter<>(enumeration);
		Assertions.assertEquals((Integer)1, iter.next());
		Assertions.assertEquals((Integer)2, iter.next());
		Assertions.assertEquals((Integer)3, iter.next());
	}

	@Test
	public void testRemove() {
		Enumeration<Integer> enumeration = new IteratorEnumeration<>(Arrays.asList(1, 2, 3).iterator());
		EnumerationIter<Integer> iter = new EnumerationIter<>(enumeration);
		Assertions.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

	@Test
	public void testIterator() {
		Enumeration<Integer> enumeration = new IteratorEnumeration<>(Arrays.asList(1, 2, 3).iterator());
		EnumerationIter<Integer> iter = new EnumerationIter<>(enumeration);
		Assertions.assertSame(iter, iter.iterator());
	}

}
