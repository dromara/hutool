package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldAnnotationScannerTest {

	@Test
	public void supportTest() {
		AnnotationScanner scanner = new FieldAnnotationScanner();
		Assert.assertTrue(scanner.support(ReflectUtil.getField(Example.class, "id")));
		Assert.assertFalse(scanner.support(ReflectUtil.getMethod(Example.class, "getId")));
		Assert.assertFalse(scanner.support(null));
		Assert.assertFalse(scanner.support(Example.class));
	}

	@Test
	public void getAnnotationsTest() {
		AnnotationScanner scanner = new FieldAnnotationScanner();
		Field field = ReflectUtil.getField(Example.class, "id");
		Assert.assertNotNull(field);
		Assert.assertTrue(scanner.support(field));
		List<Annotation> annotations = scanner.getAnnotations(field);
		Assert.assertEquals(1, annotations.size());
		Assert.assertEquals(AnnotationForScannerTest.class, CollUtil.getFirst(annotations).annotationType());
	}

	@Test
	public void scanTest() {
		AnnotationScanner scanner = new FieldAnnotationScanner();
		Field field = ReflectUtil.getField(Example.class, "id");
		Map<Integer, List<Annotation>> map = new HashMap<>();
		scanner.scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			field, null
		);
		Assert.assertEquals(1, map.size());
		Assert.assertEquals(1, map.get(0).size());
		Assert.assertEquals(AnnotationForScannerTest.class, map.get(0).get(0).annotationType());
	}

	public static class Example {
		@AnnotationForScannerTest
		private Integer id;

		public Integer getId() {
			return id;
		}
	}

}
