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

import lombok.SneakyThrows;
import org.dromara.hutool.core.annotation.elements.HierarchicalAnnotatedElements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;

/**
 * test for {@link HierarchicalAnnotatedElements}
 *
 * @author huangchengxing
 */
public class HierarchicalAnnotatedElementTest {

	private static final BiFunction<Set<AnnotatedElement>, AnnotatedElement, AnnotatedElement>  ELEMENT_MAPPING_FACTORY = (es, e) -> e;

	@SneakyThrows
	@Test
	public void testCreateFromMethod() {
		final Method method1 = Foo.class.getDeclaredMethod("method");
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(method1);
		Assertions.assertEquals(3, elements.getElementMappings().size());

		final Method method2 = Foo.class.getDeclaredMethod("method2");
		elements = HierarchicalAnnotatedElements.of(method2);
		Assertions.assertEquals(1, elements.getElementMappings().size());
	}

	@Test
	public void testCreate() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(Foo.class);
		Assertions.assertNotNull(elements);
		Assertions.assertEquals(3, elements.getElementMappings().size());

		elements = HierarchicalAnnotatedElements.of(Foo.class, ELEMENT_MAPPING_FACTORY);
		Assertions.assertNotNull(elements);
		Assertions.assertEquals(3, elements.getElementMappings().size());

		Assertions.assertEquals(elements, HierarchicalAnnotatedElements.of(elements, ELEMENT_MAPPING_FACTORY));
	}

	@Test
	public void testEquals() {
		final HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(Foo.class, ELEMENT_MAPPING_FACTORY);
		Assertions.assertEquals(elements, HierarchicalAnnotatedElements.of(Foo.class, ELEMENT_MAPPING_FACTORY));
		Assertions.assertNotEquals(elements, HierarchicalAnnotatedElements.of(Super.class, ELEMENT_MAPPING_FACTORY));
		Assertions.assertNotEquals(elements, HierarchicalAnnotatedElements.of(Foo.class, (es, e) -> e));
		Assertions.assertNotEquals(elements, null);
	}

	@Test
	public void testHashCode() {
		final int hashCode = HierarchicalAnnotatedElements.of(Foo.class, ELEMENT_MAPPING_FACTORY).hashCode();
		Assertions.assertEquals(hashCode, HierarchicalAnnotatedElements.of(Foo.class, ELEMENT_MAPPING_FACTORY).hashCode());
		Assertions.assertNotEquals(hashCode, HierarchicalAnnotatedElements.of(Super.class, ELEMENT_MAPPING_FACTORY).hashCode());
		Assertions.assertNotEquals(hashCode, HierarchicalAnnotatedElements.of(Foo.class, (es, e) -> e).hashCode());
	}

	@Test
	public void testGetElement() {
		final AnnotatedElement element = Foo.class;
		final HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(element, ELEMENT_MAPPING_FACTORY);
		Assertions.assertSame(element, elements.getElement());
	}

	@Test
	public void testIsAnnotationPresent() {
		final HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(Foo.class);
		Assertions.assertTrue(elements.isAnnotationPresent(Annotation1.class));
		Assertions.assertTrue(elements.isAnnotationPresent(Annotation2.class));
		Assertions.assertTrue(elements.isAnnotationPresent(Annotation3.class));
	}

	@Test
	public void testGetAnnotations() {
		final HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(Foo.class);

		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		final Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		final Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		final Annotation[] annotations = new Annotation[]{ annotation1, annotation2, annotation3 };

		Assertions.assertArrayEquals(annotations, elements.getAnnotations());
	}

	@Test
	public void testGetAnnotation() {
		final HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(Foo.class);

		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Assertions.assertEquals(annotation1, elements.getAnnotation(Annotation1.class));

		final Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		Assertions.assertEquals(annotation2, elements.getAnnotation(Annotation2.class));

		final Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(annotation3, elements.getAnnotation(Annotation3.class));
	}

	@Test
	public void testGetAnnotationsByType() {
		final HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(Foo.class);

		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Assertions.assertArrayEquals(new Annotation[]{ annotation1 }, elements.getAnnotationsByType(Annotation1.class));

		final Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		Assertions.assertArrayEquals(new Annotation[]{ annotation2 }, elements.getAnnotationsByType(Annotation2.class));

		final Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		Assertions.assertArrayEquals(new Annotation[]{ annotation3 }, elements.getAnnotationsByType(Annotation3.class));
	}

	@Test
	public void testGetDeclaredAnnotationsByType() {
		final HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(Foo.class);

		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Assertions.assertArrayEquals(new Annotation[]{ annotation1 }, elements.getDeclaredAnnotationsByType(Annotation1.class));

		final Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		Assertions.assertArrayEquals(new Annotation[]{ annotation2 }, elements.getDeclaredAnnotationsByType(Annotation2.class));

		final Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		Assertions.assertArrayEquals(new Annotation[]{ annotation3 }, elements.getDeclaredAnnotationsByType(Annotation3.class));
	}

	@Test
	public void testGetDeclaredAnnotation() {
		final HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(Foo.class);

		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Assertions.assertEquals(annotation1, elements.getDeclaredAnnotation(Annotation1.class));

		final Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		Assertions.assertEquals(annotation2, elements.getDeclaredAnnotation(Annotation2.class));

		final Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(annotation3, elements.getDeclaredAnnotation(Annotation3.class));
	}

	@Test
	public void testGetDeclaredAnnotations() {
		final HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(Foo.class);

		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		final Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		final Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		final Annotation[] annotations = new Annotation[]{ annotation1, annotation2, annotation3 };

		Assertions.assertArrayEquals(annotations, elements.getDeclaredAnnotations());
	}

	@Test
	public void testIterator() {
		final HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.of(Foo.class);
		final Iterator<AnnotatedElement> iterator = elements.iterator();
		Assertions.assertNotNull(iterator);

		final List<AnnotatedElement> elementList = new ArrayList<>();
		iterator.forEachRemaining(elementList::add);
		Assertions.assertEquals(Arrays.asList(Foo.class, Super.class, Interface.class), elementList);
	}

	@Target({ElementType.TYPE_USE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation3 { }

	@Target({ElementType.TYPE_USE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation2 { }

	@Target({ElementType.TYPE_USE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation1 { }

	@SuppressWarnings("unused")
	@Annotation3
	private interface Interface {
		@Annotation3
		String method();
		@Annotation3
		static String method2() { return null; }
	}

	@Annotation2
	private static class Super {
		@Annotation2
		public String method() { return null; }
		@Annotation2
		public static String method2() { return null; }
	}

	@Annotation1
	private static class Foo extends Super implements Interface {
		@Annotation1
		@Override
		public String method() { return null; }
		@Annotation1
		public static String method2() { return null; }
	}

}
