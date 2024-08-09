package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class MethodAnnotationScannerTest {

	@Test
	public void supportTest() {
		AnnotationScanner scanner = new MethodAnnotationScanner();
		assertTrue(scanner.support(ReflectUtil.getMethod(Example.class, "test")));
		assertFalse(scanner.support(null));
		assertFalse(scanner.support(Example.class));
		assertFalse(scanner.support(ReflectUtil.getField(Example.class, "id")));
	}

	@Test
	public void getAnnotationsTest() {
		AnnotationScanner scanner = new MethodAnnotationScanner();
		Method method = ReflectUtil.getMethod(Example.class, "test");
		assertNotNull(method);

		// 不查找父类中具有相同方法签名的方法
		List<Annotation> annotations = scanner.getAnnotations(method);
		assertEquals(1, annotations.size());
		assertEquals(CollUtil.getFirst(annotations).annotationType(), AnnotationForScannerTest.class);

		// 查找父类中具有相同方法签名的方法
		scanner = new MethodAnnotationScanner(true);
		annotations = scanner.getAnnotations(method);
		assertEquals(3, annotations.size());
		assertEquals("Example", ((AnnotationForScannerTest) annotations.get(0)).value());
		assertEquals("SuperClass", ((AnnotationForScannerTest) annotations.get(1)).value());
		assertEquals("SuperInterface", ((AnnotationForScannerTest) annotations.get(2)).value());

		// 查找父类中具有相同方法签名的方法，但是不查找SuperInterface
		scanner = new MethodAnnotationScanner(true).addExcludeTypes(SuperInterface.class);
		annotations = scanner.getAnnotations(method);
		assertEquals(2, annotations.size());
		assertEquals("Example", ((AnnotationForScannerTest) annotations.get(0)).value());
		assertEquals("SuperClass", ((AnnotationForScannerTest) annotations.get(1)).value());

		// 查找父类中具有相同方法签名的方法，但是只查找SuperClass
		scanner = new MethodAnnotationScanner(true)
			.setFilter(t -> ClassUtil.isAssignable(SuperClass.class, t));
		annotations = scanner.getAnnotations(method);
		assertEquals(2, annotations.size());
		assertEquals("Example", ((AnnotationForScannerTest) annotations.get(0)).value());
		assertEquals("SuperClass", ((AnnotationForScannerTest) annotations.get(1)).value());
	}

	@Test
	public void scanTest() {
		Method method = ReflectUtil.getMethod(Example.class, "test");

		// 不查找父类中具有相同方法签名的方法
		Map<Integer, List<Annotation>> map = new HashMap<>();
		new MethodAnnotationScanner(false).scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			method, null
		);
		assertEquals(1, map.get(0).size());
		assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());

		// 查找父类中具有相同方法签名的方法
		map.clear();
		new MethodAnnotationScanner(true).scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			method, null
		);
		assertEquals(3, map.size());
		assertEquals(1, map.get(0).size());
		assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		assertEquals(1, map.get(1).size());
		assertEquals("SuperClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
		assertEquals(1, map.get(2).size());
		assertEquals("SuperInterface", ((AnnotationForScannerTest) map.get(2).get(0)).value());

		// 查找父类中具有相同方法签名的方法，但是不查找SuperInterface
		map.clear();
		new MethodAnnotationScanner(true)
			.addExcludeTypes(SuperInterface.class)
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				method, null
			);
		assertEquals(2, map.size());
		assertEquals(1, map.get(0).size());
		assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		assertEquals(1, map.get(1).size());
		assertEquals("SuperClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());

		// 查找父类中具有相同方法签名的方法，但是只查找SuperClass
		map.clear();
		new MethodAnnotationScanner(true)
			.setFilter(t -> ClassUtil.isAssignable(SuperClass.class, t))
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				method, null
			);
		assertEquals(2, map.size());
		assertEquals(1, map.get(0).size());
		assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		assertEquals(1, map.get(1).size());
		assertEquals("SuperClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
	}

	static class Example extends SuperClass {
		private Integer id;

		@Override
		@AnnotationForScannerTest("Example")
		public List<?> test() { return Collections.emptyList(); }
	}

	static class SuperClass implements SuperInterface {

		@Override
		@AnnotationForScannerTest("SuperClass")
		public Collection<?> test() { return Collections.emptyList(); }

	}

	interface SuperInterface {

		@AnnotationForScannerTest("SuperInterface")
		Object test();

	}

}
