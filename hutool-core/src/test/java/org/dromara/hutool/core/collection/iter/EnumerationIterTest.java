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
