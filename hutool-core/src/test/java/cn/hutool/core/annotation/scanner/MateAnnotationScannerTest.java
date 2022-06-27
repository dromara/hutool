package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangchengxing
 * @date 2022/06/10 16:51
 */
public class MateAnnotationScannerTest {

	@Test
	public void testMateAnnotationScanner() {
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

	@AnnotationForScannerTest3
	static class Example {}

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
