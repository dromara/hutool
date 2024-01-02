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

package org.dromara.hutool.core.annotation;

import org.dromara.hutool.core.annotation.elements.CombinationAnnotatedElement;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.util.ObjUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * test for {@link AnnotationUtil}
 */
public class AnnotationUtilTest {

	@Test
	public void testGetDeclaredAnnotations() {
		final Annotation[] annotations = AnnotationUtil.getDeclaredAnnotations(ClassForTest.class);
		Assertions.assertArrayEquals(annotations, ClassForTest.class.getDeclaredAnnotations());
		Assertions.assertSame(annotations, AnnotationUtil.getDeclaredAnnotations(ClassForTest.class));

		AnnotationUtil.clearCaches();
		Assertions.assertNotSame(annotations, AnnotationUtil.getDeclaredAnnotations(ClassForTest.class));
	}

	@Test
	public void testToCombination() {
		final CombinationAnnotatedElement element = AnnotationUtil.toCombination(ClassForTest.class);
		Assertions.assertEquals(2, element.getAnnotations().length);
	}

	@Test
	public void testGetAnnotations() {
		Annotation[] annotations = AnnotationUtil.getAnnotations(ClassForTest.class, true);
		Assertions.assertEquals(2, annotations.length);
		annotations = AnnotationUtil.getAnnotations(ClassForTest.class, false);
		Assertions.assertEquals(1, annotations.length);
	}

	@Test
	public void testGetCombinationAnnotations() {
		final MetaAnnotationForTest[] annotations = AnnotationUtil.getCombinationAnnotations(ClassForTest.class, MetaAnnotationForTest.class);
		Assertions.assertEquals(1, annotations.length);
	}

	@Test
	public void testAnnotations() {
		MetaAnnotationForTest[] annotations1 = AnnotationUtil.getAnnotations(ClassForTest.class, false, MetaAnnotationForTest.class);
		Assertions.assertEquals(0, annotations1.length);
		annotations1 = AnnotationUtil.getAnnotations(ClassForTest.class, true, MetaAnnotationForTest.class);
		Assertions.assertEquals(1, annotations1.length);

		Annotation[] annotations2 = AnnotationUtil.getAnnotations(
			ClassForTest.class, false, t -> ObjUtil.equals(t.annotationType(), MetaAnnotationForTest.class)
		);
		Assertions.assertEquals(0, annotations2.length);
		annotations2 = AnnotationUtil.getAnnotations(
			ClassForTest.class, true, t -> ObjUtil.equals(t.annotationType(), MetaAnnotationForTest.class)
		);
		Assertions.assertEquals(1, annotations2.length);
	}

	@Test
	public void testGetAnnotation() {
		final MetaAnnotationForTest annotation = AnnotationUtil.getAnnotation(ClassForTest.class, MetaAnnotationForTest.class);
		Assertions.assertNotNull(annotation);
	}

	@Test
	public void testHasAnnotation() {
		Assertions.assertTrue(AnnotationUtil.hasAnnotation(ClassForTest.class, MetaAnnotationForTest.class));
	}

	@Test
	public void testGetAnnotationValue() {
		final AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		Assertions.assertEquals(annotation.value(), AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class));
		Assertions.assertEquals(annotation.value(), AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class, "value"));
		Assertions.assertNull(AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class, "property"));
	}

	@Test
	public void testGetAnnotationValueMap() {
		final AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		final Map<String, Object> valueMap = AnnotationUtil.getAnnotationValueMap(ClassForTest.class, AnnotationForTest.class);
		Assertions.assertNotNull(valueMap);
		Assertions.assertEquals(2, valueMap.size());
		Assertions.assertEquals(annotation.value(), valueMap.get("value"));
	}

	@Test
	public void testGetRetentionPolicy() {
		final RetentionPolicy policy = AnnotationForTest.class.getAnnotation(Retention.class).value();
		Assertions.assertEquals(policy, AnnotationUtil.getRetentionPolicy(AnnotationForTest.class));
	}

	@Test
	public void testGetTargetType() {
		final ElementType[] types = AnnotationForTest.class.getAnnotation(Target.class).value();
		Assertions.assertArrayEquals(types, AnnotationUtil.getTargetType(AnnotationForTest.class));
	}

	@Test
	public void testIsDocumented() {
		Assertions.assertFalse(AnnotationUtil.isDocumented(AnnotationForTest.class));
	}

	@Test
	public void testIsInherited() {
		Assertions.assertFalse(AnnotationUtil.isInherited(AnnotationForTest.class));
	}

	@Test
	@EnabledForJreRange(max = JRE.JAVA_8)
	public void testSetValue() {
		// jdk9+中抛出异常，须添加`--add-opens=java.base/java.lang=ALL-UNNAMED`启动参数
		final AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		final String newValue = "is a new value";
		Assertions.assertNotEquals(newValue, annotation.value());
		AnnotationUtil.setValue(annotation, "value", newValue);
		Assertions.assertEquals(newValue, annotation.value());
	}

	@Test
	public void testGetAnnotationAlias() {
		final MetaAnnotationForTest annotation = AnnotationUtil.getAnnotationAlias(AnnotationForTest.class, MetaAnnotationForTest.class);
		Assertions.assertNotNull(annotation);
		Assertions.assertEquals(annotation.value(), annotation.alias());
		Assertions.assertEquals(MetaAnnotationForTest.class, annotation.annotationType());
	}

	@Test
	public void testGetAnnotationAttributes() {
		final Method[] methods = AnnotationUtil.getAnnotationAttributes(AnnotationForTest.class);
		Assertions.assertArrayEquals(methods, AnnotationUtil.getAnnotationAttributes(AnnotationForTest.class));
		Assertions.assertEquals(2, methods.length);
		Assertions.assertArrayEquals(AnnotationForTest.class.getDeclaredMethods(), methods);
	}

	@SneakyThrows
	@Test
	public void testIsAnnotationAttribute() {
		Assertions.assertFalse(AnnotationUtil.isAnnotationAttribute(AnnotationForTest.class.getMethod("equals", Object.class)));
		Assertions.assertTrue(AnnotationUtil.isAnnotationAttribute(AnnotationForTest.class.getMethod("value")));
	}

	@Test
	public void getAnnotationValueTest2() {
		final String[] names = AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest::names);
		Assertions.assertTrue(ArrayUtil.equals(names, new String[]{"测试1", "测试2"}));
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface MetaAnnotationForTest{
		@Alias(value = "alias")
		String value() default "";
		String alias() default "";
	}

	@MetaAnnotationForTest("foo")
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface AnnotationForTest{
		String value() default "";
		String[] names() default "";
	}

	@AnnotationForTest(value = "foo", names = {"测试1", "测试2"})
	private static class ClassForTest{}

}
