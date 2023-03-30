package cn.hutool.core.collection.iter;

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
