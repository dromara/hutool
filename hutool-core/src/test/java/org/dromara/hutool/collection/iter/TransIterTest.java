package org.dromara.hutool.collection.iter;

import org.dromara.hutool.collection.ListUtil;
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
