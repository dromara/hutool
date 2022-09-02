package cn.hutool.core.collection.iter;

import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertTrue(enumeration.hasMoreElements());
		Assert.assertFalse(new IteratorEnumeration<>(Collections.emptyIterator()).hasMoreElements());
	}

	@Test
	public void testNextElement() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		IteratorEnumeration<Integer> enumeration = new IteratorEnumeration<>(list.iterator());
		Assert.assertEquals((Integer)1, enumeration.nextElement());
		Assert.assertEquals((Integer)2, enumeration.nextElement());
		Assert.assertEquals((Integer)3, enumeration.nextElement());
	}

}
