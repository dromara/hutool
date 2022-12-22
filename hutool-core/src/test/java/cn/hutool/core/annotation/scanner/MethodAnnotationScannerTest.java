package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class MethodAnnotationScannerTest {

	@Test
	public void supportTest() {
		AnnotationScanner scanner = new MethodAnnotationScanner();
		Assert.assertTrue(scanner.support(ReflectUtil.getMethod(Example.class, "test")));
		Assert.assertFalse(scanner.support(null));
		Assert.assertFalse(scanner.support(Example.class));
		Assert.assertFalse(scanner.support(ReflectUtil.getField(Example.class, "id")));
	}

	@Test
	public void getAnnotationsTest() {
		AnnotationScanner scanner = new MethodAnnotationScanner();
		Method method = ReflectUtil.getMethod(Example.class, "test");
		Assert.assertNotNull(method);

		// 不查找父类中具有相同方法签名的方法
		List<Annotation> annotations = scanner.getAnnotations(method);
		Assert.assertEquals(1, annotations.size());
		Assert.assertEquals(CollUtil.getFirst(annotations).annotationType(), AnnotationForScannerTest.class);

		// 查找父类中具有相同方法签名的方法
		scanner = new MethodAnnotationScanner(true);
		annotations = scanner.getAnnotations(method);
		Assert.assertEquals(3, annotations.size());
		Assert.assertEquals("Example", ((AnnotationForScannerTest) annotations.get(0)).value());
		Assert.assertEquals("SuperClass", ((AnnotationForScannerTest) annotations.get(1)).value());
		Assert.assertEquals("SuperInterface", ((AnnotationForScannerTest) annotations.get(2)).value());

		// 查找父类中具有相同方法签名的方法，但是不查找SuperInterface
		scanner = new MethodAnnotationScanner(true).addExcludeTypes(SuperInterface.class);
		annotations = scanner.getAnnotations(method);
		Assert.assertEquals(2, annotations.size());
		Assert.assertEquals("Example", ((AnnotationForScannerTest) annotations.get(0)).value());
		Assert.assertEquals("SuperClass", ((AnnotationForScannerTest) annotations.get(1)).value());

		// 查找父类中具有相同方法签名的方法，但是只查找SuperClass
		scanner = new MethodAnnotationScanner(true)
			.setFilter(t -> ClassUtil.isAssignable(SuperClass.class, t));
		annotations = scanner.getAnnotations(method);
		Assert.assertEquals(2, annotations.size());
		Assert.assertEquals("Example", ((AnnotationForScannerTest) annotations.get(0)).value());
		Assert.assertEquals("SuperClass", ((AnnotationForScannerTest) annotations.get(1)).value());
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
		Assert.assertEquals(1, map.get(0).size());
		Assert.assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());

		// 查找父类中具有相同方法签名的方法
		map.clear();
		new MethodAnnotationScanner(true).scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			method, null
		);
		Assert.assertEquals(3, map.size());
		Assert.assertEquals(1, map.get(0).size());
		Assert.assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		Assert.assertEquals(1, map.get(1).size());
		Assert.assertEquals("SuperClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
		Assert.assertEquals(1, map.get(2).size());
		Assert.assertEquals("SuperInterface", ((AnnotationForScannerTest) map.get(2).get(0)).value());

		// 查找父类中具有相同方法签名的方法，但是不查找SuperInterface
		map.clear();
		new MethodAnnotationScanner(true)
			.addExcludeTypes(SuperInterface.class)
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				method, null
			);
		Assert.assertEquals(2, map.size());
		Assert.assertEquals(1, map.get(0).size());
		Assert.assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		Assert.assertEquals(1, map.get(1).size());
		Assert.assertEquals("SuperClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());

		// 查找父类中具有相同方法签名的方法，但是只查找SuperClass
		map.clear();
		new MethodAnnotationScanner(true)
			.setFilter(t -> ClassUtil.isAssignable(SuperClass.class, t))
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				method, null
			);
		Assert.assertEquals(2, map.size());
		Assert.assertEquals(1, map.get(0).size());
		Assert.assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		Assert.assertEquals(1, map.get(1).size());
		Assert.assertEquals("SuperClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
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
