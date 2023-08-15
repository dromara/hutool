/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.collection.ListUtil;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.util.ObjUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * test for {@link ObjUtil}
 */
public class ObjUtilTest {

	@SuppressWarnings("ConstantValue")
	@Test
	public void equalsTest() {
		Object a = null;
		Object b = null;
		Assertions.assertTrue(ObjUtil.equals(a, b));

		a = new BigDecimal("1.1");
		b = new BigDecimal("1.10");
		Assertions.assertTrue(ObjUtil.equals(a, b));

		a = 127;
		b = 127;
		Assertions.assertTrue(ObjUtil.equals(a, b));

		a = 128;
		b = 128;
		Assertions.assertTrue(ObjUtil.equals(a, b));

		a = LocalDateTime.of(2022, 5, 29, 22, 11);
		b = LocalDateTime.of(2022, 5, 29, 22, 11);
		Assertions.assertTrue(ObjUtil.equals(a, b));

		a = 1;
		b = 1.0;
		Assertions.assertFalse(ObjUtil.equals(a, b));
	}

	@Test
	public void lengthTest(){
		final int[] array = new int[]{1,2,3,4,5};
		int length = ObjUtil.length(array);
		Assertions.assertEquals(5, length);

		final Map<String, String> map = new HashMap<>();
		map.put("a", "a1");
		map.put("b", "b1");
		map.put("c", "c1");
		length = ObjUtil.length(map);
		Assertions.assertEquals(3, length);

		final Iterable<Integer> list = ListUtil.of(1, 2, 3);
		Assertions.assertEquals(3, ObjUtil.length(list));
		Assertions.assertEquals(3, ObjUtil.length(Arrays.asList(1, 2, 3).iterator()));
	}

	@Test
	public void containsTest(){
		Assertions.assertTrue(ObjUtil.contains(new int[]{1,2,3,4,5}, 1));
		Assertions.assertFalse(ObjUtil.contains(null, 1));
		Assertions.assertTrue(ObjUtil.contains("123", "3"));
		final Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		Assertions.assertTrue(ObjUtil.contains(map, 1));
		Assertions.assertTrue(ObjUtil.contains(Arrays.asList(1, 2, 3).iterator(), 2));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isNullTest() {
		Assertions.assertTrue(ObjUtil.isNull(null));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isNotNullTest() {
		Assertions.assertFalse(ObjUtil.isNotNull(null));
	}

	@Test
	public void isEmptyTest() {
		Assertions.assertTrue(ObjUtil.isEmpty(null));
		Assertions.assertTrue(ObjUtil.isEmpty(new int[0]));
		Assertions.assertTrue(ObjUtil.isEmpty(""));
		Assertions.assertTrue(ObjUtil.isEmpty(Collections.emptyList()));
		Assertions.assertTrue(ObjUtil.isEmpty(Collections.emptyMap()));
		Assertions.assertTrue(ObjUtil.isEmpty(Collections.emptyIterator()));
	}

	@Test
	public void isNotEmptyTest() {
		Assertions.assertFalse(ObjUtil.isNotEmpty(null));
		Assertions.assertFalse(ObjUtil.isNotEmpty(new int[0]));
		Assertions.assertFalse(ObjUtil.isNotEmpty(""));
		Assertions.assertFalse(ObjUtil.isNotEmpty(Collections.emptyList()));
		Assertions.assertFalse(ObjUtil.isNotEmpty(Collections.emptyMap()));
		Assertions.assertFalse(ObjUtil.isNotEmpty(Collections.emptyIterator()));
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void defaultIfNullTest() {
		final Object val1 = new Object();
		final Object val2 = new Object();

		Assertions.assertSame(val1, ObjUtil.defaultIfNull(val1, () -> val2));
		Assertions.assertSame(val2, ObjUtil.defaultIfNull(null, () -> val2));

		Assertions.assertSame(val1, ObjUtil.defaultIfNull(val1, val2));
		Assertions.assertSame(val2, ObjUtil.defaultIfNull(null, val2));

		Assertions.assertSame(val1, ObjUtil.defaultIfNull(val1, Function.identity(), () -> val2));
		Assertions.assertSame(val2, ObjUtil.defaultIfNull(null, Function.identity(), () -> val2));

		Assertions.assertSame(val1, ObjUtil.defaultIfNull(val1, Function.identity(), val2));
		Assertions.assertSame(val2, ObjUtil.defaultIfNull(null, Function.identity(), val2));

		final SerializableBean obj = new SerializableBean(null);
		final SerializableBean objNull = null;
		final String result3 = ObjUtil.defaultIfNull(obj, Object::toString, "fail");
		Assertions.assertNotNull(result3);

		final String result4 = ObjUtil.defaultIfNull(objNull, Object::toString, () -> "fail");
		Assertions.assertNotNull(result4);
	}

	@Test
	void cloneListTest() {
		final ArrayList<Integer> list = ListUtil.of(1, 2);
		final ArrayList<Integer> clone = ObjUtil.clone(list);
		Assertions.assertEquals(list, clone);
	}

	@Test
	public void cloneTest() {
		Assertions.assertNull(ObjUtil.clone(null));

		final CloneableBean cloneableBean1 = new CloneableBean(1);
		final CloneableBean cloneableBean2 = ObjUtil.clone(cloneableBean1);
		Assertions.assertEquals(cloneableBean1, cloneableBean2);

		final SerializableBean serializableBean1 = new SerializableBean(2);
		final SerializableBean serializableBean2 = ObjUtil.clone(serializableBean1);
		Assertions.assertEquals(serializableBean1, serializableBean2);

		final Bean bean1 = new Bean(3);
		Assertions.assertNull(ObjUtil.clone(bean1));
	}

	@Test
	public void cloneIfPossibleTest() {
		Assertions.assertNull(ObjUtil.clone(null));

		final CloneableBean cloneableBean1 = new CloneableBean(1);
		Assertions.assertEquals(cloneableBean1, ObjUtil.cloneIfPossible(cloneableBean1));

		final SerializableBean serializableBean1 = new SerializableBean(2);
		Assertions.assertEquals(serializableBean1, ObjUtil.cloneIfPossible(serializableBean1));

		final Bean bean1 = new Bean(3);
		Assertions.assertSame(bean1, ObjUtil.cloneIfPossible(bean1));

		final ExceptionCloneableBean exceptionBean1 = new ExceptionCloneableBean(3);
		Assertions.assertSame(exceptionBean1, ObjUtil.cloneIfPossible(exceptionBean1));
	}

	@Test
	public void cloneByStreamTest() {
		Assertions.assertNull(ObjUtil.cloneByStream(null));
		Assertions.assertNull(ObjUtil.cloneByStream(new CloneableBean(1)));
		final SerializableBean serializableBean1 = new SerializableBean(2);
		Assertions.assertEquals(serializableBean1, ObjUtil.cloneByStream(serializableBean1));
		Assertions.assertNull(ObjUtil.cloneByStream(new Bean(1)));
	}

	@Test
	public void isBasicTypeTest(){
		final int a = 1;
		final boolean basicType = ObjUtil.isBasicType(a);
		Assertions.assertTrue(basicType);
	}

	@Test
	public void isValidIfNumberTest() {
		Assertions.assertTrue(ObjUtil.isValidIfNumber(null));
		Assertions.assertFalse(ObjUtil.isValidIfNumber(Double.NEGATIVE_INFINITY));
		Assertions.assertFalse(ObjUtil.isValidIfNumber(Double.NaN));
		Assertions.assertTrue(ObjUtil.isValidIfNumber(Double.MIN_VALUE));
		Assertions.assertFalse(ObjUtil.isValidIfNumber(Float.NEGATIVE_INFINITY));
		Assertions.assertFalse(ObjUtil.isValidIfNumber(Float.NaN));
		Assertions.assertTrue(ObjUtil.isValidIfNumber(Float.MIN_VALUE));
	}

	@Test
	public void getTypeArgumentTest() {
		final Bean bean = new Bean(1);
		Assertions.assertEquals(Integer.class, ObjUtil.getTypeArgument(bean));
		Assertions.assertEquals(String.class, ObjUtil.getTypeArgument(bean, 1));
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("null", ObjUtil.toString(null));
		Assertions.assertEquals(Collections.emptyMap().toString(), ObjUtil.toString(Collections.emptyMap()));
		Assertions.assertEquals("[1, 2]", Arrays.asList("1", "2").toString());
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	private static class ExceptionCloneableBean implements Cloneable {
		private final Integer id;
		@Override
		protected Object clone() throws CloneNotSupportedException {
			throw new CloneNotSupportedException("can not clone this object");
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
		private static final long serialVersionUID = -7759522980793544334L;
		private final Integer id;
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	private static class Bean implements TypeArgument<Integer, String> {
		private final Integer id;
	}

	@SuppressWarnings("unused")
	private interface TypeArgument<A, B> {}

}
