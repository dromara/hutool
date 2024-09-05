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

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * test for {@link GenericAnnotationMapping}
 *
 * @author huangchengxing
 */
public class GenericAnnotationMappingTest {

	@Test
	public void testEquals() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assertions.assertNotEquals(mapping, null);
		Assertions.assertEquals(mapping, GenericAnnotationMapping.create(annotation, false));
		Assertions.assertNotEquals(mapping, GenericAnnotationMapping.create(annotation, true));
	}

	@Test
	public void testHashCode() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final int hashCode = GenericAnnotationMapping.create(annotation, false).hashCode();
		Assertions.assertEquals(hashCode, GenericAnnotationMapping.create(annotation, false).hashCode());
		Assertions.assertNotEquals(hashCode, GenericAnnotationMapping.create(annotation, true).hashCode());
	}


	@Test
	public void testCreate() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assertions.assertNotNull(mapping);
	}

	@Test
	public void testIsRoot() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, true);
		Assertions.assertTrue(mapping.isRoot());

		mapping = GenericAnnotationMapping.create(annotation, false);
		Assertions.assertFalse(mapping.isRoot());
	}

	@Test
	public void testGetAnnotation() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assertions.assertSame(annotation, mapping.getAnnotation());
	}

	@SneakyThrows
	@Test
	public void testGetAttributes() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		for (int i = 0; i < mapping.getAttributes().length; i++) {
			final Method method = mapping.getAttributes()[i];
			Assertions.assertEquals(Annotation1.class.getDeclaredMethod(method.getName()), method);
		}
	}

	@Test
	public void testAnnotationType() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assertions.assertEquals(annotation.annotationType(), mapping.annotationType());
	}

	@Test
	public void testIsResolved() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assertions.assertFalse(mapping.isResolved());
	}

	@Test
	public void testGetAttributeValue() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assertions.assertEquals(annotation.value(), mapping.getAttributeValue("value", String.class));
		Assertions.assertNull(mapping.getAttributeValue("value", Integer.class));
	}

	@Test
	public void testGetResolvedAnnotation() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assertions.assertSame(annotation, mapping.getResolvedAnnotation());
	}

	@Test
	public void testGetResolvedAttributeValue() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assertions.assertEquals(annotation.value(), mapping.getResolvedAttributeValue("value", String.class));
		Assertions.assertNull(mapping.getResolvedAttributeValue("value", Integer.class));
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation1 {
		String value() default "";
	}

	@Annotation1("foo")
	private static class Foo {}

}
