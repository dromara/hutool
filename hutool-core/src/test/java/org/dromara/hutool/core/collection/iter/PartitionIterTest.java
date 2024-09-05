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
