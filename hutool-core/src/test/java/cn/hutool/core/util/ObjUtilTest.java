package cn.hutool.core.util;

import cn.hutool.core.collection.ListUtil;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * test for {@link ObjUtil}
 */
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
		Assert.assertEquals(3, ObjUtil.length(Arrays.asList(1, 2, 3).iterator()));
	}

	@Test
	public void containsTest(){
		Assert.assertTrue(ObjUtil.contains(new int[]{1,2,3,4,5}, 1));
		Assert.assertFalse(ObjUtil.contains(null, 1));
		Assert.assertTrue(ObjUtil.contains("123", "3"));
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		Assert.assertTrue(ObjUtil.contains(map, 1));
		Assert.assertTrue(ObjUtil.contains(Arrays.asList(1, 2, 3).iterator(), 2));
	}

	@Test
	public void isNullTest() {
		Assert.assertTrue(ObjUtil.isNull(null));
	}

	@Test
	public void isNotNullTest() {
		Assert.assertFalse(ObjUtil.isNotNull(null));
	}

	@Test
	public void isEmptyTest() {
		Assert.assertTrue(ObjUtil.isEmpty(null));
		Assert.assertTrue(ObjUtil.isEmpty(new int[0]));
		Assert.assertTrue(ObjUtil.isEmpty(""));
		Assert.assertTrue(ObjUtil.isEmpty(Collections.emptyList()));
		Assert.assertTrue(ObjUtil.isEmpty(Collections.emptyMap()));
		Assert.assertTrue(ObjUtil.isEmpty(Collections.emptyIterator()));
	}

	@Test
	public void isNotEmptyTest() {
		Assert.assertFalse(ObjUtil.isNotEmpty(null));
		Assert.assertFalse(ObjUtil.isNotEmpty(new int[0]));
		Assert.assertFalse(ObjUtil.isNotEmpty(""));
		Assert.assertFalse(ObjUtil.isNotEmpty(Collections.emptyList()));
		Assert.assertFalse(ObjUtil.isNotEmpty(Collections.emptyMap()));
		Assert.assertFalse(ObjUtil.isNotEmpty(Collections.emptyIterator()));
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
	public void cloneTest() {
		Assert.assertNull(ObjUtil.clone(null));

		final CloneableBean cloneableBean1 = new CloneableBean(1);
		final CloneableBean cloneableBean2 = ObjUtil.clone(cloneableBean1);
		Assert.assertEquals(cloneableBean1, cloneableBean2);

		final SerializableBean serializableBean1 = new SerializableBean(2);
		final SerializableBean serializableBean2 = ObjUtil.clone(serializableBean1);
		Assert.assertEquals(serializableBean1, serializableBean2);

		final Bean bean1 = new Bean(3);
		Assert.assertNull(ObjUtil.clone(bean1));
	}

	@Test
	public void cloneIfPossibleTest() {
		Assert.assertNull(ObjUtil.clone(null));

		final CloneableBean cloneableBean1 = new CloneableBean(1);
		Assert.assertEquals(cloneableBean1, ObjUtil.cloneIfPossible(cloneableBean1));

		final SerializableBean serializableBean1 = new SerializableBean(2);
		Assert.assertEquals(serializableBean1, ObjUtil.cloneIfPossible(serializableBean1));

		final Bean bean1 = new Bean(3);
		Assert.assertSame(bean1, ObjUtil.cloneIfPossible(bean1));

		final ExceptionCloneableBean exceptionBean1 = new ExceptionCloneableBean(3);
		Assert.assertSame(exceptionBean1, ObjUtil.cloneIfPossible(exceptionBean1));
	}

	@Test
	public void cloneByStreamTest() {
		Assert.assertNull(ObjUtil.cloneByStream(null));
		Assert.assertNull(ObjUtil.cloneByStream(new CloneableBean(1)));
		final SerializableBean serializableBean1 = new SerializableBean(2);
		Assert.assertEquals(serializableBean1, ObjUtil.cloneByStream(serializableBean1));
		Assert.assertNull(ObjUtil.cloneByStream(new Bean(1)));
	}

	@Test
	public void isBasicTypeTest(){
		final int a = 1;
		final boolean basicType = ObjUtil.isBasicType(a);
		Assert.assertTrue(basicType);
	}

	@Test
	public void isValidIfNumberTest() {
		Assert.assertTrue(ObjUtil.isValidIfNumber(null));
		Assert.assertFalse(ObjUtil.isValidIfNumber(Double.NEGATIVE_INFINITY));
		Assert.assertFalse(ObjUtil.isValidIfNumber(Double.NaN));
		Assert.assertTrue(ObjUtil.isValidIfNumber(Double.MIN_VALUE));
		Assert.assertFalse(ObjUtil.isValidIfNumber(Float.NEGATIVE_INFINITY));
		Assert.assertFalse(ObjUtil.isValidIfNumber(Float.NaN));
		Assert.assertTrue(ObjUtil.isValidIfNumber(Float.MIN_VALUE));
	}

	@Test
	public void compareTest() {
		Assert.assertEquals(0, ObjUtil.compare(1, 1));
		Assert.assertEquals(1, ObjUtil.compare(1, null));
		Assert.assertEquals(-1, ObjUtil.compare(null, 1));

		Assert.assertEquals(-1, ObjUtil.compare(1, null, true));
		Assert.assertEquals(1, ObjUtil.compare(null, 1, true));
	}

	@Test
	public void getTypeArgumentTest() {
		final Bean bean = new Bean(1);
		Assert.assertEquals(Integer.class, ObjUtil.getTypeArgument(bean));
		Assert.assertEquals(String.class, ObjUtil.getTypeArgument(bean, 1));
	}

	@Test
	public void toStringTest() {
		Assert.assertEquals("null", ObjUtil.toString(null));
		Assert.assertEquals(Collections.emptyMap().toString(), ObjUtil.toString(Collections.emptyMap()));
		Assert.assertEquals("[1, 2]", Arrays.asList("1", "2").toString());
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	private static class ExceptionCloneableBean implements Cloneable {
		private final Integer id;
		@Override
		protected Object clone() throws CloneNotSupportedException {
			throw new RuntimeException("can not clone this object");
		}
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	private static class CloneableBean implements Cloneable {
		private final Integer id;
		@Override
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	private static class SerializableBean implements Serializable {
		private final Integer id;
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	private static class Bean implements TypeArgument<Integer, String> {
		private final Integer id;
	}

	private interface TypeArgument<A, B> {};

}
