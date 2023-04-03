package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.date.Week;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;

public class ConstructorUtilTest {
	@Test
	public void noneStaticInnerClassTest() {
		final ReflectUtilTest.NoneStaticClass testAClass = ConstructorUtil.newInstanceIfPossible(ReflectUtilTest.NoneStaticClass.class);
		Assertions.assertNotNull(testAClass);
		Assertions.assertEquals(2, testAClass.getA());
	}

	@Test
	public void newInstanceIfPossibleTest(){
		//noinspection ConstantConditions
		final int intValue = ConstructorUtil.newInstanceIfPossible(int.class);
		Assertions.assertEquals(0, intValue);

		final Integer integer = ConstructorUtil.newInstanceIfPossible(Integer.class);
		Assertions.assertEquals(new Integer(0), integer);

		final Map<?, ?> map = ConstructorUtil.newInstanceIfPossible(Map.class);
		Assertions.assertNotNull(map);

		final Collection<?> collection = ConstructorUtil.newInstanceIfPossible(Collection.class);
		Assertions.assertNotNull(collection);

		final Week week = ConstructorUtil.newInstanceIfPossible(Week.class);
		Assertions.assertEquals(Week.SUNDAY, week);

		final int[] intArray = ConstructorUtil.newInstanceIfPossible(int[].class);
		Assertions.assertArrayEquals(new int[0], intArray);
	}
}
