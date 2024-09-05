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
import java.util.Iterator;
import java.util.function.Predicate;

public class FilterIterTest {

	@Test
	public void iterNullTest(){
		final Iterator<String> it = ListUtil.of("1", "2").iterator();
		final FilterIter<String> filterIter = new FilterIter<>(it, null);
		int count = 0;
		while (filterIter.hasNext()) {
			if(null != filterIter.next()){
				count++;
			}
		}
		Assertions.assertEquals(2, count);
	}

    @Test
    public void hasNext() {
		Iterator<Integer> iter = new FilterIter<>(Arrays.asList(1, 2, 3).iterator(), i -> true);
		Assertions.assertTrue(iter.hasNext());
		iter = new FilterIter<>(Collections.emptyIterator(), i -> true);
		Assertions.assertFalse(iter.hasNext());
    }

    @Test
    public void next() {
		// 只保留奇数
		Iterator<Integer> iter = new FilterIter<>(Arrays.asList(1, 2, 3).iterator(), i -> (i & 1) == 1);
		Assertions.assertEquals((Integer)1, iter.next());
		Assertions.assertEquals((Integer)3, iter.next());
    }

    @Test
    public void remove() {
		Iterator<Integer> iter = new FilterIter<>(Collections.emptyIterator(), i -> true);
		Assertions.assertThrows(IllegalStateException.class, iter::remove);
    }

    @Test
    public void getIterator() {
		FilterIter<Integer> iter = new FilterIter<>(Collections.emptyIterator(), i -> true);
		Assertions.assertSame(Collections.emptyIterator(), iter.getIterator());
    }

    @Test
    public void getFilter() {
		Predicate<Integer> predicate = i -> true;
		FilterIter<Integer> iter = new FilterIter<>(Collections.emptyIterator(), predicate);
		Assertions.assertSame(predicate, iter.getFilter());
    }

}
