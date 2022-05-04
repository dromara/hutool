package cn.hutool.core.reflect;

import cn.hutool.core.date.Week;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class ConstructorUtilTest {
	@Test
	public void noneStaticInnerClassTest() {
		final ReflectUtilTest.NoneStaticClass testAClass = ConstructorUtil.newInstanceIfPossible(ReflectUtilTest.NoneStaticClass.class);
		Assert.assertNotNull(testAClass);
		Assert.assertEquals(2, testAClass.getA());
	}

	@Test
	public void newInstanceIfPossibleTest(){
		//noinspection ConstantConditions
		final int intValue = ConstructorUtil.newInstanceIfPossible(int.class);
		Assert.assertEquals(0, intValue);

		final Integer integer = ConstructorUtil.newInstanceIfPossible(Integer.class);
		Assert.assertEquals(new Integer(0), integer);

		final Map<?, ?> map = ConstructorUtil.newInstanceIfPossible(Map.class);
		Assert.assertNotNull(map);

		final Collection<?> collection = ConstructorUtil.newInstanceIfPossible(Collection.class);
		Assert.assertNotNull(collection);

		final Week week = ConstructorUtil.newInstanceIfPossible(Week.class);
		Assert.assertEquals(Week.SUNDAY, week);

		final int[] intArray = ConstructorUtil.newInstanceIfPossible(int[].class);
		Assert.assertArrayEquals(new int[0], intArray);
	}
}
