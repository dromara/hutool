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
import java.util.List;

/**
 * test for {@link IteratorEnumeration}
 */
public class IteratorEnumerationTest {

	@Test
	public void testHasMoreElements() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		IteratorEnumeration<Integer> enumeration = new IteratorEnumeration<>(list.iterator());
		Assertions.assertTrue(enumeration.hasMoreElements());
		Assertions.assertFalse(new IteratorEnumeration<>(Collections.emptyIterator()).hasMoreElements());
	}

	@Test
	public void testNextElement() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		IteratorEnumeration<Integer> enumeration = new IteratorEnumeration<>(list.iterator());
		Assertions.assertEquals((Integer)1, enumeration.nextElement());
		Assertions.assertEquals((Integer)2, enumeration.nextElement());
		Assertions.assertEquals((Integer)3, enumeration.nextElement());
	}

}
