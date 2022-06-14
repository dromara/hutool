package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author huangchengxing
 */
public class FieldAnnotationScannerTest {

	@Test
	public void testFieldAnnotationScanner() {
		FieldAnnotationScanner scanner = new FieldAnnotationScanner();
		Field field = ReflectUtil.getField(Example.class, "id");
		Assert.assertNotNull(field);
		Assert.assertTrue(scanner.support(field));
		List<Annotation> annotations = scanner.getAnnotations(field);
		Assert.assertEquals(1, annotations.size());
		Assert.assertEquals(AnnotationForScannerTest.class, CollUtil.getFirst(annotations).annotationType());
	}

	public static class Example {
		@AnnotationForScannerTest
		private Integer id;
	}

}
