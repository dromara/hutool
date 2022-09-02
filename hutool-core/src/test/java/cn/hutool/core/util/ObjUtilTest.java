package cn.hutool.core.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.exceptions.CloneRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ObjUtilTest {

	@Test
	public void equalsTest() {
		Object a = null;
		Object b = null;
		Assert.assertTrue(ObjUtil.equals(a, b));

		a = new BigDecimal("1.1");
		b = new BigDecimal("1.10");
		Assert.assertTrue(ObjUtil.equals(a, b));

		a = 127;
		b = 127;
		Assert.assertTrue(ObjUtil.equals(a, b));

		a = 128;
		b = 128;
		Assert.assertTrue(ObjUtil.equals(a, b));

		a = LocalDateTime.of(2022, 5, 29, 22, 11);
		b = LocalDateTime.of(2022, 5, 29, 22, 11);
		Assert.assertTrue(ObjUtil.equals(a, b));

		a = 1;
		b = 1.0;
		Assert.assertFalse(ObjUtil.equals(a, b));
	}

	@Test
	public void lengthTest(){
		final int[] array = new int[]{1,2,3,4,5};
		int length = ObjUtil.length(array);
		Assert.assertEquals(5, length);

		final Map<String, String> map = new HashMap<>();
		map.put("a", "a1");
		map.put("b", "b1");
		map.put("c", "c1");
		length = ObjUtil.length(map);
		Assert.assertEquals(3, length);

		final Iterable<Integer> list = ListUtil.of(1, 2, 3);
		Assert.assertEquals(3, ObjUtil.length(list));
	}

	@Test
	public void containsTest(){
		final int[] array = new int[]{1,2,3,4,5};

		final boolean contains = ObjUtil.contains(array, 1);
		Assert.assertTrue(contains);
	}

	@Test
	public void cloneTest() {
		final Obj obj = new Obj();
		final Obj obj2 = ObjUtil.clone(obj);
		Assert.assertEquals("OK", obj2.doSomeThing());
	}

	static class Obj implements Cloneable {
		public String doSomeThing() {
			return "OK";
		}

		@Override
		public Obj clone() {
			try {
				return (Obj) super.clone();
			} catch (final CloneNotSupportedException e) {
				throw new CloneRuntimeException(e);
			}
		}
	}

	@Test
	public void toStringTest() {
		final ArrayList<String> strings = ListUtil.of("1", "2");
		final String result = ObjUtil.toString(strings);
		Assert.assertEquals("[1, 2]", result);
	}

	@Test
	public void defaultIfNullTest() {
		final Object val1 = new Object();
		final Object val2 = new Object();

		Assert.assertSame(val1, ObjUtil.defaultIfNull(val1, () -> val2));
		Assert.assertSame(val2, ObjUtil.defaultIfNull(null, () -> val2));

		Assert.assertSame(val1, ObjUtil.defaultIfNull(val1, val2));
		Assert.assertSame(val2, ObjUtil.defaultIfNull(null, val2));

		Assert.assertSame(val1, ObjUtil.defaultIfNull(val1, Function.identity(), () -> val2));
		Assert.assertSame(val2, ObjUtil.defaultIfNull(null, Function.identity(), () -> val2));

		Assert.assertSame(val1, ObjUtil.defaultIfNull(val1, Function.identity(), val2));
		Assert.assertSame(val2, ObjUtil.defaultIfNull(null, Function.identity(), val2));
	}

	@Test
	public void isBasicTypeTest(){
		final int a = 1;
		final boolean basicType = ObjUtil.isBasicType(a);
		Assert.assertTrue(basicType);
	}

	@Test
	public void cloneIfPossibleTest() {
		final String a = "a";
		final String a2 = ObjUtil.cloneIfPossible(a);
		Assert.assertNotSame(a, a2);
	}
}
