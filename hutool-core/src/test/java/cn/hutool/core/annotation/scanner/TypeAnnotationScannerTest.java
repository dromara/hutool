package cn.hutool.core.annotation.scanner;

import cn.hutool.core.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author huangchengxing
 * @date 2022/06/10 16:51
 */
public class TypeAnnotationScannerTest {

	@Test
	public void testTypeAnnotationScanner() {
		AnnotationScanner scanner = new TypeAnnotationScanner();
		Assert.assertTrue(scanner.support(Example.class));
		List<Annotation> annotations = scanner.getAnnotations(Example.class);
		Assert.assertEquals(3, annotations.size());
		annotations.forEach(a -> Assert.assertEquals(a.annotationType(), AnnotationForScannerTest.class));

		// 不查找父接口
		scanner = new TypeAnnotationScanner().setIncludeInterfaces(false);
		Assert.assertTrue(scanner.support(Example.class));
		annotations = scanner.getAnnotations(Example.class);
		Assert.assertEquals(2, annotations.size());
		annotations.forEach(a -> Assert.assertEquals(a.annotationType(), AnnotationForScannerTest.class));

		// 不查找父类
		scanner = new TypeAnnotationScanner().setIncludeSupperClass(false);
		Assert.assertTrue(scanner.support(Example.class));
		annotations = scanner.getAnnotations(Example.class);
		Assert.assertEquals(1, annotations.size());
		annotations.forEach(a -> Assert.assertEquals(a.annotationType(), AnnotationForScannerTest.class));

		// 不查找ExampleSupplerClass.class
		scanner = new TypeAnnotationScanner().addExcludeTypes(ExampleSupplerClass.class);
		Assert.assertTrue(scanner.support(Example.class));
		annotations = scanner.getAnnotations(Example.class);
		Assert.assertEquals(1, annotations.size());
		annotations.forEach(a -> Assert.assertEquals(a.annotationType(), AnnotationForScannerTest.class));

		// 只查找ExampleSupplerClass.class
		scanner = new TypeAnnotationScanner().setFilter(t -> ClassUtil.isAssignable(ExampleSupplerClass.class, t));
		Assert.assertTrue(scanner.support(Example.class));
		annotations = scanner.getAnnotations(Example.class);
		Assert.assertEquals(2, annotations.size());
		annotations.forEach(a -> Assert.assertEquals(a.annotationType(), AnnotationForScannerTest.class));
	}

	@AnnotationForScannerTest
	static class ExampleSupplerClass implements ExampleInterface {}

	@AnnotationForScannerTest
	interface ExampleInterface {}

	@AnnotationForScannerTest
	static class Example extends ExampleSupplerClass {}

}
