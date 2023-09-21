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

package org.dromara.hutool.core.reflect.method;

import lombok.SneakyThrows;
import org.dromara.hutool.core.annotation.Alias;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * test for {@link MethodScanner}
 *
 * @author huangchengxing
 */
class MethodScannerTest {

	@Test
	void testGetMethods() {
		Assertions.assertEquals(0, MethodScanner.getMethods(null).length);
		final Method[] actual = MethodScanner.getMethods(Child.class);
		Assertions.assertSame(actual, MethodScanner.getMethods(Child.class));
		final Method[] expected = Child.class.getMethods();
		Assertions.assertArrayEquals(expected, actual);
	}

	@Test
	void testGetDeclaredMethods() {
		Assertions.assertEquals(0, MethodScanner.getDeclaredMethods(null).length);
		final Method[] actual = MethodScanner.getDeclaredMethods(Child.class);
		Assertions.assertSame(actual, MethodScanner.getDeclaredMethods(Child.class));
		final Method[] expected = Child.class.getDeclaredMethods();
		Assertions.assertArrayEquals(expected, actual);
	}

	@Test
	void testGetAllMethods() {
		Assertions.assertEquals(0, MethodScanner.getAllMethods(null).length);
		final Method[] actual = MethodScanner.getAllMethods(Child.class);
		// get declared method from child、parent、grandparent
		final Method[] expected = Stream.of(Child.class, Parent.class, Interface.class, Object.class)
			.flatMap(c -> Stream.of(MethodScanner.getDeclaredMethods(c)))
			.toArray(Method[]::new);
		Assertions.assertArrayEquals(expected, actual);
	}

	@Test
	void testClearCaches() {
		final Method[] declaredMethods = MethodScanner.getDeclaredMethods(Child.class);
		Assertions.assertSame(declaredMethods, MethodScanner.getDeclaredMethods(Child.class));
		final Method[] methods = MethodScanner.getMethods(Child.class);
		Assertions.assertSame(methods, MethodScanner.getMethods(Child.class));

		// clear method cache
		MethodScanner.clearCaches();
		Assertions.assertNotSame(declaredMethods, MethodScanner.getDeclaredMethods(Child.class));
		Assertions.assertNotSame(methods, MethodScanner.getMethods(Child.class));
	}

	@SneakyThrows
	@Test
	void testFindWithMetadataFromSpecificMethods() {
		Assertions.assertTrue(MethodScanner.findWithMetadataFromSpecificMethods(null, m -> m.getAnnotation(Annotation.class)).isEmpty());
		final Method[] methods = MethodScanner.getMethods(Child.class);
		final Map<Method, Annotation> actual = MethodScanner.findWithMetadataFromSpecificMethods(methods, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.containsKey(expectedMethod));

		// check annotation
		final Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.get(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testFindFromSpecificMethods() {
		final Method[] methods = MethodScanner.getMethods(Child.class);
		final Set<Method> actual = MethodScanner.findFromSpecificMethods(methods, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.contains(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testGetWithMetadataFromSpecificMethods() {
		// find first oneArgMethod method
		final Method[] methods = MethodScanner.getMethods(Child.class);
		final Map.Entry<Method, Boolean> actual = MethodScanner.getWithMetadataFromSpecificMethods(methods, MethodMatcherUtil.forName("oneArgMethod"));
		Assertions.assertNotNull(actual);
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual.getKey());
		Assertions.assertTrue(actual.getValue());
	}

	@SneakyThrows
	@Test
	void testGetFromSpecificMethods() {
		// find first oneArgMethod method
		final Method[] methods = MethodScanner.getMethods(Child.class);
		final Method actual = MethodScanner.getFromSpecificMethods(methods, MethodMatcherUtil.forName("oneArgMethod"));
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual);
	}

	@SneakyThrows
	@Test
	void testFindWithMetadataFromMethods() {
		final Map<Method, Annotation> actual = MethodScanner.findWithMetadataFromMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.containsKey(expectedMethod));

		// check annotation
		final Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.get(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testFindFromMethods() {
		final Set<Method> actual = MethodScanner.findFromMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.contains(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testGetWithMetadataFromMethods() {
		final Map.Entry<Method, Annotation> actual = MethodScanner.getWithMetadataFromMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertNotNull(actual);
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual.getKey());
		final Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.getValue());
	}

	@SneakyThrows
	@Test
	void testGetFromMethods() {
		final Method actual = MethodScanner.getFromMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual);
	}

	@SneakyThrows
	@Test
	void testFindWithMetadataFromDeclaredMethods() {
		final Map<Method, Annotation> actual = MethodScanner.findWithMetadataFromDeclaredMethods(Parent.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.containsKey(expectedMethod));

		// check annotation
		final Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.get(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testFindFromDeclaredMethods() {
		final Set<Method> actual = MethodScanner.findFromDeclaredMethods(Parent.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.contains(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testGetWithMetadataFromDeclaredMethods() {
		final Map.Entry<Method, Annotation> actual = MethodScanner.getWithMetadataFromDeclaredMethods(Parent.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertNotNull(actual);
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual.getKey());
		final Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.getValue());
	}

	@SneakyThrows
	@Test
	void testGetFromDeclaredMethods() {
		final Method actual = MethodScanner.getFromDeclaredMethods(Parent.class, m -> m.getAnnotation(Annotation.class));
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual);
	}

	@SneakyThrows
	@Test
	void testFindWithMetadataFromAllMethods() {
		final Map<Method, Annotation> actual = MethodScanner.findWithMetadataFromAllMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.containsKey(expectedMethod));

		// check annotation
		final Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.get(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testFindFromAllMethods() {
		final Set<Method> actual = MethodScanner.findFromAllMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.contains(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testGetWithMetadataFromAllMethods() {
		Assertions.assertNull(MethodScanner.getWithMetadataFromAllMethods(Child.class, m -> m.getAnnotation(Alias.class)));
		final Map.Entry<Method, Annotation> actual = MethodScanner.getWithMetadataFromAllMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertNotNull(actual);
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual.getKey());
		final Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.getValue());
	}

	@SneakyThrows
	@Test
	void testGetFromAllMethods() {
		final Method actual = MethodScanner.getFromAllMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		final Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual);
	}

	@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Annotation {
		String value() default "default";
	}

	@Annotation("Interface")
	public interface Interface {
		static void staticMethod() {

		}
		void noneArgMethod();
		void oneArgMethod(String arg);
	}

	public static class Parent implements Interface {
		@Override
		public void noneArgMethod() { }

		@Annotation("oneArgMethod")
		@Override
		public void oneArgMethod(final String arg) { }
	}

	public static class Child extends Parent implements Interface {

	}
}
