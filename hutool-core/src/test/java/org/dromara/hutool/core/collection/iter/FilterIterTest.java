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
