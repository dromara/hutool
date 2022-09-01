package cn.hutool.core.collection.iter;

import cn.hutool.core.collection.ListUtil;
import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertEquals(2, count);
	}

    @Test
    public void hasNext() {
		Iterator<Integer> iter = new FilterIter<>(Arrays.asList(1, 2, 3).iterator(), i -> true);
		Assert.assertTrue(iter.hasNext());
		iter = new FilterIter<>(Collections.emptyIterator(), i -> true);
		Assert.assertFalse(iter.hasNext());
    }

    @Test
    public void next() {
		// 只保留奇数
		Iterator<Integer> iter = new FilterIter<>(Arrays.asList(1, 2, 3).iterator(), i -> (i & 1) == 1);
		Assert.assertEquals((Integer)1, iter.next());
		Assert.assertEquals((Integer)3, iter.next());
    }

    @Test
    public void remove() {
		Iterator<Integer> iter = new FilterIter<>(Collections.emptyIterator(), i -> true);
		Assert.assertThrows(IllegalStateException.class, iter::remove);
    }

    @Test
    public void getIterator() {
		FilterIter<Integer> iter = new FilterIter<>(Collections.emptyIterator(), i -> true);
		Assert.assertSame(Collections.emptyIterator(), iter.getIterator());
    }

    @Test
    public void getFilter() {
		Predicate<Integer> predicate = i -> true;
		FilterIter<Integer> iter = new FilterIter<>(Collections.emptyIterator(), predicate);
		Assert.assertSame(predicate, iter.getFilter());
    }

}
