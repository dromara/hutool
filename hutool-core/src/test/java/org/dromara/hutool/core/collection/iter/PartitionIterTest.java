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

/**
 * test for {@link PartitionIter}
 */
public class PartitionIterTest {

	@Test
	public void testHasNext() {
		Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4).iterator();
		PartitionIter<Integer> partitionIter = new PartitionIter<>(iter, 2);
		Assertions.assertTrue(partitionIter.hasNext());
		Assertions.assertFalse(new PartitionIter<>(Collections.emptyIterator(), 1).hasNext());
	}

	@Test
	public void testNext() {
		Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4).iterator();
		PartitionIter<Integer> partitionIter = new PartitionIter<>(iter, 2);
		Assertions.assertEquals(Arrays.asList(1, 2), partitionIter.next());
		Assertions.assertEquals(Arrays.asList(3, 4), partitionIter.next());
	}

}
