/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
