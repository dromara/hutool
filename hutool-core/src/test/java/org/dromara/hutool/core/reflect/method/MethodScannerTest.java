package org.dromara.hutool.core.reflect.method;

import lombok.SneakyThrows;
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
		Method[] actual = MethodScanner.getMethods(Child.class);
		Assertions.assertSame(actual, MethodScanner.getMethods(Child.class));
		Method[] expected = Child.class.getMethods();
		Assertions.assertArrayEquals(expected, actual);
	}

	@Test
	void testGetDeclaredMethods() {
		Assertions.assertEquals(0, MethodScanner.getDeclaredMethods(null).length);
		Method[] actual = MethodScanner.getDeclaredMethods(Child.class);
		Assertions.assertSame(actual, MethodScanner.getDeclaredMethods(Child.class));
		Method[] expected = Child.class.getDeclaredMethods();
		Assertions.assertArrayEquals(expected, actual);
	}

	@Test
	void testGetAllMethods() {
		Assertions.assertEquals(0, MethodScanner.getAllMethods(null).length);
		Method[] actual = MethodScanner.getAllMethods(Child.class);
		// get declared method from child、parent、grandparent
		Method[] expected = Stream.of(Child.class, Parent.class, Interface.class, Object.class)
			.flatMap(c -> Stream.of(MethodScanner.getDeclaredMethods(c)))
			.toArray(Method[]::new);
		Assertions.assertArrayEquals(expected, actual);
	}

	@Test
	void testClearCaches() {
		Method[] declaredMethods = MethodScanner.getDeclaredMethods(Child.class);
		Assertions.assertSame(declaredMethods, MethodScanner.getDeclaredMethods(Child.class));
		Method[] methods = MethodScanner.getMethods(Child.class);
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
		Method[] methods = MethodScanner.getMethods(Child.class);
		Map<Method, Annotation> actual = MethodScanner.findWithMetadataFromSpecificMethods(methods, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.containsKey(expectedMethod));

		// check annotation
		Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.get(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testFindFromSpecificMethods() {
		Method[] methods = MethodScanner.getMethods(Child.class);
		Set<Method> actual = MethodScanner.findFromSpecificMethods(methods, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.contains(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testGetWithMetadataFromSpecificMethods() {
		// find first oneArgMethod method
		Method[] methods = MethodScanner.getMethods(Child.class);
		Map.Entry<Method, Boolean> actual = MethodScanner.getWithMetadataFromSpecificMethods(methods, MethodMatcherUtil.forName("oneArgMethod"));
		Assertions.assertNotNull(actual);
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual.getKey());
		Assertions.assertTrue(actual.getValue());
	}

	@SneakyThrows
	@Test
	void testGetFromSpecificMethods() {
		// find first oneArgMethod method
		Method[] methods = MethodScanner.getMethods(Child.class);
		Method actual = MethodScanner.getFromSpecificMethods(methods, MethodMatcherUtil.forName("oneArgMethod"));
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual);
	}

	@SneakyThrows
	@Test
	void testFindWithMetadataFromMethods() {
		Map<Method, Annotation> actual = MethodScanner.findWithMetadataFromMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.containsKey(expectedMethod));

		// check annotation
		Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.get(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testFindFromMethods() {
		Set<Method> actual = MethodScanner.findFromMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.contains(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testGetWithMetadataFromMethods() {
		Map.Entry<Method, Annotation> actual = MethodScanner.getWithMetadataFromMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertNotNull(actual);
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual.getKey());
		Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.getValue());
	}

	@SneakyThrows
	@Test
	void testGetFromMethods() {
		Method actual = MethodScanner.getFromMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual);
	}

	@SneakyThrows
	@Test
	void testFindWithMetadataFromDeclaredMethods() {
		Map<Method, Annotation> actual = MethodScanner.findWithMetadataFromDeclaredMethods(Parent.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.containsKey(expectedMethod));

		// check annotation
		Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.get(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testFindFromDeclaredMethods() {
		Set<Method> actual = MethodScanner.findFromDeclaredMethods(Parent.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.contains(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testGetWithMetadataFromDeclaredMethods() {
		Map.Entry<Method, Annotation> actual = MethodScanner.getWithMetadataFromDeclaredMethods(Parent.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertNotNull(actual);
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual.getKey());
		Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.getValue());
	}

	@SneakyThrows
	@Test
	void testGetFromDeclaredMethods() {
		Method actual = MethodScanner.getFromDeclaredMethods(Parent.class, m -> m.getAnnotation(Annotation.class));
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual);
	}

	@SneakyThrows
	@Test
	void testFindWithMetadataFromAllMethods() {
		Map<Method, Annotation> actual = MethodScanner.findWithMetadataFromAllMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.containsKey(expectedMethod));

		// check annotation
		Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.get(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testFindFromAllMethods() {
		Set<Method> actual = MethodScanner.findFromAllMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertEquals(1, actual.size());

		// check method
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertTrue(actual.contains(expectedMethod));
	}

	@SneakyThrows
	@Test
	void testGetWithMetadataFromAllMethods() {
		Assertions.assertNull(MethodScanner.getWithMetadataFromAllMethods(Child.class, m -> m.getAnnotation(Deprecated.class)));
		Map.Entry<Method, Annotation> actual = MethodScanner.getWithMetadataFromAllMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Assertions.assertNotNull(actual);
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
		Assertions.assertEquals(expectedMethod, actual.getKey());
		Annotation expectedAnnotation = expectedMethod.getAnnotation(Annotation.class);
		Assertions.assertEquals(expectedAnnotation, actual.getValue());
	}

	@SneakyThrows
	@Test
	void testGetFromAllMethods() {
		Method actual = MethodScanner.getFromAllMethods(Child.class, m -> m.getAnnotation(Annotation.class));
		Method expectedMethod = Parent.class.getDeclaredMethod("oneArgMethod", String.class);
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
		public void oneArgMethod(String arg) { }
	}

	public static class Child extends Parent implements Interface {

	}
}
