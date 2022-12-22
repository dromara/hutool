package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MateAnnotationScannerTest {

	@Test
	public void supportTest() {
		AnnotationScanner scanner = new MetaAnnotationScanner();
		Assert.assertTrue(scanner.support(AnnotationForScannerTest.class));
		Assert.assertFalse(scanner.support(ReflectUtil.getField(Example.class, "id")));
		Assert.assertFalse(scanner.support(ReflectUtil.getMethod(Example.class, "getId")));
		Assert.assertFalse(scanner.support(null));
		Assert.assertFalse(scanner.support(Example.class));
	}

	@Test
	public void getAnnotationsTest() {
		AnnotationScanner scanner = new MetaAnnotationScanner();
		Assert.assertTrue(scanner.support(AnnotationForScannerTest3.class));
		Map<Class<? extends Annotation>, Annotation> annotations = CollUtil.toMap(scanner.getAnnotations(AnnotationForScannerTest3.class), new HashMap<>(), Annotation::annotationType);
		Assert.assertEquals(3, annotations.size());
		Assert.assertTrue(annotations.containsKey(AnnotationForScannerTest.class));
		Assert.assertTrue(annotations.containsKey(AnnotationForScannerTest1.class));
		Assert.assertTrue(annotations.containsKey(AnnotationForScannerTest2.class));
		Assert.assertFalse(annotations.containsKey(AnnotationForScannerTest3.class));

		scanner = new MetaAnnotationScanner(false);
		Assert.assertTrue(scanner.support(AnnotationForScannerTest3.class));
		annotations = CollUtil.toMap(scanner.getAnnotations(AnnotationForScannerTest3.class), new HashMap<>(), Annotation::annotationType);
		Assert.assertEquals(1, annotations.size());
		Assert.assertTrue(annotations.containsKey(AnnotationForScannerTest2.class));
		Assert.assertFalse(annotations.containsKey(AnnotationForScannerTest.class));
		Assert.assertFalse(annotations.containsKey(AnnotationForScannerTest1.class));
		Assert.assertFalse(annotations.containsKey(AnnotationForScannerTest3.class));
	}

	@Test
	public void scanTest() {
		AnnotationScanner scanner = new MetaAnnotationScanner();
		Map<Integer, List<Annotation>> map = new HashMap<>();
		scanner.scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			AnnotationForScannerTest3.class, null
		);

		Assert.assertEquals(3, map.size());
		Assert.assertEquals(1, map.get(0).size());
		Assert.assertEquals(AnnotationForScannerTest2.class, map.get(0).get(0).annotationType());

		Assert.assertEquals(1, map.get(1).size());
		Assert.assertEquals(AnnotationForScannerTest1.class, map.get(1).get(0).annotationType());

		Assert.assertEquals(1, map.get(2).size());
		Assert.assertEquals(AnnotationForScannerTest.class, map.get(2).get(0).annotationType());
	}

	static class Example {
		private Integer id;
		public Integer getId() {
			return id;
		}
	}

	@AnnotationForScannerTest
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
	@interface AnnotationForScannerTest1 {}

	@AnnotationForScannerTest1
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
	@interface AnnotationForScannerTest2 {}

	@AnnotationForScannerTest2
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
	@interface AnnotationForScannerTest3 {}
}
