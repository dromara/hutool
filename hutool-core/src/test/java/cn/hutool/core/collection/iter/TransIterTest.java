package cn.hutool.core.collection.iter;

import cn.hutool.core.collection.ListUtil;
import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertTrue(iter.hasNext());
		Assert.assertFalse(new TransIter<>(Collections.emptyIterator(), Function.identity()).hasNext());
	}

	@Test
	public void testNext() {
		TransIter<Integer, String> iter = new TransIter<>(Arrays.asList(1, 2, 3).iterator(), String::valueOf);
		Assert.assertEquals("1", iter.next());
		Assert.assertEquals("2", iter.next());
		Assert.assertEquals("3", iter.next());
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
		Assert.assertTrue(list.isEmpty());
	}
}
