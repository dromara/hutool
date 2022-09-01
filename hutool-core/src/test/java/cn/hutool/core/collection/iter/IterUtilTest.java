package cn.hutool.core.collection.iter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * test for {@link IterUtil}
 */
public class IterUtilTest {

	@Test
	public void testGetIter() {
		Assert.assertNull(IterUtil.getIter(null));
		Assert.assertEquals(Collections.emptyIterator(), IterUtil.getIter(Collections.emptyList()));
	}

	@Test
	public void testIsEmpty() {
		Assert.assertTrue(IterUtil.isEmpty(Collections.emptyIterator()));
		Assert.assertFalse(IterUtil.isEmpty(Arrays.asList(1, 2).iterator()));

		Assert.assertTrue(IterUtil.isEmpty(Collections.emptyList()));
		Assert.assertFalse(IterUtil.isEmpty(Arrays.asList(1, 2)));
	}

	@Test
	public void testIsNotEmpty() {
		Assert.assertFalse(IterUtil.isNotEmpty(Collections.emptyIterator()));
		Assert.assertTrue(IterUtil.isNotEmpty(Arrays.asList(1, 2).iterator()));

		Assert.assertFalse(IterUtil.isNotEmpty(Collections.emptyList()));
		Assert.assertTrue(IterUtil.isNotEmpty(Arrays.asList(1, 2)));
	}

	@Test
	public void testHasNull() {
		Assert.assertFalse(IterUtil.hasNull(Arrays.asList(1, 3, 2).iterator()));
		Assert.assertTrue(IterUtil.hasNull(Arrays.asList(1, null, 2).iterator()));
		Assert.assertFalse(IterUtil.hasNull(Collections.emptyIterator()));
		Assert.assertTrue(IterUtil.hasNull(null));
	}

	@Test
	public void testIsAllNull() {
		Assert.assertTrue(IterUtil.isAllNull(Arrays.asList(null, null)));
		Assert.assertFalse(IterUtil.isAllNull(Arrays.asList(1, null)));
		Assert.assertTrue(IterUtil.isAllNull((Iterable<?>)null));
		Assert.assertTrue(IterUtil.isAllNull(Arrays.asList(null, null).iterator()));
		Assert.assertFalse(IterUtil.isAllNull(Arrays.asList(1, null).iterator()));
		Assert.assertTrue(IterUtil.isAllNull((Iterator<?>)null));
	}

	@Test
	public void testCountMap() {
		Object o1 = new Object();
		Object o2 = new Object();
		Map<Object, Integer> countMap = IterUtil.countMap(Arrays.asList(o1, o2, o1, o1).iterator());
		Assert.assertEquals((Integer)3, countMap.get(o1));
		Assert.assertEquals((Integer)1, countMap.get(o2));
	}

}
