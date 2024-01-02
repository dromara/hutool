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

import org.dromara.hutool.core.annotation.elements.RepeatableMetaAnnotatedElement;
import org.dromara.hutool.core.collection.iter.IterUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * test for {@link RepeatableMetaAnnotatedElement}
 *
 * @author huangchengxing
 */
public class RepeatableMetaAnnotatedElementTest {

	private static final BiFunction<ResolvedAnnotationMapping, Annotation, ResolvedAnnotationMapping> RESOLVED_MAPPING_FACTORY =
		(source, annotation) -> new ResolvedAnnotationMapping(source, annotation, true);

	private static final BiFunction<ResolvedAnnotationMapping, Annotation, ResolvedAnnotationMapping> MAPPING_FACTORY =
		(source, annotation) -> new ResolvedAnnotationMapping(source, annotation, false);

	@Test
	public void testEquals() {
		final AnnotatedElement element = RepeatableMetaAnnotatedElement.create(Foo.class, RESOLVED_MAPPING_FACTORY);
		Assertions.assertNotEquals(element, null);
		Assertions.assertEquals(element, RepeatableMetaAnnotatedElement.create(Foo.class, RESOLVED_MAPPING_FACTORY));
		Assertions.assertNotEquals(element, RepeatableMetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY));
		Assertions.assertNotEquals(element, RepeatableMetaAnnotatedElement.create(Annotation1.class, MAPPING_FACTORY));
	}

	@Test
	public void testHashCode() {
		final int hashCode = RepeatableMetaAnnotatedElement.create(Foo.class, RESOLVED_MAPPING_FACTORY).hashCode();
		Assertions.assertEquals(hashCode, RepeatableMetaAnnotatedElement.create(Foo.class, RESOLVED_MAPPING_FACTORY).hashCode());
		Assertions.assertNotEquals(hashCode, RepeatableMetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY).hashCode());
		Assertions.assertNotEquals(hashCode, RepeatableMetaAnnotatedElement.create(Annotation1.class, MAPPING_FACTORY).hashCode());
	}

	@Test
	public void testIsAnnotationPresent() {
		final AnnotatedElement element = RepeatableMetaAnnotatedElement.create(
			RepeatableAnnotationCollector.standard(), Foo.class, (s, a) -> new GenericAnnotationMapping(a, Objects.isNull(s))
		);
		Assertions.assertTrue(element.isAnnotationPresent(Annotation1.class));
		Assertions.assertTrue(element.isAnnotationPresent(Annotation2.class));
		Assertions.assertTrue(element.isAnnotationPresent(Annotation3.class));
		Assertions.assertTrue(element.isAnnotationPresent(Annotation4.class));
	}

	@Test
	public void testGetAnnotations() {
		final AnnotatedElement element = RepeatableMetaAnnotatedElement.create(
			RepeatableAnnotationCollector.standard(), Foo.class, (s, a) -> new GenericAnnotationMapping(a, Objects.isNull(s))
		);
		final List<Class<? extends Annotation>> annotationTypes = Arrays.stream(element.getAnnotations())
			.map(Annotation::annotationType)
			.collect(Collectors.toList());
		final Map<Class<? extends Annotation>, Integer> countMap = IterUtil.countMap(annotationTypes.iterator());

		Assertions.assertEquals((Integer)1, countMap.get(Annotation1.class));
		Assertions.assertEquals((Integer)2, countMap.get(Annotation2.class));
		Assertions.assertEquals((Integer)4, countMap.get(Annotation3.class));
		Assertions.assertEquals((Integer)7, countMap.get(Annotation4.class));
	}

	@Test
	public void testGetAnnotation() {
		final AnnotatedElement element = RepeatableMetaAnnotatedElement.create(
			RepeatableAnnotationCollector.standard(), Foo.class, (s, a) -> new GenericAnnotationMapping(a, Objects.isNull(s))
		);

		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Assertions.assertEquals(annotation1, element.getAnnotation(Annotation1.class));

		final Annotation4 annotation4 = Annotation1.class.getAnnotation(Annotation4.class);
		Assertions.assertEquals(annotation4, element.getAnnotation(Annotation4.class));

		final Annotation2 annotation2 = annotation1.value()[0];
		Assertions.assertEquals(annotation2, element.getAnnotation(Annotation2.class));

		final Annotation3 annotation3 = annotation2.value()[0];
		Assertions.assertEquals(annotation3, element.getAnnotation(Annotation3.class));
	}

	@Test
	public void testGetAnnotationsByType() {
		final AnnotatedElement element = RepeatableMetaAnnotatedElement.create(
			RepeatableAnnotationCollector.standard(), Foo.class, (s, a) -> new GenericAnnotationMapping(a, Objects.isNull(s))
		);

		Annotation[] annotations = element.getAnnotationsByType(Annotation1.class);
		Assertions.assertEquals(1, annotations.length);

		annotations = element.getAnnotationsByType(Annotation2.class);
		Assertions.assertEquals(2, annotations.length);

		annotations = element.getAnnotationsByType(Annotation3.class);
		Assertions.assertEquals(4, annotations.length);

		annotations = element.getAnnotationsByType(Annotation4.class);
		Assertions.assertEquals(7, annotations.length);
	}

	@Test
	public void testGetDeclaredAnnotations() {
		final AnnotatedElement element = RepeatableMetaAnnotatedElement.create(
			RepeatableAnnotationCollector.standard(), Foo.class, (s, a) -> new GenericAnnotationMapping(a, Objects.isNull(s))
		);
		final List<Class<? extends Annotation>> annotationTypes = Arrays.stream(element.getDeclaredAnnotations())
			.map(Annotation::annotationType)
			.collect(Collectors.toList());
		final Map<Class<? extends Annotation>, Integer> countMap = IterUtil.countMap(annotationTypes.iterator());

		Assertions.assertEquals((Integer)1, countMap.get(Annotation1.class));
		Assertions.assertFalse(countMap.containsKey(Annotation2.class));
		Assertions.assertFalse(countMap.containsKey(Annotation3.class));
		Assertions.assertFalse(countMap.containsKey(Annotation4.class));
	}

	@Test
	public void testGetDeclaredAnnotation() {
		final AnnotatedElement element = RepeatableMetaAnnotatedElement.create(
			RepeatableAnnotationCollector.standard(), Foo.class, (s, a) -> new GenericAnnotationMapping(a, Objects.isNull(s))
		);

		final Annotation1 annotation1 = Foo.class.getDeclaredAnnotation(Annotation1.class);
		Assertions.assertEquals(annotation1, element.getDeclaredAnnotation(Annotation1.class));
		Assertions.assertNull(element.getDeclaredAnnotation(Annotation3.class));
		Assertions.assertNull(element.getDeclaredAnnotation(Annotation4.class));
		Assertions.assertNull(element.getDeclaredAnnotation(Annotation2.class));
	}

	@Test
	public void testGetDeclaredAnnotationsByType() {
		final AnnotatedElement element = RepeatableMetaAnnotatedElement.create(
			RepeatableAnnotationCollector.standard(), Foo.class, (s, a) -> new GenericAnnotationMapping(a, Objects.isNull(s))
		);

		Annotation[] annotations = element.getDeclaredAnnotationsByType(Annotation1.class);
		Assertions.assertEquals(1, annotations.length);

		annotations = element.getDeclaredAnnotationsByType(Annotation2.class);
		Assertions.assertEquals(0, annotations.length);

		annotations = element.getDeclaredAnnotationsByType(Annotation3.class);
		Assertions.assertEquals(0, annotations.length);

		annotations = element.getDeclaredAnnotationsByType(Annotation4.class);
		Assertions.assertEquals(0, annotations.length);
	}

	@Test
	public void testGetElement() {
		final AnnotatedElement element = Foo.class;
		final RepeatableMetaAnnotatedElement<GenericAnnotationMapping> repeatableMetaAnnotatedElement = RepeatableMetaAnnotatedElement.create(
			RepeatableAnnotationCollector.standard(), element, (s, a) -> new GenericAnnotationMapping(a, Objects.isNull(s))
		);
		Assertions.assertSame(element, repeatableMetaAnnotatedElement.getElement());
	}

	@Test
	public void testIterator() {
		final RepeatableMetaAnnotatedElement<GenericAnnotationMapping> element = RepeatableMetaAnnotatedElement.create(
			RepeatableAnnotationCollector.standard(), Foo.class, (s, a) -> new GenericAnnotationMapping(a, Objects.isNull(s))
		);
		int count = 0;
		for (final GenericAnnotationMapping mapping : element) {
			Assertions.assertNotNull(mapping);
			count++;
		}
		Assertions.assertEquals(14, count);
	}

	@Annotation4
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation1 {
		Annotation2[] value() default {};
	}

	@Annotation4
	@Repeatable(Annotation1.class)
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation2 {
		Annotation3[] value() default {};
	}

	@Annotation4
	@Repeatable(Annotation2.class)
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation3 { }

	@Annotation4 // 循环引用
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation4 { }

	@Annotation1({
		@Annotation2({@Annotation3, @Annotation3}),
		@Annotation2({@Annotation3, @Annotation3})
	})
	private static class Foo {}

}
