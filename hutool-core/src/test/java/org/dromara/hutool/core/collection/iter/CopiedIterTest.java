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
