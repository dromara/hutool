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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;

/**
 * test for {@link CombinationAnnotatedElement}
 */
public class CombinationAnnotatedElementTest {

	@Test
	public void testOf() {
		final CombinationAnnotatedElement element = CombinationAnnotatedElement.of(ClassForTest.class, a -> true);
		Assertions.assertNotNull(element);
	}

	@Test
	public void testIsAnnotationPresent() {
		final CombinationAnnotatedElement element = CombinationAnnotatedElement.of(ClassForTest.class, a -> true);
		Assertions.assertTrue(element.isAnnotationPresent(MetaAnnotationForTest.class));
	}

	@Test
	public void testGetAnnotation() {
		final AnnotationForTest annotation1 = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		final MetaAnnotationForTest annotation2 = AnnotationForTest.class.getAnnotation(MetaAnnotationForTest.class);
		final CombinationAnnotatedElement element = CombinationAnnotatedElement.of(ClassForTest.class, a -> true);
		Assertions.assertEquals(annotation1, element.getAnnotation(AnnotationForTest.class));
		Assertions.assertEquals(annotation2, element.getAnnotation(MetaAnnotationForTest.class));
	}

	@Test
	public void testGetAnnotations() {
		final CombinationAnnotatedElement element = CombinationAnnotatedElement.of(ClassForTest.class, a -> true);
		final Annotation[] annotations = element.getAnnotations();
		Assertions.assertEquals(2, annotations.length);
	}

	@Test
	public void testGetDeclaredAnnotations() {
		final CombinationAnnotatedElement element = CombinationAnnotatedElement.of(ClassForTest.class, a -> true);
		final Annotation[] annotations = element.getDeclaredAnnotations();
		Assertions.assertEquals(2, annotations.length);
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface MetaAnnotationForTest{ }

	@MetaAnnotationForTest
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface AnnotationForTest{ }

	@AnnotationForTest
	private static class ClassForTest{}

}
