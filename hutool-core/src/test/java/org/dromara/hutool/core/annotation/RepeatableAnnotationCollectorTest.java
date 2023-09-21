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

import org.dromara.hutool.core.text.CharSequenceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * test for {@link RepeatableAnnotationCollector}
 *
 * @author huangchengxing
 */
public class RepeatableAnnotationCollectorTest {

	private static final Annotation1 ANNOTATION1 = Foo.class.getAnnotation(Annotation1.class);
	private static final List<Annotation2> ANNOTATION2S = Arrays.asList(ANNOTATION1.value());
	private static final List<Annotation3> ANNOTATION3S = ANNOTATION2S.stream()
		.map(Annotation2::value)
		.flatMap(Stream::of)
		.collect(Collectors.toList());
	private static final List<Annotation> ANNOTATIONS = new ArrayList<>();
	static {
		ANNOTATIONS.add(ANNOTATION1);
		ANNOTATIONS.addAll(ANNOTATION2S);
		ANNOTATIONS.addAll(ANNOTATION3S);
	}

	private static final BiPredicate<Annotation, Method> PREDICATE = (annotation, attribute) -> {
		// 属性名需为“value”
		if (!CharSequenceUtil.equals("value", attribute.getName())) {
			return false;
		}
		final Class<?> attributeType = attribute.getReturnType();
		// 返回值类型需为数组
		return attributeType.isArray()
			// 且数组元素需为注解
			&& attributeType.getComponentType()
			.isAnnotation()
			// 该注解类必须被@Repeatable注解，但不要求与当前属性的声明方法一致
			&& attributeType.getComponentType()
			.isAnnotationPresent(Repeatable.class);
	};

	@Test
	public void testNone() {
		final RepeatableAnnotationCollector collector = RepeatableAnnotationCollector.none();
		Assertions.assertSame(collector, RepeatableAnnotationCollector.none());

		Assertions.assertEquals(0, collector.getFinalRepeatableAnnotations(null).size());

		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		Assertions.assertEquals(Collections.singletonList(annotation), collector.getFinalRepeatableAnnotations(annotation));

		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(Collections.singletonList(annotation3), collector.getFinalRepeatableAnnotations(annotation3));

		Assertions.assertEquals(Collections.singletonList(annotation3), collector.getRepeatableAnnotations(annotation3, Annotation3.class));
		Assertions.assertTrue(collector.getRepeatableAnnotations(annotation3, Annotation1.class).isEmpty());
		Assertions.assertTrue(collector.getRepeatableAnnotations(null, Annotation1.class).isEmpty());
	}

	@Test
	public void testNoneWhenAccumulate() {
		final RepeatableAnnotationCollector collector = RepeatableAnnotationCollector.none();
		Assertions.assertSame(collector, RepeatableAnnotationCollector.none());

		Assertions.assertEquals(0, collector.getAllRepeatableAnnotations(null).size());

		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		Assertions.assertEquals(Collections.singletonList(annotation), collector.getAllRepeatableAnnotations(annotation));

		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(Collections.singletonList(annotation3), collector.getAllRepeatableAnnotations(annotation3));
	}

	@Test
	public void testGenericCollector() {
		final RepeatableAnnotationCollector collector = RepeatableAnnotationCollector.standard();
		Assertions.assertSame(collector, RepeatableAnnotationCollector.standard());

		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final List<Annotation3> annotations = Stream.of(annotation.value())
			.map(Annotation2::value)
			.flatMap(Stream::of)
			.collect(Collectors.toList());
		Assertions.assertEquals(annotations, collector.getFinalRepeatableAnnotations(annotation));
		Assertions.assertEquals(ANNOTATION3S, collector.getFinalRepeatableAnnotations(ANNOTATION1));

		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(Collections.singletonList(annotation3), collector.getFinalRepeatableAnnotations(annotation3));

		Assertions.assertEquals(Collections.singletonList(ANNOTATION1), collector.getRepeatableAnnotations(ANNOTATION1, Annotation1.class));
		Assertions.assertEquals(ANNOTATION2S, collector.getRepeatableAnnotations(ANNOTATION1, Annotation2.class));
		Assertions.assertEquals(ANNOTATION3S, collector.getRepeatableAnnotations(ANNOTATION1, Annotation3.class));
	}

	@Test
	public void testGenericCollectorWhenAccumulate() {
		final RepeatableAnnotationCollector collector = RepeatableAnnotationCollector.standard();
		Assertions.assertSame(collector, RepeatableAnnotationCollector.standard());

		final List<Annotation> annotations = new ArrayList<>();
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		annotations.add(annotation);
		annotations.addAll(Arrays.asList(annotation.value()));
		Stream.of(annotation.value())
			.map(Annotation2::value)
			.flatMap(Stream::of)
			.forEach(annotations::add);
		Assertions.assertEquals(annotations, collector.getAllRepeatableAnnotations(annotation));

		Assertions.assertEquals(ANNOTATIONS, collector.getAllRepeatableAnnotations(ANNOTATION1));

		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(Collections.singletonList(annotation3), collector.getAllRepeatableAnnotations(annotation3));
	}

	@Test
	public void testConditionCollector() {
		final RepeatableAnnotationCollector collector = RepeatableAnnotationCollector.condition(PREDICATE);
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final List<Annotation3> annotations = Stream.of(annotation.value())
			.map(Annotation2::value)
			.flatMap(Stream::of)
			.collect(Collectors.toList());
		Assertions.assertEquals(annotations, collector.getFinalRepeatableAnnotations(annotation));

		Assertions.assertEquals(ANNOTATION3S, collector.getFinalRepeatableAnnotations(ANNOTATION1));

		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(Collections.singletonList(annotation3), collector.getFinalRepeatableAnnotations(annotation3));

		Assertions.assertEquals(Collections.singletonList(ANNOTATION1), collector.getRepeatableAnnotations(ANNOTATION1, Annotation1.class));
		Assertions.assertEquals(ANNOTATION2S, collector.getRepeatableAnnotations(ANNOTATION1, Annotation2.class));
		Assertions.assertEquals(ANNOTATION3S, collector.getRepeatableAnnotations(ANNOTATION1, Annotation3.class));
	}

	@Test
	public void testConditionCollectorWhenAccumulate() {
		final RepeatableAnnotationCollector collector = RepeatableAnnotationCollector.condition(PREDICATE);

		final List<Annotation> annotations = new ArrayList<>();
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		annotations.add(annotation);
		annotations.addAll(Arrays.asList(annotation.value()));
		Stream.of(annotation.value())
			.map(Annotation2::value)
			.flatMap(Stream::of)
			.forEach(annotations::add);
		Assertions.assertEquals(annotations, collector.getAllRepeatableAnnotations(annotation));
		Assertions.assertEquals(ANNOTATIONS, collector.getAllRepeatableAnnotations(ANNOTATION1));

		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(Collections.singletonList(annotation3), collector.getAllRepeatableAnnotations((annotation3)));
	}

	@Test
	public void testFullCollector() {
		final RepeatableAnnotationCollector collector = RepeatableAnnotationCollector.full();
		Assertions.assertSame(collector, RepeatableAnnotationCollector.full());

		Assertions.assertEquals(ANNOTATION3S, collector.getFinalRepeatableAnnotations(ANNOTATION1));

		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(Collections.singletonList(annotation3), collector.getFinalRepeatableAnnotations(annotation3));

		Assertions.assertEquals(Collections.singletonList(ANNOTATION1), collector.getRepeatableAnnotations(ANNOTATION1, Annotation1.class));
		Assertions.assertEquals(ANNOTATION2S, collector.getRepeatableAnnotations(ANNOTATION1, Annotation2.class));
		Assertions.assertEquals(ANNOTATION3S, collector.getRepeatableAnnotations(ANNOTATION1, Annotation3.class));
	}

	@Test
	public void testFullCollectorWhenAccumulate() {
		final RepeatableAnnotationCollector collector = RepeatableAnnotationCollector.full();
		Assertions.assertSame(collector, RepeatableAnnotationCollector.full());

		Assertions.assertEquals(ANNOTATIONS, collector.getAllRepeatableAnnotations(ANNOTATION1));

		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(Collections.singletonList(annotation3), collector.getAllRepeatableAnnotations(annotation3));
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation1 {
		Annotation2[] value() default {};
	}

	@Repeatable(Annotation1.class)
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation2 {
		Annotation3[] value() default {};
	}

	@SuppressWarnings("unused")
	@Repeatable(Annotation2.class)
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation3 {
		int value();
		String name() default "";
	}

	@Annotation3(Integer.MIN_VALUE)
	@Annotation1({
		@Annotation2({@Annotation3(1), @Annotation3(2)}),
		@Annotation2({@Annotation3(3), @Annotation3(4)})
	})
	private static class Foo {}

}
