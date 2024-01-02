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

import org.dromara.hutool.core.annotation.elements.MetaAnnotatedElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

/**
 * test for {@link MetaAnnotatedElement}
 *
 * @author huangchengxing
 */
public class MetaAnnotatedElementTest {

	private static final BiFunction<ResolvedAnnotationMapping, Annotation, ResolvedAnnotationMapping> RESOLVED_MAPPING_FACTORY =
		(source, annotation) -> new ResolvedAnnotationMapping(source, annotation, true);

	private static final BiFunction<ResolvedAnnotationMapping, Annotation, ResolvedAnnotationMapping> MAPPING_FACTORY =
		(source, annotation) -> new ResolvedAnnotationMapping(source, annotation, false);

	@Test
	public void testEquals() {
		final AnnotatedElement element = new MetaAnnotatedElement<>(Foo.class, RESOLVED_MAPPING_FACTORY);
		Assertions.assertNotEquals(element, null);
		Assertions.assertEquals(element, new MetaAnnotatedElement<>(Foo.class, RESOLVED_MAPPING_FACTORY));
		Assertions.assertNotEquals(element, new MetaAnnotatedElement<>(Foo.class, MAPPING_FACTORY));
		Assertions.assertNotEquals(element, new MetaAnnotatedElement<>(Annotation1.class, MAPPING_FACTORY));
	}

	@Test
	public void testHashCode() {
		final int hashCode = new MetaAnnotatedElement<>(Foo.class, RESOLVED_MAPPING_FACTORY).hashCode();
		Assertions.assertEquals(hashCode, new MetaAnnotatedElement<>(Foo.class, RESOLVED_MAPPING_FACTORY).hashCode());
		Assertions.assertNotEquals(hashCode, new MetaAnnotatedElement<>(Foo.class, MAPPING_FACTORY).hashCode());
	}

	@Test
	public void testCreate() {
		// 第二次创建时优先从缓存中获取
		final AnnotatedElement resolvedElement = MetaAnnotatedElement.create(Foo.class, RESOLVED_MAPPING_FACTORY);
		Assertions.assertEquals(resolvedElement, MetaAnnotatedElement.create(Foo.class, RESOLVED_MAPPING_FACTORY));
		final AnnotatedElement element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Assertions.assertEquals(element, MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY));
	}

	@Test
	public void testGetMapping() {
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Assertions.assertTrue(element.getMapping(Annotation1.class).isPresent());
		Assertions.assertTrue(element.getMapping(Annotation2.class).isPresent());
		Assertions.assertTrue(element.getMapping(Annotation3.class).isPresent());
		Assertions.assertTrue(element.getMapping(Annotation4.class).isPresent());
	}

	@Test
	public void testGetDeclaredMapping() {
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Assertions.assertFalse(element.getDeclaredMapping(Annotation1.class).isPresent());
		Assertions.assertFalse(element.getDeclaredMapping(Annotation2.class).isPresent());
		Assertions.assertTrue(element.getDeclaredMapping(Annotation3.class).isPresent());
		Assertions.assertTrue(element.getDeclaredMapping(Annotation4.class).isPresent());
	}

	@Test
	public void testIsAnnotationPresent() {
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Assertions.assertTrue(element.isAnnotationPresent(Annotation1.class));
		Assertions.assertTrue(element.isAnnotationPresent(Annotation2.class));
		Assertions.assertTrue(element.isAnnotationPresent(Annotation3.class));
		Assertions.assertTrue(element.isAnnotationPresent(Annotation4.class));
	}

	@Test
	public void testGetAnnotation() {
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		final Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		final Annotation2 annotation2 = Annotation3.class.getAnnotation(Annotation2.class);
		final Annotation1 annotation1 = Annotation2.class.getAnnotation(Annotation1.class);

		Assertions.assertEquals(annotation1, element.getAnnotation(Annotation1.class));
		Assertions.assertEquals(annotation2, element.getAnnotation(Annotation2.class));
		Assertions.assertEquals(annotation3, element.getAnnotation(Annotation3.class));
		Assertions.assertEquals(annotation4, element.getAnnotation(Annotation4.class));
	}

	@Test
	public void testGetDeclaredAnnotation() {
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		final Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);

		Assertions.assertNull(element.getDeclaredAnnotation(Annotation1.class));
		Assertions.assertNull(element.getDeclaredAnnotation(Annotation2.class));
		Assertions.assertEquals(annotation3, element.getDeclaredAnnotation(Annotation3.class));
		Assertions.assertEquals(annotation4, element.getDeclaredAnnotation(Annotation4.class));
	}

	@Test
	public void testGetAnnotationByType() {
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		final Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		Assertions.assertArrayEquals(
			new Annotation[]{ annotation4 },
			element.getAnnotationsByType(Annotation4.class)
		);
		Assertions.assertEquals(0, element.getAnnotationsByType(None.class).length);
	}

	@Test
	public void testGetDeclaredAnnotationByType() {
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		final Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		Assertions.assertArrayEquals(
			new Annotation[]{ annotation4 },
			element.getDeclaredAnnotationsByType(Annotation4.class)
		);
		Assertions.assertEquals(0, element.getDeclaredAnnotationsByType(None.class).length);
	}

	@Test
	public void testGetAnnotations() {
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		final Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		final Annotation2 annotation2 = Annotation3.class.getAnnotation(Annotation2.class);
		final Annotation1 annotation1 = Annotation2.class.getAnnotation(Annotation1.class);
		final Annotation[] annotations = new Annotation[]{ annotation3, annotation4, annotation2, annotation1 };

		Assertions.assertArrayEquals(annotations, element.getAnnotations());
	}

	@Test
	public void testGetDeclaredAnnotations() {
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		final Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		final Annotation[] annotations = new Annotation[]{ annotation3, annotation4 };

		Assertions.assertArrayEquals(annotations, element.getDeclaredAnnotations());
	}

	@Test
	public void testIterator() {
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		final Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		final Annotation2 annotation2 = Annotation3.class.getAnnotation(Annotation2.class);
		final Annotation1 annotation1 = Annotation2.class.getAnnotation(Annotation1.class);
		final Annotation[] annotations = new Annotation[]{ annotation3, annotation4, annotation2, annotation1 };

		final Iterator<ResolvedAnnotationMapping> iterator = element.iterator();
		final List<Annotation> mappingList = new ArrayList<>();
		iterator.forEachRemaining(mapping -> mappingList.add(mapping.getAnnotation()));

		Assertions.assertEquals(Arrays.asList(annotations), mappingList);
	}

	@Test
	public void testGetElement() {
		final AnnotatedElement source = Foo.class;
		final MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(source, MAPPING_FACTORY);
		Assertions.assertSame(source, element.getElement());
	}

	@Annotation4 // 循环引用
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface None { }

	@SuppressWarnings("unused")
	@Annotation4 // 循环引用
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation1 {
		@Alias("name")
		String value() default "";
		@Alias("value")
		String name() default "";
	}

	@SuppressWarnings("unused")
	@Annotation1("Annotation2")
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation2 {
		@Alias("name")
		String value() default "";
		@Alias("value")
		String name() default "";
	}

	@Annotation2("Annotation3")
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation3 {
		String name() default "";
	}

	@Annotation2("Annotation4")
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation4 {
		String value() default "";
	}

	@Annotation3(name = "foo")
	@Annotation4("foo")
	private static class Foo {}

}
