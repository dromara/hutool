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
