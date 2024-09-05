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

import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author huangchengxing
 */
public class TransIterTest {

	@Test
	public void testHasNext() {
		TransIter<Integer, String> iter = new TransIter<>(Arrays.asList(1, 2, 3).iterator(), String::valueOf);
		Assertions.assertTrue(iter.hasNext());
		Assertions.assertFalse(new TransIter<>(Collections.emptyIterator(), Function.identity()).hasNext());
	}

	@Test
	public void testNext() {
		TransIter<Integer, String> iter = new TransIter<>(Arrays.asList(1, 2, 3).iterator(), String::valueOf);
		Assertions.assertEquals("1", iter.next());
		Assertions.assertEquals("2", iter.next());
		Assertions.assertEquals("3", iter.next());
	}

	@Test
	public void testRemove() {
		List<Integer> list = ListUtil.of(1, 2, 3);
		TransIter<Integer, String> iter = new TransIter<>(list.iterator(), String::valueOf);
		iter.next();
		iter.remove();
		iter.next();
		iter.remove();
		iter.next();
		iter.remove();
		Assertions.assertTrue(list.isEmpty());
	}
}
